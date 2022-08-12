package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.*;
import ca.jrvs.apps.trading.model.domain.*;
import org.hibernate.criterion.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceUnitTest {

    // capture parameter when calling securityOrderDao.save
    @Captor
    ArgumentCaptor<SecurityOrder> captorSecurityOrder;

    // mock all dependencies
    @Mock
    private AccountDao accountDao;
    @Mock
    private SecurityOrderDao securityOrderDao;
    @Mock
    private QuoteDao quoteDao;
    @Mock
    private PositionDao positionDao;


    // injecting mocked dependencies to the testing class via constructor
    @InjectMocks
    private OrderService orderService;

    private Account savedAccount1 = new Account();
    private Trader savedTrader1 = new Trader();
    private Quote savedQuote1 = new Quote();
    private SecurityOrder securityOrder1 = new SecurityOrder();
    private MarketOrderDto marketOrderDto1 = new MarketOrderDto();
    private MarketOrderDto marketOrderDto2 = new MarketOrderDto();

    @Before
    public void setUp() throws Exception {

        Date date = new Date();

        // quotes
        savedQuote1.setAskPrice(10d);
        savedQuote1.setAskSize(10);
        savedQuote1.setBidPrice(10.2d);
        savedQuote1.setBidSize(10);
        savedQuote1.setId("AAPL");
        savedQuote1.setLastPrice(10.1d);
        quoteDao.save(savedQuote1);

        // traders
        savedTrader1.setCountry("Canada");
        savedTrader1.setDob(date); // yyyy-dd-mm in swagger UI
        savedTrader1.setEmail("first@email.com");
        savedTrader1.setFirstName("First");
        savedTrader1.setLastName("Last");

        // traders accounts
        savedAccount1.setTraderId(savedTrader1.getId());
        savedAccount1.setAmount(5000.00);

        // traders security orders
        securityOrder1.setAccountId(savedTrader1.getId());
        securityOrder1.setStatus("FILLED");
        securityOrder1.setTicker(savedQuote1.getTicker());
        // default values before order is placed...
        securityOrder1.setSize(1);
        securityOrder1.setPrice(450.00);
        securityOrder1.setNotes("just a note, bro...");

        // market orders
        marketOrderDto1.setTicker("AAPL");
        marketOrderDto1.setAccountId(1);
        marketOrderDto1.setSize(3);

        marketOrderDto2.setTicker("AAPL");
        marketOrderDto2.setAccountId(2);
        marketOrderDto2.setSize(-2);
    }

    @Test
    public void buyMarketOrder(){

        // TODO - sad path

        // happy path - valid market order purchase
        Integer idNum = 1; // mocked dao, value not present in db

        when(quoteDao.findById(savedQuote1.getId())).thenReturn(Optional.of(savedQuote1));
        when(accountDao.findById(idNum)).thenReturn(Optional.of(savedAccount1));
        when(positionDao.findById(idNum)).thenReturn(Optional.of(new Position()));
        Double currentBalance = savedAccount1.getAmount();

        OrderService spyOrderService = Mockito.spy(orderService);

        SecurityOrder securityOrder = spyOrderService.executeMarketOrder(marketOrderDto1);
        Position position = positionDao.findById(1).get();

        Double orderCost = securityOrder.getSize() * securityOrder.getPrice();
        Double newBalance = currentBalance - orderCost;

        // check order status, correct ticker, and account balance
        assertEquals("FILLED", securityOrder.getStatus());
        assertEquals("", securityOrder.getNotes());
        assertEquals("AAPL", marketOrderDto1.getTicker());
        assertEquals(securityOrder.getSize(), position.getPosition());
        assertEquals(newBalance, savedAccount1.getAmount());

        // check captured securityOrder object against asserted securityOrder
        Mockito.verify(securityOrderDao).save(captorSecurityOrder.capture());
        SecurityOrder capturedSecurityOrder = captorSecurityOrder.getValue();
        assertEquals(securityOrder, capturedSecurityOrder);
    }

    @Test
    public void sellMarketOrder(){

        // TODO - sad path

        // happy path - valid market order sale
        Integer idNum = 1; // mocked dao, value not present in db

        when(quoteDao.findById(savedQuote1.getId())).thenReturn(Optional.of(savedQuote1));
        when(accountDao.findById(idNum)).thenReturn(Optional.of(savedAccount1));
        when(positionDao.findById(idNum)).thenReturn(Optional.of(new Position()));
        Double currentBalance = savedAccount1.getAmount();

        OrderService spyOrderService = Mockito.spy(orderService);

        SecurityOrder securityOrder = spyOrderService.executeMarketOrder(marketOrderDto2);
        Position position = positionDao.findById(1).get();

        Double orderProfit = securityOrder.getSize() * securityOrder.getPrice();
        Double newBalance = currentBalance + orderProfit;

        // check order status, correct ticker, and account balance
        assertEquals("FILLED", securityOrder.getStatus());
        assertEquals("", securityOrder.getNotes());
        assertEquals("AAPL", marketOrderDto2.getTicker());
        assertEquals(securityOrder.getSize(), position.getPosition());
        assertEquals(newBalance, savedAccount1.getAmount());

        // check captured securityOrder object against asserted securityOrder
        Mockito.verify(securityOrderDao).save(captorSecurityOrder.capture());
        SecurityOrder capturedSecurityOrder = captorSecurityOrder.getValue();
        assertEquals(securityOrder, capturedSecurityOrder);

    }

    @After
    public void tearDown() throws Exception {
    }
}