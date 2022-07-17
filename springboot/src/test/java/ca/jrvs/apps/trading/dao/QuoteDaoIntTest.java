package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Quote;
import com.google.common.collect.Iterables;
import com.sun.org.apache.xpath.internal.operations.Quo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
    public void insertOne(){
        savedQuote1.setAskPrice(10d);
        savedQuote1.setAskSize(10);
        savedQuote1.setBidPrice(10.2d);
        savedQuote1.setBidSize(10);
        savedQuote1.setId("aapl");
        savedQuote1.setLastPrice(10.1d);
        quoteDao.save(savedQuote1);

        savedQuote2.setAskPrice(10d);
        savedQuote2.setAskSize(10);
        savedQuote2.setBidPrice(10.2d);
        savedQuote2.setBidSize(10);
        savedQuote2.setId("goog");
        savedQuote2.setLastPrice(10.1d);
        quoteDao.save(savedQuote2);

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
    }

    @Test
    public void makeUpdateValues(){
        //quoteDao.makeUpdateValues(savedQuote);
        assertEquals(savedQuote1.getTicker(), "aapl");
    }

    @Test
    public void findAll(){
        Iterable<Quote> quoteList = quoteDao.findAll();
        for(Quote quote : quoteList){
            // System.out.println(quote.getTicker());
            // System.out.println(quote.getAskPrice());
            assertTrue(quote.getTicker() != null);
        }

        assertEquals(2, Iterables.size(quoteList));
    }

    @Test
    public void count(){
        long count = quoteDao.count();
        // System.out.println(count);
        assertEquals(2, count);
    }

    @Test
    public void findById(){
        Quote quote = quoteDao.findById(savedQuote1.getId()).get();
        assertEquals(savedQuote1.getId(), quote.getId());
    }

    @Test
    public void existsById(){
        String id = savedQuote1.getId();
        boolean exists = quoteDao.existsById(id);
        // System.out.println(ex);
        assertEquals(exists, true);
    }

    @Test
    public void saveAll(){

        List<Quote> quotes = new LinkedList<>();
        quotes.add(savedQuote3);
        quotes.add(savedQuote4);
        Iterable<Quote> iterableQuotes = quoteDao.saveAll(quotes);
        for(Quote quote : iterableQuotes){
            assertTrue(quote.getId() != null);
            // System.out.println(quote.getId());
        }

        assertEquals(2, Iterables.size(iterableQuotes));
    }

    @Test
    public void deleteAll(){
        quoteDao.deleteAll();
        assertEquals(0, quoteDao.count());
    }

    @After
    public void deleteOne(){
        quoteDao.deleteById(savedQuote1.getId());
        quoteDao.deleteById(savedQuote2.getId());
        quoteDao.deleteById(savedQuote3.getId());
        quoteDao.deleteById(savedQuote4.getId());
        //quoteDao.deleteAll();
    }
}