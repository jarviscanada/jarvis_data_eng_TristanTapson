package src.test.ca.jrvs.apps.twitter.dao.helper;

import com.google.gdata.util.common.base.PercentEscaper;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import src.main.ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import src.main.ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

public class TwitterHttpHelperIntTest {

    HttpHelper helper;
    PercentEscaper percentEscaper;

    // constructor
    public TwitterHttpHelperIntTest() throws URISyntaxException {};

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

        helper = new TwitterHttpHelper(CONSUMER_KEY,
                CONSUMER_SECRET, ACCESS_TOKEN, TOKEN_SECRET);
        percentEscaper = new PercentEscaper("", false);
    }

    @After
    public void tearDown() throws Exception {
        // TODO: null vars
    }

    @Ignore
    @Test
    public void httpPost() throws OAuthMessageSignerException, OAuthExpectationFailedException, IOException, OAuthCommunicationException, URISyntaxException {

        // uri & strings
        String postStr = "https://api.twitter.com/1.1/statuses/update.json?status=";
        String status = "Hello World!";
        URI myPostURI = new URI(postStr + percentEscaper.escape(status));

        // httpPost method test
        HttpResponse response = helper.httpPost(myPostURI);
        System.out.println("JUnit test: TwitterHttpHelper.httpPost");
        System.out.println(EntityUtils.toString(response.getEntity()));
    }

    @Ignore
    @Test
    public void httpGet() throws OAuthMessageSignerException, OAuthExpectationFailedException, IOException, OAuthCommunicationException, URISyntaxException {

        // uri & strings
        String getStr = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=";
        String screenName = "tapsonte";
        URI myGetURI = new URI(getStr + screenName);

        // httpGet method test
        HttpResponse response = helper.httpGet(myGetURI);
        System.out.println("JUnit test: TwitterHttpHelper.httpGet");
        System.out.println(EntityUtils.toString(response.getEntity()));
    }
}