package src.main.ca.jrvs.apps.twitter.dao.helper;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;

@Component
public class TwitterHttpHelper implements HttpHelper{

    /**
     * Dependencies are specified as private member variables
     */
    private OAuthConsumer consumer;
    private HttpClient httpClient;

    /**
     * Constructor
     * Setup dependencies using secrets
     *
     * @param consumerKey
     * @param consumerSecret
     * @param accessToken
     * @param tokenSecret
     */
    public TwitterHttpHelper(String consumerKey, String consumerSecret,
                             String accessToken, String tokenSecret){
        // set up OAuth
        consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
        consumer.setTokenWithSecret(accessToken, tokenSecret);

        // default single connection
        httpClient = new DefaultHttpClient();
    }

    @Override
    public HttpResponse httpPost(URI uri) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, IOException {

        // create an http POST request, then sign and send the request
        HttpPost postRequest = new HttpPost(uri);
        consumer.sign(postRequest);
        httpClient = HttpClientBuilder.create().build();

        // try-catch block for request execution
        try {
            return httpClient.execute(postRequest);
        } catch (IOException ex){
            throw new RuntimeException("Failed to execute POST request", ex);
        }

    }

    @Override
    public HttpResponse httpGet(URI uri) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, IOException {

        // create an http GET request, then sign and send the request
        HttpGet getRequest = new HttpGet(uri);
        consumer.sign(getRequest);
        httpClient = HttpClientBuilder.create().build();

        // try-catch block for request execution
        try {
            return httpClient.execute(getRequest);
        } catch (IOException ex){
            throw new RuntimeException("Failed to execute GET request", ex);
        }
    }
}