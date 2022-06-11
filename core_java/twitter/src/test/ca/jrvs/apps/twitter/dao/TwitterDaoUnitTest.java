package src.test.ca.jrvs.apps.twitter.dao;

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
import src.main.ca.jrvs.apps.twitter.dao.TwitterDao;
import src.main.ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import src.main.ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import src.main.ca.jrvs.apps.twitter.example.JsonParser;
import src.main.ca.jrvs.apps.twitter.model.Tweet;


import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TwitterDaoUnitTest {

    Tweet myTweet;
    @Mock
    HttpHelper mockHelper;
    @InjectMocks
    TwitterDao dao;

    // TODO: create & delete, and code cleanup

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

    }

    @After
    public void tearDown() {
        // TODO: null vars
    }

    //@Test
    public void create() throws OAuthMessageSignerException, OAuthExpectationFailedException, URISyntaxException, IOException, OAuthCommunicationException {

        Tweet sampleTweet = JsonParser.toObjectFromJson(sampleJson, Tweet.class);

        try{
            dao.create(sampleTweet);
        } catch (RuntimeException ex){
            assertTrue(true);
        }

        TwitterDao spyDao = Mockito.spy(dao);
        Tweet tweet = spyDao.create(sampleTweet);

        // assertion test to see if tweet object was created
        assertNotNull(tweet);
    }

    @Test
    public void findById() throws OAuthMessageSignerException, OAuthExpectationFailedException, URISyntaxException, IOException, OAuthCommunicationException {

        //TODO: mockito fix (basically its creating a "mock" instance of HttpHelper via the @Mock tag

        String id = "1535458118318047235";

        /* exception is expected here (unnecessary code...)
        when(mockHelper.httpGet(isNotNull())).thenThrow(new RuntimeException("mock"));
        if(mockHelper == null){
            System.out.println("null");
        }
        else{
            System.out.println("not null");
        }*/

        try {
            dao.findById(id);
            fail();
        } catch (RuntimeException e) {
            assertTrue(true);
        }

        when(mockHelper.httpGet(isNotNull())).thenReturn(null);
        TwitterDao spyDAO = Mockito.spy(dao);
        Tweet expectedTweet = JsonParser.toObjectFromJson(sampleJson, Tweet.class);

        // mock tweet
        doReturn(expectedTweet).when(spyDAO).tweetFormatter(any());
        // actual tweet
        Tweet spyTweet = spyDAO.findById(id);

        // TODO: more assertion tests?
        // assertion tests
        assertNotNull(spyTweet);
        assertNotNull(expectedTweet.getText());
        assertEquals(expectedTweet, spyTweet); // expectedTweet and spyTweet should be the same

    }

    //@Test
    public void deleteById() throws URISyntaxException, OAuthMessageSignerException, OAuthExpectationFailedException, IOException, OAuthCommunicationException {

        String id = "1535447651608674304";

        try{
            dao.deleteById(id);
        } catch (RuntimeException ex){
            assertTrue(true);
        }

        when(mockHelper.httpGet(isNotNull())).thenReturn(null);
        TwitterDao spyDao = Mockito.spy(dao);
        Tweet tweet = spyDao.deleteById(id);

        // assertion test to see if tweet object was deleted
        assertNotNull(tweet);
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