package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.dao.TraderDao;
import ca.jrvs.apps.trading.model.domain.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
@Sql({"classpath:schema.sql"})
public class TraderAccountServiceIntTest {

    private TraderAccountView savedView;
    @Autowired
    private TraderAccountService traderAccountService;
    @Autowired
    private TraderDao traderDao;
    @Autowired
    private QuoteDao quoteDao;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private SecurityOrderDao securityOrderDao;

    private Quote savedQuote1 = new Quote();
    private Trader savedTrader1 = new Trader();
    private Trader savedTrader2 = new Trader();

    private SecurityOrder securityOrder1 = new SecurityOrder();
    private SecurityOrder securityOrder2 = new SecurityOrder();

    @Before
    public void setUp(){

        Date date = new Date();

        // quotes
        savedQuote1.setAskPrice(10d);
        savedQuote1.setAskSize(10);
        savedQuote1.setBidPrice(10.2d);
        savedQuote1.setBidSize(10);
        savedQuote1.setId("aapl");
        savedQuote1.setLastPrice(10.1d);
        quoteDao.save(savedQuote1);

        // traders
        savedTrader1.setCountry("Canada");
        savedTrader1.setDob(date); // yyyy-dd-mm in swagger UI
        savedTrader1.setEmail("first@email.com");
        savedTrader1.setFirstName("First");
        savedTrader1.setLastName("Last");

        savedTrader2.setCountry("United States");
        savedTrader2.setDob(date); // yyyy-dd-mm in swagger UI
        savedTrader2.setEmail("another@gmail.com");
        savedTrader2.setFirstName("Another");
        savedTrader2.setLastName("Trader");

        // traders security orders
        securityOrder1.setAccountId(savedTrader1.getId());
        securityOrder1.setStatus("PENDING");
        securityOrder1.setTicker(savedQuote1.getTicker());
        securityOrder1.setSize(20);
        securityOrder1.setPrice(450.00);
        securityOrder1.setNotes("just a note, bro...");

        securityOrder2.setAccountId(savedTrader2.getId());
        securityOrder2.setStatus("CANCELLED");
        securityOrder2.setTicker(savedQuote1.getTicker());
        securityOrder2.setSize(15);
        securityOrder2.setPrice(790.50);
        securityOrder2.setNotes("notes are fun");

    }

    @Test
    public void createTraderAndAccount(){

        // happy path - valid trader
        TraderAccountView view = traderAccountService.createTraderAndAccount(savedTrader1);
        assertEquals(savedTrader1, view.getTrader());

        // sad path - invalid trader
        Trader badTrader = savedTrader1;
        badTrader.setFirstName(null);
        try{
            traderAccountService.createTraderAndAccount(badTrader);
        } catch (IllegalArgumentException ex){
            assertTrue(true);
        }

        SecurityOrder securityOrderView1 = securityOrder1;
        securityOrderView1.setAccountId(view.getTrader().getId());
        securityOrderDao.save(securityOrderView1);
    }

    @Test
    public void deleteTraderById(){

        // happy path - valid trader, account, securityOrders
        TraderAccountView view1 = traderAccountService.createTraderAndAccount(savedTrader1);
        TraderAccountView view2 = traderAccountService.createTraderAndAccount(savedTrader2);

        SecurityOrder securityOrderView1 = securityOrder1;
        securityOrderView1.setAccountId(view1.getTrader().getId());
        securityOrderView1.setNotes("this is persisted");
        securityOrderDao.save(securityOrderView1);

        // security order to be deleted
        SecurityOrder securityOrderView2 = securityOrder2;
        securityOrderView2.setAccountId(view2.getTrader().getId());
        securityOrderDao.save(securityOrderView2);

        long before = securityOrderDao.count();

        // view is deleted by the correct trader id (traderId == accountId), and security_order table size check
        assertEquals(securityOrderView2.getAccountId(), view2.getTrader().getId());
        traderAccountService.deleteTraderById(view2.getTrader().getId());
        assertEquals(before-1, securityOrderDao.count());

        // TODO - sad path
    }

    @After
    public void tearDown(){

    }
}