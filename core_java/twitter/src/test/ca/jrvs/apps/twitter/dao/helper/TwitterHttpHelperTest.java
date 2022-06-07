package src.test.ca.jrvs.apps.twitter.dao.helper;

import com.google.gdata.util.common.base.PercentEscaper;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import src.main.ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import src.main.ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

public class TwitterHttpHelperTest {

    private static String CONSUMER_KEY = System.getenv("consumerKey");
    private static String CONSUMER_SECRET = System.getenv("consumerSecret");
    private static String ACCESS_TOKEN = System.getenv("accessToken");
    private static String TOKEN_SECRET = System.getenv("tokenSecret");

    // uri strings
    String postUpdate = "https://api.twitter.com/1.1/statuses/update.json?status=";
    String get = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=";

    // uri string vars
    String status = "VM JUnit test: testing 123";
    String id = "1533275853152522240";
    String screenName = "tapsonte";
    PercentEscaper percentEscaper = new PercentEscaper("", false);

    // uri strings
    URI myPostURI = new URI(postUpdate + percentEscaper.escape(status));
    URI myGetURI = new URI(get + screenName);

    HttpHelper helper = new TwitterHttpHelper(CONSUMER_KEY,
            CONSUMER_SECRET, ACCESS_TOKEN, TOKEN_SECRET);

    // constructor
    public TwitterHttpHelperTest() throws URISyntaxException {
    }

    @Test
    public void httpPost() throws OAuthMessageSignerException, OAuthExpectationFailedException, IOException, OAuthCommunicationException  {

        // env vars test
        System.out.println("consumer_key = " + CONSUMER_KEY);
        System.out.println("consumer_key_secret = " + CONSUMER_SECRET);
        System.out.println("access_token = " + ACCESS_TOKEN);
        System.out.println("access_token_secret = " + TOKEN_SECRET);
        System.out.println("");

        // TODO: assert testing, rather than compile testing...
        // httpPost method test
        System.out.println("JUNIT test: TwitterHttpHelper.httpPost");
        HttpResponse response = helper.httpPost(myPostURI);
        System.out.println(EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void httpGet() throws OAuthMessageSignerException, OAuthExpectationFailedException, IOException, OAuthCommunicationException {

        // env vars test
        System.out.println("consumer_key = " + CONSUMER_KEY);
        System.out.println("consumer_key_secret = " + CONSUMER_SECRET);
        System.out.println("access_token = " + ACCESS_TOKEN);
        System.out.println("access_token_secret = " + TOKEN_SECRET);
        System.out.println("");

        // TODO: assert testing, rather than compile testing...
        // httpGet method test
        System.out.println("JUNIT test: TwitterHttpHelper.httpGet");
        HttpResponse response = helper.httpGet(myGetURI);
        System.out.println(EntityUtils.toString(response.getEntity()));
    }
}