package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class JdbcCrudDao<T extends Entity<Integer>> implements CrudRepository<T, Integer>{

    private static final Logger logger = LoggerFactory.getLogger(JdbcCrudDao.class);

    abstract public JdbcTemplate getJdbcTemplate();
    abstract public SimpleJdbcInsert getSimpleJdbcInsert();
    abstract public String getTableName();
    abstract public String getIdColumnName();
    abstract Class<T> getEntityClass();

    /**
     * Save an entity and update auto-generated integer ID
     * @param entity to be saved
     * @return save entity
     */
    @Override
    public <S extends T> S save(S entity){
        if(existsById(entity.getId())){
            if(updateOne(entity) != 1){
                throw new DataRetrievalFailureException("Unable to update entity");
            }
        } else {
            addOne(entity);
        }
        return entity;
    }

    /**
     * helper method that saves one entity
     */
    private <S extends T> void addOne(S entity){
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(entity);

        Number newId = getSimpleJdbcInsert().executeAndReturnKey(parameterSource);
        entity.setId(newId.intValue());
    }

    /**
     * helper method that updates one entity
     */
    abstract public int updateOne(T entity);

    @Override
    public Optional<T> findById(Integer id){
        Optional<T> entity = Optional.empty();
        String select_sql = "SELECT * FROM " + getTableName() + " WHERE " + getIdColumnName() + "=?";

        try{
            entity = Optional.ofNullable(getJdbcTemplate()
                    .queryForObject(select_sql,
                            BeanPropertyRowMapper.newInstance(getEntityClass()), id));
        } catch (IncorrectResultSizeDataAccessException ex){
            logger.debug("Can't find entity id: " + id, ex);
        }
        return entity;
    }

    @Override
    public boolean existsById(Integer id){
        String count_sql = "SELECT COUNT(*) FROM " + getTableName()
                + " WHERE " + getIdColumnName() + "=?";
        int count = getJdbcTemplate().queryForObject(count_sql, Integer.class, id);
        boolean exists = count > 0;
        //System.out.println(exists);
        return exists;
    }

    @Override
    public List<T> findAll() {
        String select_sql = "SELECT * FROM " + getTableName();
        List<T> entities = null;
        try {
            entities = getJdbcTemplate()
                    .query(select_sql, BeanPropertyRowMapper.newInstance(getEntityClass()));
        } catch (IncorrectResultSizeDataAccessException ex){
            logger.debug("Can't find entity", ex);
        }

        return entities;
    }

    @Override
    public List<T> findAllById(Iterable<Integer> ids){

        List<T> idsList = new LinkedList<>();
        for(Integer id : ids){
            findById(id).ifPresent(idsList::add);
        }

        return idsList;
    }

    @Override
    public long count(){
        String count_sql = "SELECT COUNT(*) FROM " + getTableName();
        long count = getJdbcTemplate().queryForObject(count_sql, Integer.class);
        return count;
    }

    @Override
    public void deleteAll(){
        String delete_sql = "DELETE FROM " + getTableName();
        getJdbcTemplate().update(delete_sql);
    }

    @Override
    public void deleteById(Integer integer) {
        String delete_sql = "DELETE FROM " + getTableName() + " WHERE " + getIdColumnName() + "=?";
        getJdbcTemplate().update(delete_sql, integer);
    }
}

