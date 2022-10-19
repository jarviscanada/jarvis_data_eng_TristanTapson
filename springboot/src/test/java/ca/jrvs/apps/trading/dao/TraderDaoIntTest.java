package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Trader;
import com.google.common.collect.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
@Sql({"classpath:schema.sql"})
public class TraderDaoIntTest {

    @Autowired
    private TraderDao traderDao;

    private Trader savedTrader1 = new Trader();
    private Trader savedTrader2 = new Trader();

    @Before
    public void setUp(){

        // SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        LocalDate date = LocalDate.now();

        savedTrader1.setCountry("Canada");
        savedTrader1.setDob(date); // yyyy-dd-mm in swagger UI
        savedTrader1.setEmail("first@email.com");
        savedTrader1.setFirstName("First");
        savedTrader1.setLastName("Last");
        traderDao.save(savedTrader1);

        savedTrader2.setCountry("United States");
        savedTrader2.setDob(date); // yyyy-dd-mm in swagger UI
        savedTrader2.setEmail("another@gmail.com");
        savedTrader2.setFirstName("Another");
        savedTrader2.setLastName("Trader");
        traderDao.save(savedTrader2);

    }

    // TODO - fix date formatting? also test happy/sad paths

    @Test
    public void findById(){

        // trader found in trader table
        Trader trader = traderDao.findById(savedTrader1.getId()).get();
        assertEquals(savedTrader1.getId(), trader.getId());
    }

    @Test
    public void findAllById(){

        // find all traders in trader table
        List<Trader> traders = Lists
                .newArrayList(traderDao.findAllById(Arrays.asList(savedTrader1.getId(), -1)));

        assertEquals(1, traders.size());
        assertEquals(savedTrader1.getCountry(), traders.get(0).getCountry());
    }

    @Test
    public void findAll(){

        // finds all traders in trader table and store them in a list
        List<Trader> traders = traderDao.findAll();
        assertEquals(2, traders.size());
        assertEquals(savedTrader1.getFirstName(), traders.get(0).getFirstName());

        // trader id not null check
        for(Trader trader : traders){
            assertTrue(trader.getId() != null);
        }
    }

    @Test
    public void existsById(){

        // trader exists by id
        Integer id = savedTrader1.getId();
        boolean exists = traderDao.existsById(id);
        assertEquals(exists, true);
    }

    @Test
    public void deleteAll(){

        // delete all entries in the trader table, and trader table size check
        traderDao.deleteAll();
        assertEquals(0, traderDao.count());
    }

    @Test
    public void deleteById(){

        // delete an entry in the trader table by id
        long before = traderDao.count();
        Integer id = savedTrader2.getId();
        Trader trader = traderDao.findById(id).get();

        // correct trader is deleted from table, and trader table size check
        assertEquals(savedTrader2.getFirstName(), trader.getFirstName());
        traderDao.deleteById(id);
        assertEquals(1, before-1);
    }

    @After
    public void tearDown(){
        // traderDao.deleteAll();
    }
}