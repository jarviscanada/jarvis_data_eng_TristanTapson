package src.test.ca.jrvs.apps.twitter.controller;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import src.main.ca.jrvs.apps.twitter.controller.TwitterController;
import src.main.ca.jrvs.apps.twitter.dao.TwitterDao;
import src.main.ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import src.main.ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import src.main.ca.jrvs.apps.twitter.example.JsonParser;
import src.main.ca.jrvs.apps.twitter.model.Tweet;
import src.main.ca.jrvs.apps.twitter.service.TwitterService;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class TwitterControllerIntTest {

    private static String CONSUMER_KEY = System.getenv("consumerKey");
    private static String CONSUMER_SECRET = System.getenv("consumerSecret");
    private static String ACCESS_TOKEN = System.getenv("accessToken");
    private static String TOKEN_SECRET = System.getenv("tokenSecret");

    HttpHelper helper = new TwitterHttpHelper(CONSUMER_KEY,
            CONSUMER_SECRET, ACCESS_TOKEN, TOKEN_SECRET);
    TwitterDao dao = new TwitterDao(helper);
    TwitterService service = new TwitterService(dao);
    TwitterController controller = new TwitterController(service);

    String[] myArgsPost = {"post", "test post main @tapsonte #test", "43:79"};
    String[] myArgsShow = {"show", "1534859668211777536", "created_at,text,coordinates"};
    String[] myArgsDelete = {"delete", "1534794763572060160,1534794822816505856"};


    // constructor
    public TwitterControllerIntTest() {
    }

    @Before
    public void setUp() throws Exception {
       // TODO: setup & teardown
    }

    @Ignore("temp ignore")
    @Test
    public void postTweet() throws Exception {
        // env vars test
        System.out.println("consumer_key = " + CONSUMER_KEY);
        System.out.println("consumer_key_secret = " + CONSUMER_SECRET);
        System.out.println("access_token = " + ACCESS_TOKEN);
        System.out.println("access_token_secret = " + TOKEN_SECRET);
        System.out.println("");

        Tweet test = controller.postTweet(myArgsPost);
        String jsonTest = JsonParser.toJson(test,true,false);
        System.out.println(jsonTest);
    }

    @Ignore("temp ignore")
    @Test
    public void showTweet() throws Exception {
        // env vars test
        System.out.println("consumer_key = " + CONSUMER_KEY);
        System.out.println("consumer_key_secret = " + CONSUMER_SECRET);
        System.out.println("access_token = " + ACCESS_TOKEN);
        System.out.println("access_token_secret = " + TOKEN_SECRET);
        System.out.println("");

        Tweet test = controller.showTweet(myArgsShow);
        String jsonTest = JsonParser.toJson(test,true,false);
        System.out.println(jsonTest);
    }

    @Ignore("temp ignore")
    @Test
    public void deleteTweet() throws Exception {
        // env vars test
        System.out.println("consumer_key = " + CONSUMER_KEY);
        System.out.println("consumer_key_secret = " + CONSUMER_SECRET);
        System.out.println("access_token = " + ACCESS_TOKEN);
        System.out.println("access_token_secret = " + TOKEN_SECRET);
        System.out.println("");

        List<Tweet> delTweets = controller.deleteTweet(myArgsDelete);
        for (Tweet tweet : delTweets) {
            String jsonTest = JsonParser.toJson(tweet, true, false);
            System.out.println(jsonTest);
        }
    }
}