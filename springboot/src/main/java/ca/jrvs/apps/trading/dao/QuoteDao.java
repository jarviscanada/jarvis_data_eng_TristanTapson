package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Quote;
import com.google.common.collect.Iterables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Repository
public class QuoteDao implements CrudRepository<Quote, String> {

    private static final String TABLE_NAME = "quote";
    private static final String ID_COLUMN_NAME = "ticker";

    private static final Logger logger = LoggerFactory.getLogger(QuoteDao.class);
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public QuoteDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(TABLE_NAME);
    }

    /**
     * saves a quote
     * @param quote to be saved
     * @return saved quote
     * @throws DataRetrievalFailureException for unexpected SQL result or SQL execution failure
     */
    @Override
    public Quote save(Quote quote){
        if(existsById(quote.getTicker())){
            int updatedRowNo = updateOne(quote);
            if(updatedRowNo != 1){
                throw new DataRetrievalFailureException("Unable to update quote");
            }
        } else{
            addOne(quote);
        }
        return quote;
    }

    /**
     * helper method that saves one quote
     */
    private void addOne(Quote quote){
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(quote);
        int row = simpleJdbcInsert.execute(parameterSource);
        if(row != 1){
            throw new IncorrectResultSizeDataAccessException("Failed to insert", 1, row);
        }
    }

    /**
     * helper method that updates one quote
     */
    private int updateOne(Quote quote) {
        String update_sql = "UPDATE quote SET last_price=?, bid_price=?, "
                + "bid_size=?, ask_price=?, ask_size=? WHERE ticker=?";
        return jdbcTemplate.update(update_sql, makeUpdateValues(quote));
    }

    /**
     * helper method that makes sql update values objects
     * @param quote to be updated
     * @return UPDATE_SQL values
     */
    private Object[] makeUpdateValues(Quote quote){
        Object[] quoteObject = {quote.getTicker(), quote.getLastPrice(),
            quote.getBidPrice(), quote.getBidSize(), quote.getAskPrice(),
            quote.getAskSize()};
        return quoteObject;
    }

    // Saves a list of quote objects to the database table
    @Override
    public <S extends Quote> Iterable<S> saveAll(Iterable<S> iterable) {

        List<Quote> quotes = new LinkedList<>();
        for(Quote quote : iterable){
            save(quote);
            quotes.add(quote);
        }

        return (List<S>) quotes;
    }

    /**
     * return all quotes
     * @throws DataRetrievalFailureException if failed to query
     * @return the list of quotes
     */
    @Override
    public Iterable<Quote> findAll() {
        String select_sql = "SELECT * FROM " + TABLE_NAME;
        List<Quote> quotes;
        try {
            quotes = jdbcTemplate
                    .query(select_sql, BeanPropertyRowMapper.newInstance(Quote.class));
        } catch (EmptyResultDataAccessException ex){
            throw new DataRetrievalFailureException("Table is empty", ex);
        }

        return quotes;
    }

    /**
     * Find a quote by ticker
     * @param ticker name
     * @return quote or Optional.empty if not found
     */
    @Override
    public Optional<Quote> findById(String ticker) {

        if(ticker == null){
            throw new IllegalArgumentException("Ticker can't be null");
        }

        Quote quote = null;
        String select_sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID_COLUMN_NAME + "=?";

        try{
            quote = jdbcTemplate.queryForObject(select_sql,  BeanPropertyRowMapper.newInstance(Quote.class), ticker);
        } catch(EmptyResultDataAccessException ex){
            logger.debug("Can't find trader id: " + ticker, ex);
        }

        if(quote == null) {
            return Optional.empty();
        } else{
            return Optional.of(quote);
        }
    }

    // Checks to see if a ticker exists in the database table
    @Override
    public boolean existsById(String ticker) {

        String count_sql = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE " + ID_COLUMN_NAME + "=?";
        int count = jdbcTemplate.queryForObject(count_sql, Integer.class, ticker);
        boolean exists = count > 0;
        //System.out.println(exists);
        return exists;
    }

    // Counts the number of rows in the database table
    @Override
    public long count() {
        String count_sql = "SELECT COUNT(*) FROM " + TABLE_NAME;
        long count = jdbcTemplate.queryForObject(count_sql, Integer.class);
        return count;
    }

    // Delete a ticker in the database table
    @Override
    public void deleteById(String ticker) {
        if(ticker == null){
            throw new IllegalArgumentException("Ticker can't be null");
        }
        String delete_sql = "DELETE FROM " + TABLE_NAME + " WHERE " + ID_COLUMN_NAME + "=?";
        jdbcTemplate.update(delete_sql, ticker);
    }

    // Delete all entries in the database table
    @Override
    public void deleteAll() {
        String delete_sql = "DELETE FROM " + TABLE_NAME;
        jdbcTemplate.update(delete_sql);
    }

    @Override
    public Iterable<Quote> findAllById(Iterable<String> iterable) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void delete(Quote quote) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void deleteAll(Iterable<? extends Quote> iterable) {
        throw new UnsupportedOperationException("Not implemented");
    }
}