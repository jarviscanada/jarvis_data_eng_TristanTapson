package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Quote;
import com.google.common.collect.Iterables;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
@Sql({"classpath:schema.sql"})
public class QuoteDaoIntTest {

    @Autowired
    private QuoteDao quoteDao;

    private Quote savedQuote1 = new Quote();
    private Quote savedQuote2 = new Quote();
    private Quote savedQuote3 = new Quote();
    private Quote savedQuote4 = new Quote();

    // TODO - clean up print/logger before starting quoteService

    @Before
    public void setUp(){

        // pre-clean quote table
        quoteDao.deleteAll();

        // sample quotes
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

        savedQuote3.setAskPrice(10d);
        savedQuote3.setAskSize(10);
        savedQuote3.setBidPrice(10.2d);
        savedQuote3.setBidSize(10);
        savedQuote3.setId("amzn");
        savedQuote3.setLastPrice(10.1d);

        savedQuote4.setAskPrice(10d);
        savedQuote4.setAskSize(10);
        savedQuote4.setBidPrice(10.2d);
        savedQuote4.setBidSize(10);
        savedQuote4.setId("msft");
        savedQuote4.setLastPrice(10.1d);

        // initially add savedQuote1 and savedQuote2 to the quote table
        quoteDao.save(savedQuote1);
        quoteDao.save(savedQuote2);
    }

    @Test
    public void findAll(){

        // tickers not null check
        Iterable<Quote> quoteList = quoteDao.findAll();
        for(Quote quote : quoteList){
            assertTrue(quote.getTicker() != null);

        }

        // first ticker equal to first saved quote ticker, and quote table size check
        assertEquals(quoteList.iterator().next().getTicker(), savedQuote1.getTicker());
        assertEquals(2, Iterables.size(quoteList));
    }

    @Test
    public void count(){

        // quote table size check
        long count = quoteDao.count();
        assertEquals(2, count);
    }

    @Test
    public void findById(){

        // happy path - ticker found in quote table
        Quote quote = quoteDao.findById(savedQuote1.getId()).get();
        assertEquals(savedQuote1.getId(), quote.getId());

        // sad path - ticker not found in quote table
        Quote sadQuote = savedQuote1;
        sadQuote.setId("xyz");
        try{
            quoteDao.findById(sadQuote.getId());
        } catch (EmptyResultDataAccessException ex){
            assertTrue(true);
        }
    }

    @Test
    public void existsById(){

        // ticker exists by id
        String id = savedQuote1.getId();
        boolean exists = quoteDao.existsById(id);
        assertEquals(exists, true);
    }

    @Test
    public void saveAll(){

        // save a list of quotes to the quote table, and quote table size check
        List<Quote> quotes = new LinkedList<>();
        long beforeCount = quoteDao.count();
        quotes.add(savedQuote3);
        quotes.add(savedQuote4);

        Iterable<Quote> iterableQuotes = quoteDao.saveAll(quotes);
        assertEquals(beforeCount + 2, beforeCount + Iterables.size(iterableQuotes));
    }

    @Test
    public void deleteAll(){

        // delete all entries in the quote table, and quote table size check
        quoteDao.deleteAll();
        assertEquals(0, quoteDao.count());
    }

    @After
    public void tearDown(){
        quoteDao.deleteAll();
    }
}