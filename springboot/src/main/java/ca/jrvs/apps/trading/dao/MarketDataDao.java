package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.example.JsonParser;
import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.internal.org.objectweb.asm.TypeReference;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;

@Repository
public class MarketDataDao implements CrudRepository<IexQuote, String> {

    // TODO: clean up print/logger for assets screenshots later...

    private static final String IEX_BATCH_PATCH = "stock/market/batch?symbols=%s&types=quote&token=";
    private final String IEX_BATCH_URL;
    private final int MAPPER_OFFSET = 14;
    private final int HTTP_OK = 200;

    private Logger logger = LoggerFactory.getLogger(MarketDataDao.class);
    private HttpClientConnectionManager httpClientConnectionManager;

    @Autowired
    public MarketDataDao(HttpClientConnectionManager httpClientConnectionManager,
                         MarketDataConfig marketDataConfig){
        this.httpClientConnectionManager = httpClientConnectionManager;
        IEX_BATCH_URL = marketDataConfig.getHost() + IEX_BATCH_PATCH + marketDataConfig.getToken();
    }

    /**
     * Get an IexQuote (helper method which class findAllById)
     *
     * @param ticker is a ticker
     * @throws IllegalArgumentException if a given ticker is invalid
     * @throws DataRetrievalFailureException if HTTP request failed
     * @return IexQuote object
     */
    @Override
    public Optional<IexQuote> findById(String ticker) throws IllegalArgumentException, DataRetrievalFailureException{

        Optional<IexQuote> iexQuote;
        List<IexQuote> quotes = findAllById(Collections.singletonList(ticker));

        for(IexQuote quote : quotes){
            // System.out.println(quote.toString());
        }

        if(quotes.size() == 0){
            return Optional.empty();
        }
        else if(quotes.size() == 1){
            iexQuote = Optional.of(quotes.get(0));
        }
        else{
            throw new DataRetrievalFailureException("Unexpected number of quotes");
        }

        return iexQuote;
    }

    /**
     *
     * @param tickers is a list of tickers
     * @return a list of IexQuote object
     * @throws IllegalArgumentException if any ticker is invalid or tickers is empty
     * @throws DataRetrievalFailureException if HTTP request failed
     */
    @Override
    public List<IexQuote> findAllById(Iterable<String> tickers) throws IllegalArgumentException, DataRetrievalFailureException {

        List<IexQuote> quoteList = new LinkedList<>();
        String tickerStr = String.join(",", tickers);
        String uri = String.format(IEX_BATCH_URL, tickerStr);

        System.out.println("Ticker(s): " + tickerStr);
        System.out.println("URI: " + uri);

        // HTTP response
        String response = executeHttpGet(uri)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid ticker"));

        // System.out.println("RESP: " + response.toString());

        // Array of JSON documents
        try {
            JSONObject IexQuotesJson = new JSONObject(response);
            System.out.println("JSON_Object: " +  IexQuotesJson.toString());
            // logger.info("JSON OBJ: " + IexQuotesJson.toString());

            // Get number of documents
            if (IexQuotesJson.length() == 0) {
                throw new IllegalArgumentException("Invalid ticker");
            }

            for(Object quoteValue : IexQuotesJson.toMap().values()) {
                // System.out.println(quoteValue.toString());
                try {

                    String json = new ObjectMapper()
                        .writerWithDefaultPrettyPrinter()
                        .writeValueAsString(quoteValue);

                    String jsonSubstring = json.substring(MAPPER_OFFSET, json.length()-1);
                    // System.out.println("VAL_AS_JSON: " + jsonSubstring); -- json
                    // logger.info("VAL_AS_JSON: " + jsonSubstring);
                    IexQuote quote = JsonParser.toObjectFromJson(jsonSubstring, IexQuote.class);
                    quoteList.add(quote);

                } catch (IOException ex) {
                    throw new RuntimeException("Unable to convert JSON str to IexQuote object", ex);
                }
            }

        } catch (JSONException ex){
            throw new IllegalArgumentException("Invalid ticker", ex);
        }

        return quoteList;
    }

    /**
     * Execute a get and return http entity/body as a string
     * Tip: use EntityUtils.toString to process HTTP entity
     *
     * @param url resource URL
     * @return http response body or Optional.empty for 404 response
     * @throws DataRetrievalFailureException if HTTP failed or status code is unexpected
     */
    private Optional<String> executeHttpGet(String url) throws DataRetrievalFailureException {

        String responseStr;
        int status;

        HttpGet getRequest = new HttpGet(url);
        try {
            CloseableHttpResponse response = getHttpClient().execute(getRequest);
            status = response.getStatusLine().getStatusCode();
            // System.out.println("STATUS_CODE: " + status);

            if(response.getEntity() == null){
                // throw new RuntimeException("Empty response");
                return Optional.empty();
            }
            if(status != HTTP_OK){
                throw new DataRetrievalFailureException("Unexpected HTTP status: " + status);
            }

            responseStr = EntityUtils.toString(response.getEntity());
            // System.out.println("GET: " + responseStr);
        } catch(IOException ex){
            throw new DataRetrievalFailureException("Failed to execute GET request", ex);
        }

        Optional<String> optionalStr = Optional.of(responseStr);
        return optionalStr;
    }

    /**
     * Borrow an HTTP client from the httpClientConnectionManager
     * @return an httpClient
     */
    private CloseableHttpClient getHttpClient(){
        return HttpClients.custom()
                .setConnectionManager(httpClientConnectionManager)
                .setConnectionManagerShared(true)
                .build();
    }

    @Override
    public <S extends IexQuote> S save(S s) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public <S extends IexQuote> Iterable<S> saveAll(Iterable<S> iterable) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean existsById(String s) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Iterable<IexQuote> findAll() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public long count() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void deleteById(String s) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void delete(IexQuote iexQuote) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void deleteAll(Iterable<? extends IexQuote> iterable) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException("Not implemented");
    }
}
