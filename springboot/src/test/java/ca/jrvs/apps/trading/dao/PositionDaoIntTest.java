package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.*;
import ca.jrvs.apps.trading.service.QuoteService;
import com.google.common.collect.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.*;

import org.junit.Assert.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
@Sql({"classpath:schema.sql"})
public class PositionDaoIntTest {

    @Autowired
    private AccountDao accountDao;
    @Autowired
    private QuoteDao quoteDao;
    @Autowired
    private QuoteService quoteService;
    @Autowired
    private TraderDao traderDao;
    @Autowired
    private SecurityOrderDao securityOrderDao;
    @Autowired
    private PositionDao positionDao;

    private Account savedAccount1 = new Account();
    private Trader savedTrader1 = new Trader();
    private SecurityOrder securityOrder1 = new SecurityOrder();
    private Position savedPosition1 = new Position();
    private Quote savedQuote1 = new Quote();

    @Before
    public void setUp(){

        LocalDate date = LocalDate.now();

        // quotes
        savedQuote1.setAskPrice(10d);
        savedQuote1.setAskSize(10);
        savedQuote1.setBidPrice(10.2d);
        savedQuote1.setBidSize(10);
        savedQuote1.setId("AAPL");
        savedQuote1.setLastPrice(10.1d);
        quoteDao.save(savedQuote1);

        quoteService.updateMarketData();

        // traders
        savedTrader1.setCountry("Canada");
        savedTrader1.setDob(date); // yyyy-dd-mm in swagger UI
        savedTrader1.setEmail("first@email.com");
        savedTrader1.setFirstName("First");
        savedTrader1.setLastName("Last");
        traderDao.save(savedTrader1);

        // traders accounts
        savedAccount1.setTraderId(savedTrader1.getId());
        savedAccount1.setAmount(500.00);
        accountDao.save(savedAccount1);

        // traders security orders
        securityOrder1.setAccountId(savedTrader1.getId());
        securityOrder1.setStatus("FILLED");
        securityOrder1.setTicker(savedQuote1.getTicker());
        securityOrder1.setSize(1);
        securityOrder1.setPrice(450.00);
        securityOrder1.setNotes("just a note, bro...");
        securityOrderDao.save(securityOrder1);

        // traders positions
        /*savedPosition1.setAccountId(securityOrder1.getAccountId());
        savedPosition1.setPosition(1);
        savedPosition1.setTicker(securityOrder1.getTicker());*/
    }

    // TODO - more tests to meet code coverage

    @Test
    public void saves(){

        // position view is read only, create and update methods are disabled (via override)
        try{
            positionDao.save(savedPosition1);
        } catch (UnsupportedOperationException ex){
            assertTrue(true);
        }

        List<Position> positions = new LinkedList<>();
        positions.add(savedPosition1);


        try{
            positionDao.saveAll(positions);
        } catch (UnsupportedOperationException ex){
            assertTrue(true);
        }
    }

    @Test
    public void findAll() {
        List<Position> positionList = positionDao.findAll();
        assertEquals(1, positionList.size());
    }

    @Test
    public void updates(){

        // position view is read only, create and update methods are disabled (via override)
        try{
            positionDao.updateOne(savedPosition1);
        } catch (UnsupportedOperationException ex){
            assertTrue(true);
        }
    }

    @After
    public void tearDown(){
        //positionDao.deleteAll();
        /*securityOrderDao.deleteAll();
        accountDao.deleteAll();
        traderDao.deleteAll();
        quoteDao.deleteAll();*/
    }
}