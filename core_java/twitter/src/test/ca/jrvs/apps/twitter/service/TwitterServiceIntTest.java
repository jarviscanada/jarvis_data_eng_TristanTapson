package ca.jrvs.apps.twitter.service;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.example.JsonParser;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.TwitterService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class TwitterServiceIntTest {

    Tweet myTweet;
    TwitterHttpHelper helper;
    TwitterDao dao;
    TwitterService service;

    // constructor
    public TwitterServiceIntTest() throws IOException {};

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

        myTweet = JsonParser.toObjectFromJson(sampleJson, Tweet.class);

        helper = new TwitterHttpHelper(CONSUMER_KEY,
                CONSUMER_SECRET, ACCESS_TOKEN, TOKEN_SECRET);
        dao = new TwitterDao(helper);
        service = new TwitterService(dao);
    }

    @After
    public void tearDown() throws Exception {
        // TODO: null vars
    }

    @Ignore
    @Test
    public void postTweet() throws OAuthMessageSignerException, OAuthExpectationFailedException, URISyntaxException, IOException, OAuthCommunicationException {

        // postTweet method test
        System.out.println("JUnit test: TwitterService.postTweet");
        Tweet tweet = service.postTweet(myTweet);
        System.out.println(JsonParser.toJson(tweet, true, false));
    }

    @Ignore
    @Test
    public void showTweet() throws OAuthMessageSignerException, OAuthExpectationFailedException, URISyntaxException, IOException, OAuthCommunicationException {

        String id = "1536727242822275079";
        String field1 = "created_at";
        String field2 = "text";
        String[] fields = {field1, field2};

        // showTweet method test
        System.out.println("JUnit test: TwitterService.showTweet");
        Tweet tweet = service.showTweet(id, fields);
        System.out.println(JsonParser.toJson(tweet, true, false));
    }

    @Ignore
    @Test
    public void deleteTweets() throws OAuthMessageSignerException, OAuthExpectationFailedException, URISyntaxException, IOException, OAuthCommunicationException {

        String id1 = "1536727242822275079";
        String id2 = "1536727735099445249";
        String[] ids = {id1, id2};

        // deleteTweets method test
        System.out.println("JUnit test: TwitterService.deleteTweets");
        List<Tweet> deletedTweets = service.deleteTweets(ids);
        for(Tweet tweet : deletedTweets) {
            System.out.println(JsonParser.toJson(tweet, true, false));
        }
    }

    // sample pretty JSON string
    public static final String sampleJson =

            "{\n" +
                    "   \"created_at\":\"Mon Feb 18 21:24:39 +0000 2019\",\n" +
                    "   \"id\":1097607853932564480,\n" +
                    "   \"id_str\":\"1097607853932564480\",\n" +
                    "   \"text\":\"test with loc223\",\n" +
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