package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import ca.jrvs.apps.trading.model.domain.Trader;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.Assert.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
@Sql({"classpath:schema.sql"})
public class SecurityOrderDaoIntTest {

    @Autowired
    private AccountDao accountDao;
    @Autowired
    private TraderDao traderDao;
    @Autowired
    private SecurityOrderDao securityOrderDao;
    @Autowired
    private QuoteDao quoteDao;
    @Autowired
    private QuoteService quoteService;

    private Account savedAccount1 = new Account();
    private Account savedAccount2 = new Account();
    private Trader savedTrader1 = new Trader();
    private Trader savedTrader2 = new Trader();
    private SecurityOrder securityOrder1 = new SecurityOrder();
    private SecurityOrder securityOrder2 = new SecurityOrder();

    private Quote savedQuote1 = new Quote();
    private Quote savedQuote2 = new Quote();

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

        savedQuote2.setAskPrice(10d);
        savedQuote2.setAskSize(10);
        savedQuote2.setBidPrice(10.2d);
        savedQuote2.setBidSize(10);
        savedQuote2.setId("MSFT");
        savedQuote2.setLastPrice(10.1d);
        quoteDao.save(savedQuote2);

        quoteService.updateMarketData();

        // traders
        savedTrader1.setCountry("Canada");
        savedTrader1.setDob(date); // yyyy-dd-mm in swagger UI
        savedTrader1.setEmail("first@email.com");
        savedTrader1.setFirstName("First");
        savedTrader1.setLastName("Last");
        traderDao.save(savedTrader1);

        savedTrader2.setCountry("United States");
        savedTrader2.setDob(date); // yyyy-dd-mm in swagger UI
        savedTrader2.setEmail("another@gmail.com");
        savedTrader2.setFirstName("Another");
        savedTrader2.setLastName("Trader");
        traderDao.save(savedTrader2);

        // traders accounts
        savedAccount1.setTraderId(savedTrader1.getId());
        savedAccount1.setAmount(500.00);
        accountDao.save(savedAccount1);

        savedAccount2.setTraderId(savedTrader2.getId());
        savedAccount2.setAmount(1000.00);
        accountDao.save(savedAccount2);

        // traders security orders
        securityOrder1.setAccountId(savedTrader1.getId());
        securityOrder1.setStatus("FILLED");
        securityOrder1.setTicker(savedQuote1.getTicker());
        securityOrder1.setSize(savedQuote1.getAskSize());
        securityOrder1.setPrice(savedQuote1.getAskPrice());
        securityOrder1.setNotes("just a note, bro...");
        securityOrderDao.save(securityOrder1);

        securityOrder2.setAccountId(savedTrader1.getId());
        securityOrder2.setStatus("FILLED");
        securityOrder2.setTicker(savedQuote2.getTicker());
        securityOrder2.setSize(savedQuote2.getAskSize());
        securityOrder2.setPrice(savedQuote2.getAskPrice());
        securityOrder2.setNotes("notes are fun");
        securityOrderDao.save(securityOrder2);

    }

    // TODO - fix date formatting? also test happy/sad paths

    @Test
    public void findById(){

        // security order found in security order table
        SecurityOrder securityOrder = securityOrderDao.findById(securityOrder1.getId()).get();
        assertEquals(securityOrder1.getId(), securityOrder.getId());

    }

    @Test
    public void findAllById(){

        // find all security orders in security_order table
        List<SecurityOrder> securityOrders = Lists
                .newArrayList(securityOrderDao.findAllById(Arrays.asList(savedTrader1.getId(), -1)));

        assertEquals(1, securityOrders.size());
        assertEquals(securityOrder1.getTicker(), securityOrders.get(0).getTicker());
    }

    @Test
    public void findAll(){

        // finds all security orders in security_order table and store them in a list
        List<SecurityOrder> securityOrders = securityOrderDao.findAll();
        assertEquals(2, securityOrders.size());
        assertEquals(securityOrder1.getTicker(), securityOrders.get(0).getTicker());

        // security order id not null check
        for(SecurityOrder securityOrder : securityOrders){
            assertTrue(securityOrder.getId() != null);
        }
    }

    @Test
    public void existsById(){

        // security order exists by id
        Integer id = securityOrder1.getId();
        boolean exists = securityOrderDao.existsById(id);
        assertEquals(exists, true);
    }

    @Test
    public void deleteAll(){

        // delete all entries in the security_order table, and security_order table size check
        securityOrderDao.deleteAll();
        assertEquals(0, securityOrderDao.count());
    }

    @Test
    public void deleteById(){

        // delete an entry in the security_order table by id
        long before = securityOrderDao.count();
        Integer id = savedTrader2.getId();
        SecurityOrder securityOrder = securityOrderDao.findById(id).get();

        // correct security order is deleted from table, and security_order table size check
        assertEquals(securityOrder2.getNotes(), securityOrder.getNotes());
        securityOrderDao.deleteById(id);
        assertEquals(1, before-1);
    }

    @Test
    public void multiOrder(){

        SecurityOrder securityOrder3 = new SecurityOrder();
        Quote savedQuote3 = new Quote();

        savedQuote3.setAskPrice(10d);
        savedQuote3.setAskSize(10);
        savedQuote3.setBidPrice(10.2d);
        savedQuote3.setBidSize(10);
        savedQuote3.setId("JNJ");
        savedQuote3.setLastPrice(10.1d);
        quoteDao.save(savedQuote3);
        quoteService.updateMarketData();

        securityOrder3.setAccountId(savedTrader1.getId());
        securityOrder3.setStatus("FILLED");
        securityOrder3.setTicker(savedQuote3.getTicker());
        securityOrder3.setSize(savedQuote3.getAskSize());
        securityOrder3.setPrice(savedQuote3.getAskPrice());
        securityOrder3.setNotes("just a note, bro...");
        securityOrderDao.save(securityOrder3);

        assertEquals(3, securityOrderDao.count());
    }

    @After
    public void tearDown(){
        securityOrderDao.deleteAll();
        accountDao.deleteAll();
        traderDao.deleteAll();
        quoteDao.deleteAll();
    }
}