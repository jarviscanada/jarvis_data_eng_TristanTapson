package ca.jrvs.apps.twitter.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import ca.jrvs.apps.twitter.controller.TwitterController;
import ca.jrvs.apps.twitter.example.JsonParser;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TwitterControllerUnitTest {

    Tweet sampleTweet;

    @Mock
    Service mockService;
    @InjectMocks
    TwitterController controller;

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
    public void tearDown() throws Exception {
        sampleTweet = null;
    }

    @Test
    public void postTweet() throws Exception {

        String  COORD_SEP = ":";
        String longitude = sampleTweet.getCoordinates().getCoordinates().get(0).toString();
        String latitude = sampleTweet.getCoordinates().getCoordinates().get(1).toString();
        String[] myArgsPost = {"post", sampleTweet.getText(), longitude + COORD_SEP + latitude};

        // mock tweet (test creation)
        when(mockService.postTweet(any())).thenReturn(new Tweet());
        TwitterController spyController = Mockito.spy(controller);
        Tweet spyTweet = spyController.postTweet(myArgsPost);
        assertNotNull(spyTweet);

        // break case (invalid coordinate string as command line argument)
        String breakCoordinates = "43a:79b";
        String[] myArgsPostBreak = {"post", sampleTweet.getText(), breakCoordinates};

        // exception expected here (assert statement is reachable)
        try{
            controller.postTweet(myArgsPostBreak);
            fail();
        } catch (RuntimeException ex){
            assertTrue(true);
        }
    }

    @Test
    public void showTweet() throws Exception {

        String COMMA = ",";
        String field1 = "created_at";
        String field2 = "text";
        String field3 = "id";

        List<String> fieldList = Arrays.asList(field1, field2, field3);
        String fields = String.join(COMMA, fieldList);
        String[] myArgsShow = {"show", sampleTweet.getId().toString(), fields};

        // mock tweet (test showing)
        when(mockService.showTweet(any(), any())).thenReturn(new Tweet());
        TwitterController spyController = Mockito.spy(controller);
        Tweet spyTweet = spyController.showTweet(myArgsShow);
        assertNotNull(spyTweet);

        // break case (invalid field string as command line argument)
        List<String> breakFields = new ArrayList<>();
        breakFields.add("invalid_field1");
        breakFields.add("invalid_field2");
        String breakStr = String.join(COMMA, fieldList);
        String[] myArgsShowBreak = {"show", sampleTweet.getId().toString(), breakStr};

        // exception expected here (assert statement is reachable)
        try{
            controller.postTweet(myArgsShowBreak);
            fail();
        } catch (RuntimeException ex){
            assertTrue(true);
        }
    }

    @Test
    public void deleteTweet() throws Exception {

        String COMMA = ",";
        String id1 = sampleTweet.getId().toString();

        List<String> idList = Arrays.asList(id1);
        String ids = String.join(COMMA, idList);
        System.out.println(ids);
        String[] myArgsDelete = {"delete", ids};

        // mock tweet (test deletion)
        when(mockService.deleteTweets(any())).thenReturn(new ArrayList<Tweet>());
        TwitterController spyController = Mockito.spy(controller);
        List<Tweet> spyTweets = spyController.deleteTweet(myArgsDelete);
        assertNotNull(spyTweets);

        // break case (invalid id string as command line argument)
        List<String> breakIds = new ArrayList<>();
        breakIds.add("1097607853932564480_ABC");
        String breakStr = String.join(COMMA, breakIds);
        String[] myArgsDeleteBreak = {"delete", breakStr};

        // exception expected here (assert statement is reachable)
        try{
            controller.postTweet(myArgsDeleteBreak);
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