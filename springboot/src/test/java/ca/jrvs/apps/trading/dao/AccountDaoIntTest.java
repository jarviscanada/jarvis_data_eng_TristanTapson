package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Trader;
import com.google.common.collect.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.Assert.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
@Sql({"classpath:schema.sql"})
public class AccountDaoIntTest {

    @Autowired
    private AccountDao accountDao;
    @Autowired
    private TraderDao traderDao;

    private Account savedAccount1 = new Account();
    private Account savedAccount2 = new Account();
    private Trader savedTrader1 = new Trader();
    private Trader savedTrader2 = new Trader();

    @Before
    public void setUp(){

        LocalDate date = LocalDate.now();

        // traders
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

        // traders accounts
        savedAccount1.setTraderId(savedTrader1.getId());
        savedAccount1.setAmount(500.00);
        accountDao.save(savedAccount1);

        savedAccount2.setTraderId(savedTrader2.getId());
        savedAccount2.setAmount(1000.00);
        accountDao.save(savedAccount2);
    }

    // TODO - fix date formatting? also test happy/sad paths

    @Test
    public void findById(){

        // trader account found in account table
        Account account = accountDao.findById(savedAccount1.getId()).get();
        assertEquals(savedAccount1.getId(), account.getId());

    }

    @Test
    public void findAllById(){

        // find all accounts in account table
        List<Account> accounts = Lists
                .newArrayList(accountDao.findAllById(Arrays.asList(savedAccount1.getId(), -1)));

        assertEquals(1, accounts.size());
        double val = accounts.get(0).getAmount();
        assertEquals(savedAccount1.getAmount(), accounts.get(0).getAmount());

    }

    @Test
    public void findAll(){

        // finds all accounts in account table and store them in a list
        List<Account> accounts = accountDao.findAll();
        assertEquals(2, accounts.size());
        assertEquals(savedAccount1.getAmount(), accounts.get(0).getAmount());

        // account id not null check
        for(Account account : accounts){
            assertTrue(account.getId() != null);
        }

    }

    @Test
    public void existsById(){

        // account exists by id
        Integer id = savedAccount1.getId();
        boolean exists = accountDao.existsById(id);
        assertEquals(exists, true);

    }

    @Test
    public void deleteAll(){

        // delete all entries in the account table, and account table size check
        accountDao.deleteAll();
        assertEquals(0, accountDao.count());

    }

    @Test
    public void deleteById(){

        // delete an entry in the account table by id
        long before = accountDao.count();
        Integer id = savedAccount2.getId();
        Account account = accountDao.findById(id).get();

        // correct account is deleted from table, and account table size check
        assertEquals(savedAccount2.getAmount(), account.getAmount());
        accountDao.deleteById(id);
        assertEquals(1, before-1);

    }

    @After
    public void tearDown(){
        // accountDao.deleteAll();
    }
}