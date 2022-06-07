package src.test.ca.jrvs.apps.twitter.dao;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Ignore;
import org.junit.Test;
import src.main.ca.jrvs.apps.twitter.dao.TwitterDao;
import src.main.ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import src.main.ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import src.main.ca.jrvs.apps.twitter.example.JsonParser;
import src.main.ca.jrvs.apps.twitter.model.Tweet;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

public class TwitterDaoTest {

    private static String CONSUMER_KEY = System.getenv("consumerKey");
    private static String CONSUMER_SECRET = System.getenv("consumerSecret");
    private static String ACCESS_TOKEN = System.getenv("accessToken");
    private static String TOKEN_SECRET = System.getenv("tokenSecret");

    HttpHelper helper = new TwitterHttpHelper(CONSUMER_KEY,
            CONSUMER_SECRET, ACCESS_TOKEN, TOKEN_SECRET);
    Tweet myTweet = JsonParser.toObjectFromJson(sampleJson, Tweet.class);
    TwitterDao dao = new TwitterDao(helper);

    // constructor
    public TwitterDaoTest() throws IOException {
    }

    @Ignore("temp ignore")
    @Test
    public void create() throws URISyntaxException, OAuthMessageSignerException, OAuthExpectationFailedException, IOException, OAuthCommunicationException {

        // env vars test
        System.out.println("consumer_key = " + CONSUMER_KEY);
        System.out.println("consumer_key_secret = " + CONSUMER_SECRET);
        System.out.println("access_token = " + ACCESS_TOKEN);
        System.out.println("access_token_secret = " + TOKEN_SECRET);
        System.out.println("");

        // TODO: assert testing, rather than compile testing...
        // create method test
        System.out.println("JUNIT test: TwitterDAO.create");
        dao.create(myTweet);

    }

    @Ignore("temp ignore")
    @Test
    public void findById() throws URISyntaxException, OAuthMessageSignerException, OAuthExpectationFailedException, IOException, OAuthCommunicationException {

        // env vars test
        System.out.println("consumer_key = " + CONSUMER_KEY);
        System.out.println("consumer_key_secret = " + CONSUMER_SECRET);
        System.out.println("access_token = " + ACCESS_TOKEN);
        System.out.println("access_token_secret = " + TOKEN_SECRET);
        System.out.println("");

        // TODO: assert testing, rather than compile testing...
        // findById method test
        System.out.println("JUNIT test: TwitterDAO.findById");
        dao.findById("1533718066793693184");

    }

    //@Ignore("temp ignore")
    @Test
    public void deleteById() throws URISyntaxException, OAuthMessageSignerException, OAuthExpectationFailedException, IOException, OAuthCommunicationException {

        // env vars test
        System.out.println("consumer_key = " + CONSUMER_KEY);
        System.out.println("consumer_key_secret = " + CONSUMER_SECRET);
        System.out.println("access_token = " + ACCESS_TOKEN);
        System.out.println("access_token_secret = " + TOKEN_SECRET);
        System.out.println("");

        // TODO: assert testing, rather than compile testing...
        // deleteById method test
        System.out.println("JUNIT test: TwitterDAO.deleteById");
        dao.deleteById("1534028002513801216");

    }

    // sample pretty JSON string
    public static final String sampleJson =

            "{\n" +
                    "   \"created_at\":\"Mon Feb 18 21:24:39 +0000 2019\",\n" +
                    "   \"id\":1097607853932564480,\n" +
                    "   \"id_str\":\"1097607853932564480\",\n" +
                    "   \"text\":\"test with loc223 (JUnit)\",\n" +
                    "   \"entities\":{\n" +
                    "      \"hashtags\":[],\n" +
                    "      \"user_mentions\":[]\n" +
                    "   },\n" +
                    "   \"coordinates\":null,\n" +
                    "   \"retweet_count\":0,\n" +
                    "   \"favorite_count\":0,\n" +
                    "   \"favorited\":false,\n" +
                    "   \"retweeted\":false\n" +
                    "}";
}