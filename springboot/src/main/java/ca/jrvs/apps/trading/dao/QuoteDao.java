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
        Object[] quoteObj = {quote.getTicker(), quote.getLastPrice(),
            quote.getBidPrice(), quote.getBidSize(), quote.getAskPrice(),
            quote.getAskSize()};
        return quoteObj;
    }

    @Override
    public <S extends Quote> Iterable<S> saveAll(Iterable<S> iterable) {

        List<Quote> quotes = new LinkedList<>();
        for(Quote quote : iterable){
            save(quote);
            quotes.add(quote);
        }

        return (List<S>) quotes;
    }

    @Override
    public Optional<Quote> findById(String s) {

        if(s == null){
            throw new IllegalArgumentException("Ticker can't be null");
        }

        Quote quote = null;
        String select_sql = "SELECT * FROM " + TABLE_NAME + " WHERE ticker=?";

        try{
            quote = jdbcTemplate.queryForObject(select_sql,  BeanPropertyRowMapper.newInstance(Quote.class), s);
        } catch(EmptyResultDataAccessException ex){
            logger.debug("Can't find trader id: " + s, ex);
        }

        if(quote == null) {
            return Optional.empty();
        } else{
            return Optional.of(quote);
        }
    }

    @Override
    public boolean existsById(String s) {

        String count_sql = "SELECT COUNT(*) FROM " + TABLE_NAME + " where ticker=?";
        int count = jdbcTemplate.queryForObject(count_sql, Integer.class, s);
        boolean exists = count > 0;
        //System.out.println(exists);
        return exists;
    }

    @Override
    public Iterable<Quote> findAll() {
        //find multiple rows (findByColumnName)
        String select_sql = "SELECT * FROM " + TABLE_NAME;
        List<Quote> quotes =  jdbcTemplate
                .query(select_sql, BeanPropertyRowMapper.newInstance(Quote.class));

        return quotes;
    }

    @Override
    public Iterable<Quote> findAllById(Iterable<String> iterable) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public long count() {
        String count_sql = "SELECT COUNT(*) FROM " + TABLE_NAME;
        long count = jdbcTemplate.queryForObject(count_sql, Integer.class);
        return count;
    }

    @Override
    public void deleteById(String s) {
        if(s == null){
            throw new IllegalArgumentException("Ticker can't be null");
        }
        String delete_sql = "DELETE FROM " + TABLE_NAME + " WHERE ticker=?";
        jdbcTemplate.update(delete_sql, s);
    }

    @Override
    public void delete(Quote quote) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void deleteAll(Iterable<? extends Quote> iterable) {
        throw new UnsupportedOperationException("Not implemeneted");
    }

    @Override
    public void deleteAll() {
        String delete_sql = "DELETE FROM " + TABLE_NAME;
        jdbcTemplate.update(delete_sql);
    }
}
