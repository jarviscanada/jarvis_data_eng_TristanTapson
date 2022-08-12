package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.model.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private AccountDao accountDao;
    private SecurityOrderDao securityOrderDao;
    private QuoteDao quoteDao;
    private PositionDao positionDao;

    @Autowired
    public OrderService(AccountDao accountDao, SecurityOrderDao securityOrderDao,
                        QuoteDao quoteDao, PositionDao positionDao){
        this.accountDao = accountDao;
        this.securityOrderDao = securityOrderDao;
        this.quoteDao = quoteDao;
        this.positionDao = positionDao;
    }

    /**
     * Execute a market order
     *
     * - validate the order (e.g. size, and ticker)
     * - Create a securityOrder (for security_order table)
     * - Handle buy or sell order
     *      - buy order: check account balance (calls helper method)
     *      - sell order: check position for the ticker/symbol (calls helper method)
     *      - (please don't forget to update securityOrder.status)
     * - Save and return securityOrder
     *
     * @param orderDto market order
     * @return securityOrder from security_order table
     * @throws DataAccessException if unable to get data from DAO
     * @throws IllegalArgumentException for invalid input
     */
    public SecurityOrder executeMarketOrder(MarketOrderDto orderDto){

        // validate order (ticker must be in dailyList and size must be non-zero)
        if(orderDto.getTicker() == null || quoteDao.findById(orderDto.getTicker()) == null){
            throw new IllegalArgumentException("Invalid ticker");
        }
        if(orderDto.getSize() == null || orderDto.getSize() == 0){
            throw new IllegalArgumentException("Invalid size");
        }

        Integer id = orderDto.getAccountId();

        // create security order
        SecurityOrder securityOrder = new SecurityOrder();
        securityOrder.setAccountId(id);
        securityOrder.setStatus("PENDING");
        securityOrder.setNotes("pending...");

        Account account = new Account();
        try {
            account = accountDao.findById(id).get();
        } catch (DataRetrievalFailureException ex){
            throw new DataAccessException("Account not found", ex) {};
        }

        // buy (balance check)
        if(orderDto.getSize() > 0){
            handleBuyMarketOrder(orderDto, securityOrder, account);
        }

        // sell (position check - size less than 0 to sell)
        if(orderDto.getSize() < 0){
            handleSellMarketOrder(orderDto, securityOrder, account);
        }

        //securityOrderDao.save(securityOrder);
        return securityOrder;
    }

    /**
     * Helper method that executes a buy order
     * @param marketOrderDto user order
     * @param securityOrder to be saved in database
     * @param account account
     */
    protected void handleBuyMarketOrder(MarketOrderDto marketOrderDto,
                                        SecurityOrder securityOrder, Account account){

        Integer id = marketOrderDto.getAccountId();

        Integer size = marketOrderDto.getSize();
        Quote quote = quoteDao.findById(marketOrderDto.getTicker()).get();
        Double price = quote.getLastPrice();
        Double orderCost = size * price;
        Double currentBalance = account.getAmount();

        // valid funds to purchase order
        if(currentBalance - orderCost > 0) {

            // update the amount in the account after order purchase
            account.setAmount(currentBalance - orderCost);
            accountDao.save(account);

            // update security order
            securityOrder.setTicker(marketOrderDto.getTicker());
            securityOrder.setPrice(price);
            securityOrder.setSize(size);
            securityOrder.setStatus("FILLED");
            securityOrder.setNotes("");
            securityOrderDao.save(securityOrder);

            // update position after successful order purchase
            Position position = positionDao.findById(id).get();

            if(position.getPosition() != null) {
                position.setPosition(position.getPosition() + size);
            }
            else{
                position.setPosition(size);
            }
        }

        // invalid funds to purchase order
        if(currentBalance - orderCost <= 0){
            securityOrder.setTicker(marketOrderDto.getTicker());
            securityOrder.setPrice(price);
            securityOrder.setSize(size);
            securityOrder.setStatus("CANCELLED");
            securityOrder.setNotes("Insufficient funds.");
            securityOrderDao.save(securityOrder);
        }
    }

    protected void handleSellMarketOrder(MarketOrderDto marketOrderDto,
                                         SecurityOrder securityOrder, Account account){

        Integer id = marketOrderDto.getAccountId();

        Integer size = marketOrderDto.getSize();
        Quote quote = quoteDao.findById(marketOrderDto.getTicker()).get();
        Double price = quote.getLastPrice();
        Double orderProfit = size * price;
        Double currentBalance = account.getAmount();

        // update position after successful order
        // TODO - list of positions via find all and compare to ID ?
        Position position = positionDao.findById(id).get();
        Integer currentPosition = position.getPosition();

        // valid position to sell order (negative size means sell)
        if(currentPosition + size > 0) {

            // update the amount in the account after order sell
            account.setAmount(currentBalance + (orderProfit * -1.0));
            accountDao.save(account);

            // update security order
            securityOrder.setTicker(marketOrderDto.getTicker());
            securityOrder.setPrice(price);
            securityOrder.setSize(size);
            securityOrder.setStatus("FILLED");
            securityOrder.setNotes("");
            securityOrderDao.save(securityOrder);

            // update position after successful order sale
            position.setPosition(currentPosition + size);
        }

        // invalid position to sell order
        if(currentPosition + size <= 0){
            securityOrder.setTicker(marketOrderDto.getTicker());
            securityOrder.setPrice(price);
            securityOrder.setSize(size);
            securityOrder.setStatus("CANCELLED");
            securityOrder.setNotes("Insufficient position.");
            securityOrderDao.save(securityOrder);
        }
    }
}
