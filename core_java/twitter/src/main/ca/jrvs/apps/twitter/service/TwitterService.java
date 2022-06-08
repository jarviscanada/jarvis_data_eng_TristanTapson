package src.main.ca.jrvs.apps.twitter.service;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.springframework.beans.factory.annotation.Autowired;
import src.main.ca.jrvs.apps.twitter.dao.CrdDao;
import src.main.ca.jrvs.apps.twitter.model.Tweet;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TwitterService implements Service {

    private CrdDao dao;

    // constructor
    @Autowired
    public TwitterService(CrdDao dao){ this.dao = dao; }

    @Override
    public Tweet postTweet(Tweet tweet) throws OAuthMessageSignerException, OAuthExpectationFailedException, URISyntaxException, IOException, OAuthCommunicationException {

        validatePostTweet(tweet);
        return (Tweet) dao.create(tweet);
    }

    // validate tweet fields (text exceeds 140 chars, lon/lat out of range)
    private void validatePostTweet(Tweet tweet) throws RuntimeException{

        String text = tweet.getText();
        Float longitude = tweet.getCoordinates().getCoordinates().get(0);
        Float latitude = tweet.getCoordinates().getCoordinates().get(1);

        // check tweet text length
        System.out.println("Attempting to validate tweet using Service...");
        if (text.length() > 140) {
            throw new RuntimeException("Tweet length exceeds 140 characters");
        }

        // check long/lat range
        if(!(latitude >= -90 && latitude <= 90
                && longitude >= -180 && longitude <= 180)){
            throw new RuntimeException("Tweet coordinate(s) range invalid");
        }
    }

    @Override
    public Tweet showTweet(String id, String[] fields) throws OAuthMessageSignerException, OAuthExpectationFailedException, URISyntaxException, IOException, OAuthCommunicationException {

        validateShowTweet(id, fields);

        // TODO: fields? 
        // List<String> fieldsList = Arrays.asList(fields);
        // Tweet myTweet = (Tweet) dao.findById(id);
        
        return (Tweet) dao.findById(id);
    }

    private void validateShowTweet(String id, String[] fields) throws RuntimeException{

        // check that tweet id contains only numbers from 0-9, and has at least one digit
        if(!(validID(id))){
            throw new RuntimeException("Tweet id contains non-numeric character(s)");
        }

        // check if specified field is valid field in simplified tweet object
        List<String> validFields = Stream.of("created_at", "id", "id_str",
                "text", "entities", "hastags", "user_mentions", "coordinates",
                "retweet_count", "favorite_count", "favorited", "retweeted")
                .collect(Collectors.toList());

        for(String field : fields){
            if(!(validFields.contains(field))){
                throw new RuntimeException("Invalid tweet field specified");
            }
        }
    }

    @Override
    public List<Tweet> deleteTweets(String[] ids) throws OAuthMessageSignerException, OAuthExpectationFailedException, URISyntaxException, IOException, OAuthCommunicationException {

        validateDeleteTweets(ids);
        List<Tweet> idList = new ArrayList<>();

        for(String id : ids){
            idList.add((Tweet) dao.deleteById(id));
        }

        return idList;
    }

    private void validateDeleteTweets(String[] ids) throws RuntimeException {

        // iterate through the list of ids
        for(String id : ids) {
            // check that tweet id contains only numbers from 0-9, and has at least one digit
            if (!(validID(id))) {
                throw new RuntimeException("Tweet id contains non-numeric character(s)");
            }
        }
    }

    // regex for valid tweet id
    public boolean validID(String line){
        if(Pattern.matches("^([0-9]{1,})$", line)){
            return true;
        }
        return false;
    }
}
