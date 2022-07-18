package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import ca.jrvs.apps.trading.model.domain.Quote;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
@Sql({"classpath:schema.sql"})
public class QuoteServiceIntTest {

    @Autowired
    private QuoteService quoteService;

    @Autowired
    private QuoteDao quoteDao;

    private Quote savedQuote1 = new Quote();
    private Quote savedQuote2 = new Quote();

    @Before
    public void setUp() throws Exception {

        savedQuote1.setAskPrice(10d);
        savedQuote1.setAskSize(10);
        savedQuote1.setBidPrice(10.2d);
        savedQuote1.setBidSize(10);
        savedQuote1.setId("aapl");
        savedQuote1.setLastPrice(10.1d);

        savedQuote2.setAskPrice(10d);
        savedQuote2.setAskSize(10);
        savedQuote2.setBidPrice(10.2d);
        savedQuote2.setBidSize(10);
        savedQuote2.setId("goog");
        savedQuote2.setLastPrice(10.1d);

    }

    @Test
    public void updateMarketData(){

        long before = quoteDao.count();
        quoteService.saveQuote(savedQuote1);
        quoteService.updateMarketData();
        //assertEquals(before + 1, quoteDao.count());

    }

    @Test
    public void saveQuotes(){

        List<String> goodTickers = new LinkedList<>();
        List<String> badTickers = new LinkedList<>();

        goodTickers.add("MSFT");
        goodTickers.add("AMZN");

        // happy path
        quoteService.saveQuotes(goodTickers);
        assertEquals(2, quoteDao.count());

        // TODO - sad path
    }


    @Test
    public void saveQuote() {

        long before = quoteDao.count();
        quoteService.saveQuote(savedQuote2);
        assertEquals(before + 1, quoteDao.count());
    }

    @Test
    public void findIexQuoteByTicker(){

        String goodTicker = "FB";
        String badTicker = "FB2";

        // happy path
        IexQuote iexQuote = quoteService.findIexQuoteByTicker(goodTicker);
        assertEquals(goodTicker, iexQuote.getSymbol());

        // sad path
        try {
            quoteService.findIexQuoteByTicker(badTicker);
        } catch (DataRetrievalFailureException e){
            assertTrue(true);
        } catch (Exception e){
            fail();
        }
    }

    @After
    public void tearDown() throws Exception {
    }
}