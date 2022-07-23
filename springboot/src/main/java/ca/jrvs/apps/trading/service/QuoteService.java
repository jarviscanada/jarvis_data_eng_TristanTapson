package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.MarketDataDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import ca.jrvs.apps.trading.model.domain.Quote;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;


@Transactional
@Service
public class QuoteService {

    private static final Logger logger = LoggerFactory.getLogger(QuoteService.class);

    private QuoteDao quoteDao;
    private MarketDataDao marketDataDao;

    public static void main(String[] args) {

    }

    // TODO - clean up print/logger later...

    @Autowired
    public QuoteService(QuoteDao quoteDao, MarketDataDao marketDataDao){
        this.quoteDao = quoteDao;
        this.marketDataDao = marketDataDao;
    }

    /**
     * Update quote table against IEX source
     * - get all quotes from the database
     * - for each ticker get iexQuote
     * - convert iexQuote to quote entity
     * - persist quote to database
     * @return the list of updated quotes against IEX source
     */
    public List<Quote> updateMarketData(){

        Iterable<Quote> quotes = findAllQuotes();
        List<Quote> updatedQuotes = new LinkedList<>();

        // TODO - make this more efficient with lambdas

        for(Quote quote: quotes) {

            IexQuote iexQuote = findIexQuoteByTicker(quote.getTicker());
            Quote updatedQuote = buildQuoteFromIexQuote(iexQuote);

            deleteQuoteById(quote);      // delete previous entry from quote table
            saveQuote(updatedQuote);     // save updated entry to quote table
            updatedQuotes.add(updatedQuote);

            System.out.println();
            System.out.println(updatedQuote.getTicker() + ": updated market data...");
            System.out.println("ticker: " + updatedQuote.getTicker());
            System.out.println("lastprice: " + updatedQuote.getLastPrice());
            System.out.println("bidprice: " + updatedQuote.getBidPrice());
            System.out.println("bidsize: " + updatedQuote.getBidSize());
            System.out.println("askprice: " + updatedQuote.getAskPrice());
            System.out.println("asksize: " + updatedQuote.getAskSize());
            System.out.println("id: " + updatedQuote.getId());
            System.out.println();
        }

        return updatedQuotes;
    }

    /**
     * Helper method to map an IexQuote to a Quote entity
     * @param iexQuote
     * @return quote entity from iexQuote
     */
    protected static Quote buildQuoteFromIexQuote(IexQuote iexQuote){

        if(iexQuote == null){
            throw new DataRetrievalFailureException("IexQuote not found");
        }

        if(!(iexQuote instanceof IexQuote)){
            throw new IllegalArgumentException("IexQuote not found");
        }

        Quote quote = new Quote();

        // ticker symbols
        quote.setId(iexQuote.getSymbol());
        quote.setTicker(iexQuote.getSymbol());

        // fields set with default values
        quote.setLastPrice(0.0);
        quote.setBidPrice(0.0);
        quote.setBidSize(0);
        quote.setAskPrice(0.0);
        quote.setAskSize(0);

        // TODO - maybe restructure this section later? also may need to consider case sensitivity with tickers...

        // update quote against IEXQuote
        if(iexQuote.getLatestPrice() != null){
            quote.setLastPrice(iexQuote.getLatestPrice());
        }
        if(iexQuote.getIexBidPrice() != null){
            quote.setBidPrice((double)iexQuote.getIexBidPrice());
        }
        if(iexQuote.getIexBidSize() != null){
            quote.setBidSize(iexQuote.getIexBidSize());
        }
        if(iexQuote.getIexAskPrice() != null){
            quote.setAskPrice((double)iexQuote.getIexAskPrice());
        }
        if(iexQuote.getIexAskSize() != null){
            quote.setAskSize(iexQuote.getIexAskSize());
        }

        return quote;
    }

    /**
     * Validate (against IEX) and save given tickers to quote table
     *
     * - get iexQuote(s)
     * - convert each iexQuote to Quote entity
     * - persist the quote to the database
     *
     * @param tickers a list of tickers/symbols
     * @throws IllegalArgumentException if ticker is not found from IEX (via findIexQuoteByTicker)
     * @return the list of saved quotes
     */
    public List<Quote> saveQuotes(List<String> tickers){

        List<Quote> quotes = new LinkedList<>();
        Quote quote = new Quote();

        for(String ticker : tickers){
            try {
                quote = buildQuoteFromIexQuote(findIexQuoteByTicker(ticker));
                quoteDao.save(quote);   // save updated entry
            } catch(DataRetrievalFailureException ex){
                throw new IllegalArgumentException("Ticker not found", ex);
            }
        }

        return quotes;
    }

    /**
     * Update a given quote to quote table without validation
     * @param quote entity
     */
    public Quote saveQuote(Quote quote){
        return quoteDao.save(quote);
    }

    /**
     * Delete a quote from the quote table by id
     * @param quote to be deleted
     */
    public void deleteQuoteById(Quote quote){
        quoteDao.deleteById(quote.getTicker());
    }

    /**
     * Find all quotes from the quote table
     * @return a list of quotes
     */
    public Iterable<Quote> findAllQuotes(){
        return quoteDao.findAll();
    }

    /**
     * Find an IexQuote
     *
     * @param ticker id
     * @return IexQuote object
     * @throws IllegalArgumentException if ticker is invalid
     */
    public IexQuote findIexQuoteByTicker(String ticker){
        return marketDataDao.findById(ticker)
                .orElseThrow(() -> new IllegalArgumentException(ticker +  "is invalid"));
    }

    // controller helper function
    public Quote saveQuoteByTickerId(String tickerId) {
        Quote quote = buildQuoteFromIexQuote(findIexQuoteByTicker(tickerId));
        saveQuote(quote);
        return quote;
    }
}
