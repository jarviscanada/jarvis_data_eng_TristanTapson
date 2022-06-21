package ca.jrvs.apps.twitter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import ca.jrvs.apps.twitter.model.*;
import ca.jrvs.apps.twitter.service.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@org.springframework.stereotype.Controller
public class TwitterController implements Controller{

    private static final String COORD_SEP = ":";
    private static final String COMMA = ",";

    private Service service;

    // constructor
    @Autowired
    public TwitterController(Service service){ this.service = service; }

    @Override
    public Tweet postTweet(String[] args) throws Exception {

        // incorrect number of arguments
        if(args.length != 3){
            throw new IllegalArgumentException("usage: post text latitude:longitude");
        }

        // method arguments
        String methodType = args[0];
        String tweetText = args[1];
        String latlong = args[2];

        // add the coordinates to a list using string split and lambda expressions (try-catch this)
        List<Float> coords = new ArrayList<>();
        List<String> tempCoords = Stream.of(latlong.split(COORD_SEP))
                .collect(Collectors.toList());

        // long/lat strings valid?
        if(validFloat(tempCoords.get(0))) {
            tempCoords.forEach(str -> coords.add(Float.parseFloat(str)));
        }

        // set tweet text
        Tweet myTweet = new Tweet();
        myTweet.setText(tweetText);

        // set tweet coordinates
        Coordinates coord = new Coordinates();
        coord.setCoordinates(coords);
        coord.setType("Point");
        myTweet.setCoordinates(coord);

        // create tweet using Service class
        Tweet formattedTweet = service.postTweet(myTweet);
        formattedTweet.setCoordinates(myTweet.getCoordinates());
        formattedTweet.setText(myTweet.getText());

        return formattedTweet;
    }


    @Override
    public Tweet showTweet(String[] args) throws Exception {

        // incorrect number of arguments
        if( args.length > 3 || args.length < 2){
            throw new IllegalArgumentException("usage: show id [field1, field2...]");
        }

        // method arguments
        String methodType = args[0];
        String tweetId = args[1];

        Tweet myTweet = new Tweet();

        // optional argument included
        if(args.length == 3) {
            String fieldsStr = args[2];

            List<String> tempFields = Stream.of(fieldsStr.split(COMMA))
                    .collect(Collectors.toList());

            String[] fields = tempFields.stream().toArray(size -> new String[size]);

            // create tweet using Service class
            Tweet formattedTweet = service.showTweet(tweetId, fields);

            for (String fldStr : fields) {
                handleFieldCases(fldStr, myTweet, formattedTweet);
            }

            return myTweet;
        }

        // optional argument excluded
        else {
            String[] emptyList = {};
            Tweet formattedTweet = service.showTweet(tweetId, emptyList);
            return formattedTweet;
        }
    }

    @Override
    public List<Tweet> deleteTweet(String[] args) throws Exception {

        // incorrect number of arguments
        if( args.length != 2){
            throw new IllegalArgumentException("usage: delete id1,id2...");
        }

        // method arguments
        String methodType = args[0];
        String deleteIdsStr = args[1];

        List<String> tempFields = Stream.of(deleteIdsStr.split(COMMA))
                .collect(Collectors.toList());

        String[] ids = tempFields.stream().toArray(size -> new String[size]);

        List<Tweet> deletedTweets = service.deleteTweets(ids);
        return deletedTweets;
    }

    // valid float
    public boolean validFloat(String floatStr) throws RuntimeException {
        try {
            Float.parseFloat(floatStr);
            return true;
        } catch (Exception ex) {
            throw new RuntimeException("Tweet coordinate(s) contains non-numeric characters", ex);
        }
    }

    // handles printing specified fields
    public Tweet handleFieldCases(String fieldString, Tweet myTweet, Tweet formattedTweet){

        String fldStr = fieldString;

        switch (fldStr) {
            case "created_at":
                myTweet.setCreated_at(formattedTweet.getCreated_at());
                break;
            case "id":
                myTweet.setId(formattedTweet.getId());
                break;
            case "id_str":
                myTweet.setId_str(formattedTweet.getId_str());
                break;
            case "text":
                myTweet.setText(formattedTweet.getText());
                break;
            case "entities":
                myTweet.setEntities(formattedTweet.getEntities());
                break;
            case "coordinates":
                myTweet.setCoordinates(formattedTweet.getCoordinates());
                break;
            case "retweet_count":
                myTweet.setRetweet_count(formattedTweet.getRetweet_count());
                break;
            case "favorite_count":
                myTweet.setFavorite_count(formattedTweet.getFavorite_count());
                break;
            case "favorited":
                myTweet.setFavorited(formattedTweet.getFavorited());
                break;
            case "retweeted":
                myTweet.setRetweeted(formattedTweet.getRetweeted());
                break;
        }

        return myTweet;
    }
}
