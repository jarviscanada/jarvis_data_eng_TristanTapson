package src.main.ca.jrvs.apps.twitter;

import org.springframework.http.HttpMethod;
import src.main.ca.jrvs.apps.twitter.dao.CrdDao;
import src.main.ca.jrvs.apps.twitter.dao.TwitterDao;
import src.main.ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import src.main.ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import com.google.gdata.util.common.base.PercentEscaper;
import com.sun.jndi.toolkit.url.Uri;
import src.main.ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import src.main.ca.jrvs.apps.twitter.example.JsonParser;
import src.main.ca.jrvs.apps.twitter.model.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class TwitterCLIApp {

    private static String CONSUMER_KEY = System.getenv("consumerKey");
    private static String CONSUMER_SECRET = System.getenv("consumerSecret");
    private static String ACCESS_TOKEN = System.getenv("accessToken");
    private static String TOKEN_SECRET = System.getenv("tokenSecret");

    public static void main(String[] args) throws Exception {

        // TODO: test using JUnit, rather than main...
        //          clean this up later after implementing JUnit testing...

        System.out.println("--- TwitterCLIApp Class ---");

        // env vars test
        System.out.println("consumer_key = " + CONSUMER_KEY);
        System.out.println("consumer_key_secret = " + CONSUMER_SECRET);
        System.out.println("access_token = " + ACCESS_TOKEN);
        System.out.println("access_token_secret = " + TOKEN_SECRET);
        System.out.println("");

        // method types
        HttpMethod postMethod = HttpMethod.POST;
        HttpMethod getMethod = HttpMethod.GET;

        // uri strings
        String postUpdate = "https://api.twitter.com/1.1/statuses/update.json?status=";
        String get = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=";

        // uri string vars
        String status = "VM API test: hello again number 5";
        String id = "1533275853152522240";
        String screenName = "tapsonte";
        PercentEscaper percentEscaper = new PercentEscaper("", false);

        URI myPostURI = new URI(postUpdate + percentEscaper.escape(status));
        URI myGetURI = new URI(get + screenName);

        //System.out.println(postMethod.toString());

        HttpHelper helper = new TwitterHttpHelper(CONSUMER_KEY,
                CONSUMER_SECRET, ACCESS_TOKEN, TOKEN_SECRET);

        // post tweet to twitter from URI using POST (TwitterHttpHelper) ***
        // helper.httpPost(myPostURI);

        // get twitter account info from URI using GET (TwitterHttpHelper) ***
        // helper.httpGet(myGetURI);

        // dao test (TwitterDao) ***
        Tweet myTweet = JsonParser.toObjectFromJson(sampleJson, Tweet.class);


        //String json = JsonParser.toJson(myTweet,true,true);
        //System.out.println(json);

        TwitterDao dao = new TwitterDao(helper);
        //dao.create(myTweet);
        //dao.findById("1533925395942330368");
        //dao.deleteById("1534010613181071362");
    }

    public static final String sampleJson =

                   "{\n" +
                   "   \"created_at\":\"Mon Feb 18 21:24:39 +0000 2019\",\n" +
                   "   \"id\":1097607853932564480,\n" +
                   "   \"id_str\":\"1097607853932564480\",\n" +
                   "   \"text\":\"test with loc223 (dao)\",\n" +
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
