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
public class OrderServiceIntTest {

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
    @Autowired
    private OrderService orderService;

    private Quote savedQuote1 = new Quote();
    private Trader savedTrader1 = new Trader();
    private Account savedAccount1 = new Account();

    private SecurityOrder securityOrder1 = new SecurityOrder();
    private MarketOrderDto marketOrderDto1 = new MarketOrderDto();
    private MarketOrderDto marketOrderDto2 = new MarketOrderDto();

    @Before
    public void setUp() throws Exception {

        Date date = new Date();

        // quotes
        savedQuote1.setAskPrice(10d);
        savedQuote1.setAskSize(10);
        savedQuote1.setBidPrice(10.2d);
        savedQuote1.setBidSize(10);
        savedQuote1.setId("AMZN");
        savedQuote1.setLastPrice(10.1d);
        quoteDao.save(savedQuote1);

        // traders
        savedTrader1.setCountry("Canada");
        savedTrader1.setDob(date); // yyyy-dd-mm in swagger UI
        savedTrader1.setEmail("first@email.com");
        savedTrader1.setFirstName("First");
        savedTrader1.setLastName("Last");
        traderDao.save(savedTrader1);

        // traders accounts
        savedAccount1.setTraderId(savedTrader1.getId());
        savedAccount1.setAmount(5000.00);
        accountDao.save(savedAccount1);

        // traders security orders
        securityOrder1.setAccountId(savedTrader1.getId());
        securityOrder1.setStatus("PENDING");
        securityOrder1.setTicker(savedQuote1.getTicker());
        // default values before order is placed...
        securityOrder1.setSize(80);
        securityOrder1.setPrice(500.00);
        securityOrder1.setNotes("we are testing...");

        // market orders
        marketOrderDto1.setTicker("AMZN");
        marketOrderDto1.setAccountId(savedAccount1.getId());
        marketOrderDto1.setSize(3);

        marketOrderDto2.setTicker("AMZN");
        marketOrderDto2.setAccountId(savedAccount1.getId());
        marketOrderDto2.setSize(-2);

    }

    @Test
    public void buyMarketOrder(){
        assertTrue(true);
        orderService.executeMarketOrder(marketOrderDto1);
    }

    @Test
    public void sellMarketOrder(){
        assertTrue(true);
        orderService.executeMarketOrder(marketOrderDto1);
        orderService.executeMarketOrder(marketOrderDto2);
    }

    @After
    public void tearDown() throws Exception {
    }
}