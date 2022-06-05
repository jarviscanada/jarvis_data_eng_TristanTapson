package ca.jrvs.apps.twitter;

import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import com.google.gdata.util.common.base.PercentEscaper;
import com.sun.jndi.toolkit.url.Uri;

import java.net.URI;

public class TwitterCLIApp {

    private static String CONSUMER_KEY = System.getenv("consumerKey");
    private static String CONSUMER_SECRET = System.getenv("consumerSecret");
    private static String ACCESS_TOKEN = System.getenv("accessToken");
    private static String TOKEN_SECRET = System.getenv("tokenSecret");

    public static void main(String[] args) throws Exception {

        // TODO: test using JUnit, rather than main...

        System.out.println("--- TwitterCLIApp class ---");

        // env vars test
        System.out.println("consumer_key = " + CONSUMER_KEY);
        System.out.println("consumer_key_secret = " + CONSUMER_SECRET);
        System.out.println("access_token = " + ACCESS_TOKEN);
        System.out.println("access_token_secret = " + TOKEN_SECRET);
        System.out.println("");

        // uri strings
        String postUpdate = "https://api.twitter.com/1.1/statuses/update.json?status=";
        String get = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=";

        // uri string vars
        String status = "VM API test: hello again number 2";
        String id = "1533275853152522240";
        String screenName = "tapsonte";
        PercentEscaper percentEscaper = new PercentEscaper("", false);

        URI myPostURI = new URI(postUpdate + percentEscaper.escape(status));
        URI myGetURI = new URI(get + screenName);

        TwitterHttpHelper helper = new TwitterHttpHelper(CONSUMER_KEY,
                CONSUMER_SECRET, ACCESS_TOKEN, TOKEN_SECRET);

        // post tweet to twitter from URI using POST
        helper.httpPost(myPostURI);

        // get twitter account info from URI using GET
        helper.httpGet(myGetURI);

    }
}
