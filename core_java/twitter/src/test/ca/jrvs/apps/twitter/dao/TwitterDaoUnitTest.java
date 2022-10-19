package ca.jrvs.apps.twitter.dao;

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
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.example.JsonParser;
import ca.jrvs.apps.twitter.model.Tweet;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TwitterDaoUnitTest {

    Tweet sampleTweet;

    @Mock
    HttpHelper mockHelper;
    @InjectMocks
    TwitterDao dao;

    @Before
    public void setUp() throws Exception {

        // env vars
        String CONSUMER_KEY = System.getenv("consumerKey");
        String CONSUMER_SECRET = System.getenv("consumerSecret");
        String ACCESS_TOKEN = System.getenv("accessToken");
        String TOKEN_SECRET = System.getenv("tokenSecret");

        // sample tweet created from Json string
        sampleTweet = JsonParser.toObjectFromJson(sampleJson, Tweet.class);
    }

    @After
    public void tearDown() {
        sampleTweet = null;
    }

    @Test
    public void create() throws OAuthMessageSignerException, OAuthExpectationFailedException, URISyntaxException, IOException, OAuthCommunicationException {

        when(mockHelper.httpPost(isNotNull())).thenReturn(null);
        TwitterDao spyDao = Mockito.spy(dao);
        Tweet expectedTweet = sampleTweet;

        // actual tweet
        doReturn(expectedTweet).when(spyDao).tweetFormatter(any());
        // mock tweet
        Tweet spyTweet = spyDao.create(sampleTweet);

        // assertion tests
        assertNotNull(spyTweet);
        assertNotNull(expectedTweet);

        // expectedTweet and spyTweet should be the same
        assertEquals(expectedTweet, spyTweet);
    }

    @Test
    public void findById() throws OAuthMessageSignerException, OAuthExpectationFailedException, URISyntaxException, IOException, OAuthCommunicationException {

        String id = sampleTweet.getId().toString();

        when(mockHelper.httpGet(isNotNull())).thenReturn(null);
        TwitterDao spyDao = Mockito.spy(dao);
        Tweet expectedTweet = sampleTweet;
        // actual tweet
        doReturn(expectedTweet).when(spyDao).tweetFormatter(any());
        // mock tweet
        Tweet spyTweet = spyDao.findById(id);

        // assertion tests
        assertNotNull(spyTweet);
        assertNotNull(expectedTweet);

        // expectedTweet and spyTweet should be the same
        assertEquals(expectedTweet, spyTweet);
    }

    @Test
    public void deleteById() throws URISyntaxException, OAuthMessageSignerException, OAuthExpectationFailedException, IOException, OAuthCommunicationException {

        String id = sampleTweet.getId().toString();

        when(mockHelper.httpPost(isNotNull())).thenReturn(null);
        TwitterDao spyDao = Mockito.spy(dao);
        Tweet expectedTweet = sampleTweet;

        // actual tweet
        doReturn(expectedTweet).when(spyDao).tweetFormatter(any());
        // mock tweet
        Tweet spyTweet = spyDao.deleteById(id);

        // assertion tests
        assertNotNull(spyTweet);
        assertNotNull(expectedTweet);

        // expectedTweet and spyTweet should be the same
        assertEquals(expectedTweet, spyTweet);
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