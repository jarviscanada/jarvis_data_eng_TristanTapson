package src.main.ca.jrvs.apps.twitter.dao.helper;


import java.io.IOException;
import java.net.URI;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;

public interface HttpHelper {

    /**
     * Execute a HTTP Post call (post tweet to account by status text)
     * @param uri
     * @return
     */
    HttpResponse httpPost(URI uri) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, IOException;

    /**
     * Execute a HTTP Get call (get tweet info by id)
     * @param uri
     * @return
     */
    HttpResponse httpGet(URI uri) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, IOException;
}