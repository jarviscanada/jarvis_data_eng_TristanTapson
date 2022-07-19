package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import ca.jrvs.apps.trading.model.domain.Quote;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    private Quote savedQuote3 = new Quote();

    @Before
    public void setUp(){

        // pre-clean quote table
        quoteDao.deleteAll();

        // sample quotes
        savedQuote1.setAskPrice(10d);
        savedQuote1.setAskSize(10);
        savedQuote1.setBidPrice(10.2d);
        savedQuote1.setBidSize(10);
        savedQuote1.setId("AAPL");
        savedQuote1.setLastPrice(10.1d);

        savedQuote2.setAskPrice(10d);
        savedQuote2.setAskSize(10);
        savedQuote2.setBidPrice(10.2d);
        savedQuote2.setBidSize(10);
        savedQuote2.setId("GOOG");
        savedQuote2.setLastPrice(10.1d);

        savedQuote3.setAskPrice(10d);
        savedQuote3.setAskSize(10);
        savedQuote3.setBidPrice(10.2d);
        savedQuote3.setBidSize(10);
        savedQuote3.setId("MSFT");
        savedQuote3.setLastPrice(10.1d);

    }

    @Test
    public void updateMarketData() throws JsonProcessingException {

        List<Quote> quotes = new LinkedList<>();

        long before = quoteDao.count();
        quoteService.saveQuote(savedQuote1);
        quoteService.saveQuote(savedQuote2);
        quotes.add(savedQuote1);
        quotes.add(savedQuote2);

        // check quote against the market, and quote table size check
        List<Quote> market = quoteService.updateMarketData();
        for(int i = 0; i < quotes.size(); i++){
            assertEquals(quotes.get(i).getTicker(), market.get(i).getTicker());
        }

        assertEquals(before + 2, quoteDao.count());
    }

    @Test
    public void saveQuotes() throws JsonProcessingException {

        List<String> goodTickers = new LinkedList<>();
        List<String> badTickers = new LinkedList<>();

        goodTickers.add("AAPL");
        goodTickers.add("GOOG");
        goodTickers.add("MSFT");

        badTickers.add("AAPL2");
        badTickers.add("GOOG2");
        badTickers.add("MSFT2");

        // happy path - valid ticker string(s) in list
        List<Quote> savedQuotes = quoteService.saveQuotes(goodTickers);
        quoteService.updateMarketData();
        for(int i = 0; i < savedQuotes.size(); i++){
            assertEquals(goodTickers.get(i), savedQuotes.get(i).getTicker());
        }
        assertEquals(3, quoteDao.count());

        // sad path - invalid ticker strings(s) in list
        try{
            quoteService.saveQuotes(badTickers);
        } catch (IllegalArgumentException ex){
            assertTrue(true);
        }
    }

    @Test
    public void saveQuote() {

        // quote table size check
        long before = quoteDao.count();
        quoteService.saveQuote(savedQuote2);
        assertEquals(before + 1, quoteDao.count());
    }

    @Test
    public void findIexQuoteByTicker(){

        String goodTicker = "FB";
        String badTicker = "FB2";

        // happy path - valid ticker
        IexQuote iexQuote = quoteService.findIexQuoteByTicker(goodTicker);
        assertEquals(goodTicker, iexQuote.getSymbol());

        // sad path - invalid ticker
        try {
            quoteService.findIexQuoteByTicker(badTicker);
        } catch (DataRetrievalFailureException e) {
            assertTrue(true);
        }
    }

    @Test
    public void findAllQuotes(){

        // quote table size check
        long before = quoteDao.count();
        quoteService.saveQuote(savedQuote1);
        quoteService.saveQuote(savedQuote3);
        List<Quote> quotes = (List<Quote>) quoteService.findAllQuotes();
        assertEquals(before + 2, quotes.size());
    }

    @After
    public void tearDown(){
        // quoteDao.deleteAll();
    }
}