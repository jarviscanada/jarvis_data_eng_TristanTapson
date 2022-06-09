package src.main.ca.jrvs.apps.twitter;

import org.springframework.http.HttpMethod;
import src.main.ca.jrvs.apps.twitter.controller.TwitterController;
import src.main.ca.jrvs.apps.twitter.dao.CrdDao;
import src.main.ca.jrvs.apps.twitter.dao.TwitterDao;
import src.main.ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import src.main.ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import com.google.gdata.util.common.base.PercentEscaper;
import com.sun.jndi.toolkit.url.Uri;
import src.main.ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import src.main.ca.jrvs.apps.twitter.example.JsonParser;
import src.main.ca.jrvs.apps.twitter.example.dto.Company;
import src.main.ca.jrvs.apps.twitter.model.*;
import src.main.ca.jrvs.apps.twitter.service.TwitterService;

import java.net.URI;
import java.util.*;

import static src.main.ca.jrvs.apps.twitter.example.JsonParser.toJson;


public class TwitterCLIApp {

    private static String CONSUMER_KEY = System.getenv("consumerKey");
    private static String CONSUMER_SECRET = System.getenv("consumerSecret");
    private static String ACCESS_TOKEN = System.getenv("accessToken");
    private static String TOKEN_SECRET = System.getenv("tokenSecret");

    public static void main(String[] args) throws Exception {

        System.out.println("--- TwitterCLIApp Class ---");

        // env vars test
        System.out.println("consumer_key = " + CONSUMER_KEY);
        System.out.println("consumer_key_secret = " + CONSUMER_SECRET);
        System.out.println("access_token = " + ACCESS_TOKEN);
        System.out.println("access_token_secret = " + TOKEN_SECRET);
        System.out.println("");

        HttpHelper helper = new TwitterHttpHelper(CONSUMER_KEY,
                CONSUMER_SECRET, ACCESS_TOKEN, TOKEN_SECRET);
        TwitterDao dao = new TwitterDao(helper);
        TwitterService service = new TwitterService(dao);
        TwitterController controller = new TwitterController(service);

        String[] myArgsPost = {"post", "test post controller @tapsonte #test", "43:79"};
        String[] myArgsShow = {"show", "1534784886027984897", "created_at,text,coordinates"};
        String[] myArgsDelete = {"delete", "1534794763572060160,1534794822816505856"};

        //Tweet test = controller.postTweet(myArgsPost);
        Tweet test = controller.showTweet(myArgsShow);
        //Tweet test = controller.deleteTweet(myArgsDelete);
        String jsonTest = JsonParser.toJson(test,true,false);
        System.out.println(jsonTest);

        /*
        List<Tweet> delTweets = controller.deleteTweet(myArgsDelete);
        for (Tweet tweet : delTweets) {
            String jsonTest = JsonParser.toJson(tweet, true, false);
            System.out.println(jsonTest);
        }*/
    }
}
