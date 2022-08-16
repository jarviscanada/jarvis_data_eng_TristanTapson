package ca.jrvs.apps.trading.service;


import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.TraderDao;
import ca.jrvs.apps.trading.model.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
public class DashboardService {

    private TraderDao traderDao;
    private PositionDao positionDao;
    private AccountDao accountDao;
    private QuoteDao quoteDao;

    @Autowired
    public DashboardService(TraderDao traderDao, PositionDao positionDao,
                            AccountDao accountDao, QuoteDao quoteDao){
        this.traderDao = traderDao;
        this.positionDao = positionDao;
        this.accountDao = accountDao;
        this.quoteDao = quoteDao;
    }

    /**
     * Create and return a traderAccountView by trader ID
     * - get trader account by id
     * - get trader info by id
     * - create and return a traderAccountView
     *
     * @param traderId must not be null
     * @return traderAccountView
     * @throws IllegalArgumentException if traderId is null or not found
     */
    public TraderAccountView getTraderAccount(Integer traderId){

        // validate id
        if(traderId == null){
            throw new IllegalArgumentException("Trader id is empty");
        }

        Trader trader = new Trader();

        // find trader
        try{
            trader = traderDao.findById(traderId).get();
        } catch (IncorrectResultSizeDataAccessException ex){
            throw new IllegalArgumentException("Trader not found", ex);
        }

        // find account from trader and create a view
        Account account = findAccountByTraderId(traderId);
        TraderAccountView traderAccountView = new TraderAccountView(account, trader);
        return traderAccountView;
    }

    /**
     * Create and return portfolioView by trader ID
     * - get account by trader id
     * - get positions by account id
     * - create and return a portfolio view
     * @param traderId must not be null
     * @return portfolioView
     * @throws IllegalArgumentException if traderId is null or not found
     */
    public PortfolioView getProfileViewByTraderId(Integer traderId){

        // validate id
        if(traderId == null){
            throw new IllegalArgumentException("Trader id is empty");
        }

        Trader trader = new Trader();

        // find trader
        try{
            trader = traderDao.findById(traderId).get();
        } catch (IncorrectResultSizeDataAccessException ex){
            throw new IllegalArgumentException("Trader not found", ex);
        }

        // find account and position from trader and create a portfolio
        Account account = findAccountByTraderId(traderId);
        // Position position = positionDao.findById(traderId).get();

        List<Position> positions = positionDao.findAll();
        List<SecurityRow> securityRows = new LinkedList<>();

        // create security rows from positions found that match trader ID
        for(Position position : positions){
            if(position.getAccountId() == traderId) {
                SecurityRow securityRow = new SecurityRow();

                String ticker = position.getTicker();
                Quote quote = quoteDao.findById(ticker).get();

                securityRow.setQuote(quote);
                securityRow.setPosition(position);
                securityRow.setTicker(ticker);
                securityRows.add(securityRow);
            }
        }

        /*for(SecurityRow row : securityRows){
            System.out.println(row.getPosition().toString());
            System.out.println(row.getTicker());
            System.out.println(row.getQuote());
        }*/

        // create portfolio view from security rows
        PortfolioView portfolioView = new PortfolioView();
        SecurityRow[] arr = new SecurityRow[securityRows.size()];
        portfolioView.setSecurityRows(securityRows.toArray(arr));

        return portfolioView;
    }

    /**
     * Finds and returns all traders from database (for frontend UI)
     * @return the list of traders
     */
    public List<Trader> getTraders(){
        return  traderDao.findAll();
    }

    /**
     * helper function to find account by trader ID
     * @param traderId must not be null
     * @throws IllegalArgumentException if traderId is not found
     */
    private Account findAccountByTraderId(Integer traderId){
        return accountDao.findById(traderId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid traderId"));
    }
}
