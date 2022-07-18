package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.MarketDataDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import ca.jrvs.apps.trading.model.domain.Quote;
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
     */
    public void updateMarketData(){

        Iterable<Quote> quotes = findAllQuotes();

        // TODO - make this more efficient with lambdas

        for(Quote quote: quotes) {
            IexQuote iexQuote = findIexQuoteByTicker(quote.getTicker());
            //System.out.println("IEX: " + iq.toString());
            Quote updatedQuote = buildQuoteFromIexQuote(iexQuote);
            quoteDao.deleteById(quote.getTicker()); // delete previous entry
            quoteDao.save(updatedQuote);            // save updated entry
        }
    }

    /**
     * Helper method to map an IexQuote to a Quote entity
     * @param iexQuote
     * @return
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

        /*System.out.println(quote.getTicker());
        System.out.println(quote.getLastPrice());
        System.out.println(quote.getBidPrice());
        System.out.println(quote.getBidSize());
        System.out.println(quote.getAskPrice());
        System.out.println(quote.getAskSize());*/

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
            quote = buildQuoteFromIexQuote(findIexQuoteByTicker(ticker));
            quoteDao.save(quote);   // save updated entry
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
     * Find all quotes form the quote table
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
}
