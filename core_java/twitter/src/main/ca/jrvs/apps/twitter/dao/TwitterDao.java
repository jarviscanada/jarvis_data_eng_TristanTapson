package ca.jrvs.apps.twitter.dao;

import com.google.gdata.util.common.base.PercentEscaper;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.example.JsonParser;
import ca.jrvs.apps.twitter.model.Tweet;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@org.springframework.stereotype.Repository
public class TwitterDao implements CrdDao<Tweet, String> {

    // URI constants
    private static final String API_BASE_URI = "https://api.twitter.com";
    private static final String POST_PATH = "/1.1/statuses/update.json";
    private static final String SHOW_PATH = "/1.1/statuses/show.json";
    private static final String DELETE_PATH = "/1.1/statuses/destroy";

    // URI keywords
    private static final String ID = "id";
    private static final String STATUS = "status";
    private static final String DOT_JSON = ".json";

    // URI symbols
    private static final String QUERY_SYM = "?";
    private static final String AMPERSAND = "&";
    private static final String EQUAL = "=";
    private static final String SLASH = "/";

    // Response code
    private static final int HTTP_OK = 200;

    private HttpHelper httpHelper;

    // constructor
    @Autowired
    public TwitterDao(HttpHelper httpHelper){
        this.httpHelper = httpHelper;
    }

    @Override
    public Tweet create(Tweet entity) throws URISyntaxException, OAuthMessageSignerException, OAuthExpectationFailedException, IOException, OAuthCommunicationException {

        // POST (create) uri formatting
        String uriStr = API_BASE_URI + POST_PATH + QUERY_SYM
                + STATUS + EQUAL + stringFormatter(entity.getText());
        System.out.println("POST (update) request uri: " + uriStr);
        URI uri = new URI(uriStr);

        // post tweet using httpHelper class
        HttpResponse response = httpHelper.httpPost(uri);

        // posted tweet object
        try {
            return tweetFormatter(response);
        } catch (IOException ex){
            throw new RuntimeException("Unable to convert JSON str to tweet object", ex);
        }
    }

    @Override
    public Tweet findById(String s) throws URISyntaxException, OAuthMessageSignerException, OAuthExpectationFailedException, IOException, OAuthCommunicationException {

        // GET uri formatting
        String uriStr = API_BASE_URI + SHOW_PATH + QUERY_SYM + ID + EQUAL + s;
        System.out.println("GET (show) request uri: " + uriStr);
        URI uri = new URI(uriStr);

        // get tweet using httpHelper class
        HttpResponse response = httpHelper.httpGet(uri);

        // retrieved tweet object
        try {
            return tweetFormatter(response);
        } catch (IOException ex){
            throw new RuntimeException("Unable to convert JSON str to tweet object", ex);
        }

    }

    @Override
    public Tweet deleteById(String s) throws URISyntaxException, OAuthMessageSignerException, OAuthExpectationFailedException, IOException, OAuthCommunicationException {

        // POST (destroy) uri formatting
        String uriStr = API_BASE_URI + DELETE_PATH + SLASH + s + DOT_JSON;
        System.out.println("POST (destroy) request uri: " + uriStr);
        URI uri = new URI(uriStr);

        // delete tweet using httpHelper class
        HttpResponse response = httpHelper.httpPost(uri);

        // deleted tweet object
        try {
            return tweetFormatter(response);
        } catch (IOException ex){
            throw new RuntimeException("Unable to convert JSON str to tweet object", ex);
        }
    }

    // formats status string into safe uri characters (optional handling of the space char)
    public static String stringFormatter(String inputStr){
        PercentEscaper percentEscaper = new PercentEscaper("", false);
        String formattedStr = percentEscaper.escape(inputStr);
        return formattedStr;
    }


    // creates tweet object from json, and prints the tweet object as pretty json
    public Tweet tweetFormatter(HttpResponse response) throws IOException {

        // TODO: delete print statements?
        if(response.getEntity() == null){
            throw new RuntimeException("Empty response");
        }

        // request headers as unformatted json string
        String jsonStr = EntityUtils.toString(response.getEntity());
        // System.out.println("JSON: " + jsonStr);

        // create tweet object from json, and print the pretty json string
        Tweet tweet = JsonParser.toObjectFromJson(jsonStr, Tweet.class);
        String prettyJson = JsonParser.toJson(tweet,true,true);
        // System.out.println("Pretty JSON: " + prettyJson);

        return tweet;
    }
}
