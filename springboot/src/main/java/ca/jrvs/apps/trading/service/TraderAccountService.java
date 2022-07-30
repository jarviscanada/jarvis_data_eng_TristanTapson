package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.*;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import ca.jrvs.apps.trading.model.domain.Trader;
import ca.jrvs.apps.trading.model.domain.TraderAccountView;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TraderAccountService {

    private TraderDao traderDao;
    private AccountDao accountDao;
    private PositionDao positionDao;
    private SecurityOrderDao securityOrderDao;

    @Autowired
    public TraderAccountService(TraderDao traderDao, AccountDao accountDao,
                                PositionDao positionDao, SecurityOrderDao securityOrderDao) {
        this.traderDao = traderDao;
        this.accountDao = accountDao;
        this.positionDao = positionDao;
        this.securityOrderDao = securityOrderDao;
    }

    /**
     * Create a new trader and initialize a new account with 0 ammount.
     * - validate user input(all fields must be non empty)
     * - create a trader
     * - create an account
     * - create, setup, and return a new traderAccountView
     *
     * Assumption: to simplify the logic, each trader has only one account where traderId == accountId
     *
     * @param trader cannot be null. All fields cannot be null except for id (auto-generated by db)
     * @return traderAccountView
     * @throws IllegalArgumentException if a trader has null fields or id is not null.
     */
    public TraderAccountView createTraderAndAccount(Trader trader) {

        Trader newTrader = new Trader();

        // validate trader fields
        if(trader.getCountry() == null || trader.getDob() == null || trader.getEmail() == null
        || trader.getFirstName() == null || trader.getLastName() == null){
            throw new IllegalArgumentException("Trader field(s) are empty");
        }

        // validate trader id
        if(trader.getId() != null){
            throw new IllegalArgumentException("Trader id is empty");
        }

        // create the trader
        traderDao.save(trader);

        // create an account for the trader
        Account account = new Account();
        account.setTraderId(trader.getId());
        account.setAmount(0.0); // default value

        // create the account
        accountDao.save(account);

        // trader account view setup
        TraderAccountView traderAccountView = new TraderAccountView(account, trader);
        return traderAccountView;
    }

    /**
     * A trader can be deleted iff it has no open position and 0 cash balance
     * - validate traderID
     * - get trader account by traderId and check account balance
     * - get positions by accountId and check positions
     * - delete all securityOrders, account, trader (in this order)
     *
     * @param traderId must not be null
     * @throws IllegalArgumentException if traderId is null or not found or unable to delete
     */
    public void deleteTraderById(Integer traderId){

        // validate traderId
        if(traderId == null){
            throw new IllegalArgumentException("Trader id is empty");
        }

        // TODO - check positions later when doing optional tickets

        Optional<Trader> trader;

        // get trader account by id
        try {
            trader = traderDao.findById(traderId);
        } catch (IncorrectResultSizeDataAccessException ex){
            throw new IllegalArgumentException("Trader not found");
        } finally {
            Optional<Account> account = accountDao.findById(traderId);
            if(account.isPresent()) {
                Double balance = account.get().getAmount();

                // delete the account iff it has a zero balance
                if(balance != 0.0){
                    throw new IllegalArgumentException("Account balance is non-zero");
                }

                // find all security orders that match ids with the trader id
                else{
                    // find all security orders in security_order table
                    List<SecurityOrder> securityOrders = securityOrderDao.findAll();

                    // then delete them from the security_order table,
                    // on the condition (traderId == securityOrder accountId)
                    for(SecurityOrder securityOrder : securityOrders){
                        if(traderId == securityOrder.getAccountId()){
                            securityOrderDao.deleteById(securityOrder.getId());
                        }
                    }
                }

            } else{
                throw new IllegalArgumentException("Account not found");
            }

            // then delete the account and the trader
            accountDao.deleteById(traderId);
            traderDao.deleteById(traderId);

        }
    }

    /**
     * Deposit a fund to an account by traderId
     * - validate user input
     * - account = accountDao.findByTraderId
     * - accountDao.updateAmountById
     *
     * @param traderId must not be null
     * @param fund must be greater than 0
     * @return updatedAccount
     * @throws IllegalArgumentException if traderId is null or not found,
     *                                  and fund is less than or equal to 0
     */
    public Account deposit(Integer traderId, Double fund){

        // validate traderId
        if(traderId == null){
            throw new IllegalArgumentException("Trader id is empty");
        }

        // validate deposit amount
        if(fund <= 0.0){
            throw new IllegalArgumentException("Deposit amount cannot be zero or negative");
        }

        try {
            accountDao.findById(traderId);
        } catch (IncorrectResultSizeDataAccessException ex){
            throw new IllegalArgumentException("Account not found");
        } finally {

            Optional<Account> account = accountDao.findById(traderId);

            Double currentAmount = account.get().getAmount();
            account.get().setAmount(currentAmount + fund);
            accountDao.updateOne(account.get());
            return account.get();
        }
    }

    /**
     * Withdraw a fund to an account by traderId
     *
     * - validate user input
     * - account = accountDao.findByTraderId
     * - accountDao.updateAmountById
     *
     * @param traderId trader ID
     * @param fund can't be 0
     * @return updated amount
     * @throws IllegalArgumentException if traderId is null or not found, fund is less than or equal to 0,
     *                                  and insufficient funds
     */
    public Account withdraw(Integer traderId, Double fund) {

        // validate traderId
        if(traderId == null){
            throw new IllegalArgumentException("Trader id is empty");
        }

        // validate withdrawal amount
        if(fund <= 0.0){
            throw new IllegalArgumentException("Withdrawal amount cannot be zero or negative");
        }

        try {
             accountDao.findById(traderId);
        } catch (IncorrectResultSizeDataAccessException ex){
            throw new IllegalArgumentException("Account not found");
        } finally {

            Optional<Account> account = accountDao.findById(traderId);
            Double currentAmount = account.get().getAmount();

            // validate fund sufficiency, and update balance if validated
            if(currentAmount - fund < 0.0){
                throw new IllegalArgumentException("Insufficient funds");
            }
            else {
                account.get().setAmount(currentAmount - fund);
                accountDao.updateOne(account.get());
                return account.get();
            }
        }
    }
}