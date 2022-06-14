package ca.jrvs.apps.twitter.service;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.example.JsonParser;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.TwitterService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TwitterServiceUnitTest {

    Tweet sampleTweet;
    List<String> fieldsList;
    List<String> idsList;

    @Mock
    CrdDao mockDao;
    @InjectMocks
    TwitterService service;

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

        // sample tweet created from Json string, sample field and id lists
        sampleTweet = JsonParser.toObjectFromJson(sampleJson, Tweet.class);
        fieldsList = Arrays.asList("created_at", "id", "text");
        idsList = Arrays.asList(sampleTweet.getId().toString());
    }

    @After
    public void tearDown() throws Exception {
        sampleTweet = null;
        fieldsList = null;
        idsList = null;
    }

    @Test
    public void postTweet() throws OAuthMessageSignerException, OAuthExpectationFailedException, URISyntaxException, IOException, OAuthCommunicationException {

        // mock tweet (test creation)
        when(mockDao.create(any())).thenReturn(new Tweet());
        TwitterService spyService = Mockito.spy(service);
        Tweet spyTweet = spyService.postTweet(sampleTweet);
        assertNotNull(spyTweet);

        // actual tweet
        Tweet breakTweet = sampleTweet;
        // break case (set tweet text to have invalid text (140+ characters via lorem text))
        breakTweet.setText("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et mag");

        // exception expected here (assert statement is reachable)
        try{
            service.postTweet(breakTweet);
            fail();
        } catch (RuntimeException ex){
            assertTrue(true);
        }
    }

    @Test
    public void showTweet() throws OAuthMessageSignerException, OAuthExpectationFailedException, URISyntaxException, IOException, OAuthCommunicationException {

        String id = sampleTweet.getId().toString();
        String[] fieldsArr = fieldsList.stream().toArray(String[]::new);

        // mock tweet (test showing)
        when(mockDao.findById(any())).thenReturn(new Tweet());
        TwitterService spyService = Mockito.spy(service);
        Tweet spyTweet = spyService.showTweet(id, fieldsArr);
        assertNotNull(spyTweet);

        // actual tweet
        Tweet breakTweet = sampleTweet;
        String breakId = breakTweet.getId().toString();
        // break case (set fields array to have an invalid field)
        List<String> breakFields = new ArrayList<>();
        breakFields.add("invalid_field");
        String[] breakArr = breakFields.stream().toArray(String[]::new);

        // exception expected here (assert statement is reachable)
        try{
            service.showTweet(breakId, breakArr);
            fail();
        } catch (RuntimeException ex){
            assertTrue(true);
        }
    }

    @Test
    public void deleteTweets() throws OAuthMessageSignerException, OAuthExpectationFailedException, URISyntaxException, IOException, OAuthCommunicationException {

        String id = sampleTweet.getId().toString();
        String[] idsArr = idsList.stream().toArray(String[]::new);

        // mock tweet (test deletion)
        when(mockDao.deleteById(any())).thenReturn(new Tweet());
        TwitterService spyService = Mockito.spy(service);
        List<Tweet> spyTweets = spyService.deleteTweets(idsArr);
        assertNotNull(spyTweets);

        // actual tweet
        Tweet breakTweet = sampleTweet;
        // break case (set ids array to have an invalid id)
        List<String> breakIds = new ArrayList<>();
        breakIds.add("1097607853932564480_ABC");
        String[] breakArr = breakIds.stream().toArray(String[]::new);

        // exception expected here (assert statement is reachable)
        try{
            service.deleteTweets(breakArr);
            fail();
        } catch (RuntimeException ex){
            assertTrue(true);
        }
    }

    // sample pretty JSON string
    public static final String sampleJson =

            "{\n" +
                    "   \"created_at\":\"Mon Feb 18 21:24:39 +0000 2019\",\n" +
                    "   \"id\":1097607853932564480,\n" +
                    "   \"id_str\":\"1097607853932564480\",\n" +
                    "   \"text\":\"test with loc223 J\",\n" +
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