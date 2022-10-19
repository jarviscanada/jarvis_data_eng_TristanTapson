package ca.jrvs.apps.twitter.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ca.jrvs.apps.twitter.TwitterCLIApp;

@Configuration
@ComponentScan(value = "ca.jrvs.apps.twitter")
public class TwitterCLIComponentScan {

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(
                TwitterCLIComponentScan.class);
        TwitterCLIApp app = context.getBean(TwitterCLIApp.class);
        app.run(args);
    }
}