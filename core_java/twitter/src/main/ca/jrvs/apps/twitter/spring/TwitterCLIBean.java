package src.main.ca.jrvs.apps.twitter.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import src.main.ca.jrvs.apps.twitter.TwitterCLIApp;
import src.main.ca.jrvs.apps.twitter.controller.Controller;
import src.main.ca.jrvs.apps.twitter.controller.TwitterController;
import src.main.ca.jrvs.apps.twitter.dao.CrdDao;
import src.main.ca.jrvs.apps.twitter.dao.TwitterDao;
import src.main.ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import src.main.ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import src.main.ca.jrvs.apps.twitter.service.Service;
import src.main.ca.jrvs.apps.twitter.service.TwitterService;

//@Configuration
public class TwitterCLIBean {

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(TwitterCLIBean.class);
        TwitterCLIApp app = context.getBean(TwitterCLIApp.class);
        app.run(args);
    }

    @Bean
    public TwitterCLIApp twitterCLIApp(Controller controller) {
        return new TwitterCLIApp(controller);
    }

    @Bean
    public Controller controller(Service service){
        return new TwitterController(service);
    }

    @Bean
    public Service service(CrdDao dao){
        return new TwitterService(dao);
    }

    @Bean
    public CrdDao crdDao(HttpHelper httpHelper){
        return new TwitterDao(httpHelper);
    }

    @Bean
    HttpHelper helper(){
        String CONSUMER_KEY = System.getenv("consumerKey");
        String CONSUMER_SECRET = System.getenv("consumerSecret");
        String ACCESS_TOKEN = System.getenv("accessToken");
        String TOKEN_SECRET = System.getenv("tokenSecret");
        return new TwitterHttpHelper(CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN, TOKEN_SECRET);
    }
}