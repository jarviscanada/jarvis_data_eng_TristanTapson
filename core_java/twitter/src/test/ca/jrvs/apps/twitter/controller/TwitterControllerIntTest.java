package src.test.ca.jrvs.apps.twitter.controller;

import org.junit.After;
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
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TwitterControllerIntTest {

    TwitterHttpHelper helper;
    TwitterDao dao;
    TwitterService service;
    TwitterController controller;

    // constructor
    public TwitterControllerIntTest() {}

    @Before
    public void setUp() throws Exception {

        // env vars
        String CONSUMER_KEY = System.getenv("consumerKey");
        String CONSUMER_SECRET = System.getenv("consumerSecret");
        String ACCESS_TOKEN = System.getenv("accessToken");
        String TOKEN_SECRET = System.getenv("tokenSecret");

        // env vars test
        System.out.println("");
        System.out.println("consumer_key = " + CONSUMER_KEY);
        System.out.println("consumer_key_secret = " + CONSUMER_SECRET);
        System.out.println("access_token = " + ACCESS_TOKEN);
        System.out.println("access_token_secret = " + TOKEN_SECRET);
        System.out.println("");

        helper = new TwitterHttpHelper(CONSUMER_KEY,
                CONSUMER_SECRET, ACCESS_TOKEN, TOKEN_SECRET);
        dao = new TwitterDao(helper);
        service = new TwitterService(dao);
        controller = new TwitterController(service);
    }

    @After
    public void tearDown(){
        // TODO: null vars
    }

    @Ignore
    @Test
    public void postTweet() throws Exception {

        String tweetText = "test post int test @tapsonte #test";
        String coordinates = "43:79";

        String[] myArgsPost = {"post", tweetText, coordinates};

        // postTweet method test
        Tweet test = controller.postTweet(myArgsPost);
        System.out.println("JUnit test: TwitterController.postTweet");
        String jsonTest = JsonParser.toJson(test,true,false);
        System.out.println(jsonTest);
    }

    @Ignore
    @Test
    public void showTweet() throws Exception {

        String COMMA = ",";
        String id = "1535466660055875585";
        String field1 = "created_at";
        String field2 = "text";
        String field3 = "id";

        List<String> fieldList = Arrays.asList(field1, field2, field3);
        String fields = String.join(COMMA, fieldList);

        String[] myArgsShow = {"show", id, fields};

        // showTweet method test
        Tweet test = controller.showTweet(myArgsShow);
        System.out.println("JUnit test: TwitterController.showTweet");
        String jsonTest = JsonParser.toJson(test,true,false);
        System.out.println(jsonTest);
    }

    @Ignore
    @Test
    public void deleteTweet() throws Exception {

        String COMMA = ",";
        String id1 = "1535466660055875585";
        String id2 = "1535470690006183936";

        List<String> idList = Arrays.asList(id1, id2);
        String ids = String.join(COMMA, idList);

        String[] myArgsDelete = {"delete", ids};

        // deleteTweet method test
        List<Tweet> deletedTweets = controller.deleteTweet(myArgsDelete);
        System.out.println("JUnit test: TwitterController.deleteTweet");
        for (Tweet tweet : deletedTweets) {
            String jsonTest = JsonParser.toJson(tweet, true, false);
            System.out.println(jsonTest);
        }
    }
}