package src.test.ca.jrvs.apps.twitter.service;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.junit.Before;
import org.junit.Test;
import src.main.ca.jrvs.apps.twitter.dao.TwitterDao;
import src.main.ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import src.main.ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import src.main.ca.jrvs.apps.twitter.example.JsonParser;
import src.main.ca.jrvs.apps.twitter.model.Tweet;
import src.main.ca.jrvs.apps.twitter.service.TwitterService;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.*;
import static src.test.ca.jrvs.apps.twitter.dao.TwitterDaoTest.sampleJson;

public class TwitterServiceIntTest {

    private static String CONSUMER_KEY = System.getenv("consumerKey");
    private static String CONSUMER_SECRET = System.getenv("consumerSecret");
    private static String ACCESS_TOKEN = System.getenv("accessToken");
    private static String TOKEN_SECRET = System.getenv("tokenSecret");

    HttpHelper helper = new TwitterHttpHelper(CONSUMER_KEY,
            CONSUMER_SECRET, ACCESS_TOKEN, TOKEN_SECRET);
    TwitterDao dao = new TwitterDao(helper);
    TwitterService service = new TwitterService(dao);

    Tweet myTweet = JsonParser.toObjectFromJson(sampleJson, Tweet.class);

    String[] delIds = new String[2];
    String[] fields = new String[2];

    // constructor
    public TwitterServiceIntTest() throws IOException {
    }

    @Before
    public void setUp() throws Exception {

        fields[0] = "created_at";
        fields[1] = "text";

        delIds[0] = "1534513688450834432";
        delIds[1] = "1534515372010913792";
    }

    //@Test
    public void postTweet() throws OAuthMessageSignerException, OAuthExpectationFailedException, URISyntaxException, IOException, OAuthCommunicationException {
        // env vars test
        System.out.println("consumer_key = " + CONSUMER_KEY);
        System.out.println("consumer_key_secret = " + CONSUMER_SECRET);
        System.out.println("access_token = " + ACCESS_TOKEN);
        System.out.println("access_token_secret = " + TOKEN_SECRET);
        System.out.println("");

        // TODO: assert testing, rather than compile testing...
        // postTweet method test
        System.out.println("JUNIT test: TwitterService.postTweet");
        service.postTweet(myTweet);
    }

    //@Test
    public void showTweet() throws OAuthMessageSignerException, OAuthExpectationFailedException, URISyntaxException, IOException, OAuthCommunicationException {
        // env vars test
        System.out.println("consumer_key = " + CONSUMER_KEY);
        System.out.println("consumer_key_secret = " + CONSUMER_SECRET);
        System.out.println("access_token = " + ACCESS_TOKEN);
        System.out.println("access_token_secret = " + TOKEN_SECRET);
        System.out.println("");

        // TODO: assert testing, rather than compile testing...
        // showTweet method test
        System.out.println("JUNIT test: TwitterService.showTweet");
        service.showTweet("1534513688450834432", fields);
    }

    @Test
    public void deleteTweets() throws OAuthMessageSignerException, OAuthExpectationFailedException, URISyntaxException, IOException, OAuthCommunicationException {
        // env vars test
        System.out.println("consumer_key = " + CONSUMER_KEY);
        System.out.println("consumer_key_secret = " + CONSUMER_SECRET);
        System.out.println("access_token = " + ACCESS_TOKEN);
        System.out.println("access_token_secret = " + TOKEN_SECRET);
        System.out.println("");

        // TODO: assert testing, rather than compile testing...
        // deleteTweets method test
        System.out.println("JUNIT test: TwitterService.deleteTweets");
        service.deleteTweets(delIds);
    }

    // sample pretty JSON string
    public static final String sampleJson =

            "{\n" +
                    "   \"created_at\":\"Mon Feb 18 21:24:39 +0000 2019\",\n" +
                    "   \"id\":1097607853932564480,\n" +
                    "   \"id_str\":\"1097607853932564480\",\n" +
                    "   \"text\":\"test with loc223 @tapsonte #test J\",\n" +
                    "   \"entities\":{\n" +
                    "      \"hashtags\":[\n" +
                    "         {\n" +
                    "            \"text\":\"documentation\",\n" +
                    "            \"indices\":[\n" +
                    "               211,\n" +
                    "               225\n" +
                    "            ]\n" +
                    "         },\n" +
                    "         {\n" +
                    "            \"text\":\"parsingJSON\",\n" +
                    "            \"indices\":[\n" +
                    "               226,\n" +
                    "               238\n" +
                    "            ]\n" +
                    "         },\n" +
                    "         {\n" +
                    "            \"text\":\"GeoTagged\",\n" +
                    "            \"indices\":[\n" +
                    "               239,\n" +
                    "               249\n" +
                    "            ]\n" +
                    "         }\n" +
                    "      ],\n" +
                    "      \"user_mentions\":[\n" +
                    "         {\n" +
                    "            \"name\":\"Twitter API\",\n" +
                    "            \"indices\":[\n" +
                    "               4,\n" +
                    "               15\n" +
                    "            ],\n" +
                    "            \"screen_name\":\"twitterapi\",\n" +
                    "            \"id\":6253282,\n" +
                    "            \"id_str\":\"6253282\"\n" +
                    "         }\n" +
                    "      ]\n" +
                    "   },\n" +
                    "   \"coordinates\":{\n" +
                    "      \"coordinates\":[\n" +
                    "         -75.14310264,\n" +
                    "         40.05701649\n" +
                    "      ],\n" +
                    "      \"type\":\"Point\"\n" +
                    "   },\n" +
                    "   \"retweet_count\":0,\n" +
                    "   \"favorite_count\":0,\n" +
                    "   \"favorited\":false,\n" +
                    "   \"retweeted\":false\n" +
                    "}";
}
