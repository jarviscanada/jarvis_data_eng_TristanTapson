package src.main.ca.jrvs.apps.twitter.dao;

import com.google.gdata.util.common.base.PercentEscaper;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import src.main.ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import src.main.ca.jrvs.apps.twitter.example.JsonParser;
import src.main.ca.jrvs.apps.twitter.model.Tweet;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

// TODO: clean up code, also try catch blocks
public class TwitterDao implements CrdDao<Tweet, String> {



    // URI constants
    private static final String API_BASE_URI = "https://api.twitter.com";
    private static final String POST_PATH = "/1.1/statuses/update.json";
    private static final String SHOW_PATH = "/1.1/statuses/show.json";
    private static final String DELETE_PATH = "/1.1/statuses/destroy";

    // URI symbols
    private static final String QUERY_SYM = "?";
    private static final String AMPERSAND = "&";
    private static final String EQUAL = "=";

    // Response code
    private static final int HTTP_OK = 200;

    private HttpHelper httpHelper;

    // constructor
    @Autowired
    public TwitterDao(HttpHelper httpHelper){
        this.httpHelper = httpHelper;
    }

    public static void main(String[] args) throws OAuthMessageSignerException, OAuthExpectationFailedException, URISyntaxException, IOException, OAuthCommunicationException {
        System.out.println("--- TwitterDao Class ---");

        Tweet myTweet = new Tweet();
        myTweet.setText("hello!");
        //String uriStr = API_BASE_URI + POST_PATH + QUERY_SYM + "status" + EQUAL + myTweet.getText();
        //System.out.println(uriStr);



    }


    // creates a tweet using the http Post method, info taken from the tweet entity
    // NOTE: gets rid of need of constructing the URI in main, all done here
    @Override
    public Tweet create(Tweet entity) throws URISyntaxException, OAuthMessageSignerException, OAuthExpectationFailedException, IOException, OAuthCommunicationException {

        System.out.println("creating tweet using DAO...");

        String uriStr = API_BASE_URI + POST_PATH + QUERY_SYM
                + "status" + EQUAL + stringFormatter(entity.getText());
        //System.out.println(uriStr);

        URI uri = new URI(uriStr);
        HttpResponse response = httpHelper.httpPost(uri);
        //System.out.println(EntityUtils.toString(response.getEntity()));
        String jsonStr;

        jsonStr = EntityUtils.toString(response.getEntity());
        System.out.println(jsonStr);

        Tweet tweet = JsonParser.toObjectFromJson(jsonStr, Tweet.class);

        //return JsonParser.toObjectFromJson(EntityUtils.toString(response.getEntity()), Tweet.class);

        String json = JsonParser.toJson(tweet,true,true);
        System.out.println(json);

        return tweet;

    }

    @Override
    public Tweet findById(String s) throws URISyntaxException, OAuthMessageSignerException, OAuthExpectationFailedException, IOException, OAuthCommunicationException {
        System.out.println("find tweet by id using DAO...");


        String uriStr = API_BASE_URI + SHOW_PATH + QUERY_SYM + "id" + EQUAL + s;
        //System.out.println(uriStr);

        URI uri = new URI(uriStr);

        HttpResponse response = httpHelper.httpGet(uri);


        Tweet tweet =  JsonParser.toObjectFromJson(EntityUtils.toString(response.getEntity()), Tweet.class);

        String json = JsonParser.toJson(tweet,true,true);
        System.out.println(json);

        return tweet;
    }

    @Override
    public Tweet deleteById(String s) throws URISyntaxException, OAuthMessageSignerException, OAuthExpectationFailedException, IOException, OAuthCommunicationException {

        System.out.println("delete tweet by id using DAO...");

        String uriStr = API_BASE_URI + DELETE_PATH + "/" + s + ".json";
        System.out.println(uriStr);

        URI uri = new URI(uriStr);

        HttpResponse response = httpHelper.httpPost(uri);


        Tweet tweet =  JsonParser.toObjectFromJson(EntityUtils.toString(response.getEntity()), Tweet.class);

        String json = JsonParser.toJson(tweet,true,true);
        System.out.println(json);

        return tweet;

    }

    public static String stringFormatter(String inputStr){
        PercentEscaper percentEscaper = new PercentEscaper("", false);
        String formattedStr = percentEscaper.escape(inputStr);
        return formattedStr;
    }
}
