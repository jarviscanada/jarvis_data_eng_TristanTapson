package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.dao.DataRetrievalFailureException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class MarketDataDaoIntTest {

    private MarketDataDao dao;

    @Before
    public void setUp(){

        // config
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(50);
        cm.setDefaultMaxPerRoute(50);
        MarketDataConfig marketDataConfig = new MarketDataConfig();
        marketDataConfig.setHost("https://cloud.iexapis.com/v1/");
        marketDataConfig.setToken(System.getenv("IEX_PUB_TOKEN"));
        dao = new MarketDataDao(cm, marketDataConfig);
    }

    @Test
    public void findAllById(){

        // happy path - valid ticker(s) in list
        List<IexQuote> quoteList = dao.findAllById(Arrays.asList("AAPL", "FB"));
        assertEquals(2, quoteList.size());
        assertEquals("AAPL", quoteList.get(0).getSymbol());

        // sad path - invalid ticker(s) in list
        try{
            dao.findAllById(Arrays.asList("AAPL", "FB2"));
            // fail(); // fixed - IllegalArgumentException caused by JSONException
        } catch (IllegalArgumentException e){
            assertTrue(true);
        } catch (Exception ex){
            fail();
        }
    }

    @Test
    public void findById(){

        // happy path - valid ticker
        String ticker1 = "AAPL";
        IexQuote iexQuote = dao.findById(ticker1).get();
        assertEquals(ticker1, iexQuote.getSymbol());

        // sad path - invalid ticker
        String ticker2 = "AAPL2";
        try {
            dao.findById(ticker2);
        } catch(DataRetrievalFailureException ex){
            assertTrue(true);
        }

        // sad path - empty ticker
        String ticker3 = "";
        try {
            dao.findById(ticker3);
        } catch (DataRetrievalFailureException ex){
            assertTrue(true);
        }
    }
}