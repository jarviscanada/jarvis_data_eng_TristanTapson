package src.main.ca.jrvs.apps.twitter.dao.helper;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;

/** TODO: make the get/post request modular b/c they are similar
 *          also include a try/catch in the http get/post functions after modularizing
 */
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
        consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
        consumer.setTokenWithSecret(accessToken, tokenSecret);

        // default single connection
        httpClient = new DefaultHttpClient();
    }

    @Override
    public HttpResponse httpPost(URI uri) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, IOException {

        HttpPost postRequest = new HttpPost(uri);
        consumer.sign(postRequest);
        HttpResponse response = httpClient.execute(postRequest);
        System.out.println(EntityUtils.toString(response.getEntity()));
        return response;

    }

    @Override
    public HttpResponse httpGet(URI uri) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, IOException {

        HttpGet getRequest = new HttpGet(uri);
        consumer.sign(getRequest);
        HttpResponse response = httpClient.execute(getRequest);
        System.out.println(EntityUtils.toString(response.getEntity()));
        return response;
    }
}
