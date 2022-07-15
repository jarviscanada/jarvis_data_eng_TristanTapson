package ca.jrvs.apps.trading;


import ca.jrvs.apps.trading.controller.QuoteController;
import ca.jrvs.apps.trading.dao.MarketDataDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import ca.jrvs.apps.trading.service.QuoteService;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
// @EnableTransactionManagement
public class AppConfig {

    private Logger logger = LoggerFactory.getLogger(AppConfig.class);

    public static void main(String[] args) throws Exception{
        System.out.println(System.getenv("IEX_PUB_TOKEN"));
    }

    @Bean
    public MarketDataConfig marketDataConfig(){

        String host = "https://cloud.iexapis.com/v1/";
        String token = System.getenv("IEX_PUB_TOKEN");

        MarketDataConfig config = new MarketDataConfig();
        config.setHost(host);
        config.setToken(token);

        return config;
    }


    @Bean
    HttpClientConnectionManager httpClientConnectionManager(){
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        manager.setMaxTotal(50);
        manager.setDefaultMaxPerRoute(50);
        return manager;
    }
}
