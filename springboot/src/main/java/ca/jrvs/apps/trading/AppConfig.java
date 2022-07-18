package ca.jrvs.apps.trading;


import ca.jrvs.apps.trading.controller.QuoteController;
import ca.jrvs.apps.trading.dao.MarketDataDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import ca.jrvs.apps.trading.service.QuoteService;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
// @EnableTransactionManagement
public class AppConfig {

    private Logger logger = LoggerFactory.getLogger(AppConfig.class);

    public static void main(String[] args) throws Exception{
        String token = System.getenv("IEX_PUB_TOKEN");
        // System.out.println(token);
    }

    @Bean
    public MarketDataConfig marketDataConfigA(){
        MarketDataConfig marketDataConfig = new MarketDataConfig();
        marketDataConfig.setHost("https://cloud.iexapis.com/v1/");
        marketDataConfig.setToken(System.getenv("IEX_PUB_TOKEN"));
        return marketDataConfig;
    }

    @Bean
    public DataSource dataSourceA(){
        System.out.println("Creating apacheDataSource...(A)");
        String url = System.getenv("PSQL_URL");
        System.out.println(url);
        String user = System.getenv("PSQL_USER");
        String password = System.getenv("PSQL_PASSWORD");
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(url);
        basicDataSource.setUsername(user);
        basicDataSource.setPassword(password);
        return basicDataSource;
    }

    @Bean
    public DataSource dataSource(){
        System.out.println("Creating apacheDataSource...");
        String url = System.getenv("PSQL_URL");
        System.out.println(url);
        String user = System.getenv("PSQL_USER");
        String password = System.getenv("PSQL_PASSWORD");
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(url);
        basicDataSource.setUsername(user);
        basicDataSource.setPassword(password);
        return basicDataSource;
    }


    @Bean
    public HttpClientConnectionManager httpClientConnectionManagerA(){
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(50);
        cm.setDefaultMaxPerRoute(50);
        return cm;
    }
}
