package ca.jrvs.apps.twitter;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;

import ca.jrvs.apps.twitter.controller.Controller;
import ca.jrvs.apps.twitter.controller.TwitterController;
import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.example.JsonParser;
import ca.jrvs.apps.twitter.model.*;
import ca.jrvs.apps.twitter.service.Service;
import ca.jrvs.apps.twitter.service.TwitterService;

import java.util.*;

@org.springframework.stereotype.Component
public class TwitterCLIApp {

    private Controller controller;

    // constructor
    @Autowired
    public TwitterCLIApp(Controller controller){ this.controller = controller; }

    private static String CONSUMER_KEY = System.getenv("consumerKey");
    private static String CONSUMER_SECRET = System.getenv("consumerSecret");
    private static String ACCESS_TOKEN = System.getenv("accessToken");
    private static String TOKEN_SECRET = System.getenv("tokenSecret");

    private static String POST_USAGE =
            "Usage:\n" +
                    "TwitterApp \"post\" \"tweet_text\" \"latitude:longitude\"\n" +
                    "\n" +
                    "Arguments:\n" +
                    "tweet_text         - tweet_text cannot exceed 140 UTF-8 encoded characters. \n" +
                    "latitude:longitude - Geo location.\n";

    private static String SHOW_USAGE =
            "Usage:\n" +
                    "TwitterApp show tweet_id [field1,field2,..]\n" +
                    "\n" +
                    "Arguments:\n" +
                    "tweet_id  - Tweet ID. Same as id_str in the tweet object.\n" +
                    "[field1,field2,..]  - A comma-separated list of top-level fields from the tweet object.\n";

    private static String DELETE_USAGE =
            "Usage: TwitterApp delete [id1,id2,..]\n" +
                    "\n" +
                    "Arguments:\n" +
                    "tweet_ids - A comma-separated list of tweets.\n";

    private static String ARGUMENT_USAGE = "Usage:\n" +
            "TwitterApp post|show|delete [options]";

    public static void main(String[] args) throws Exception {

        HttpHelper helper = new TwitterHttpHelper(CONSUMER_KEY,
                CONSUMER_SECRET, ACCESS_TOKEN, TOKEN_SECRET);
        CrdDao dao = new TwitterDao(helper);
        Service service = new TwitterService(dao);
        Controller controller = new TwitterController(service);
        TwitterCLIApp twitterCLIApp = new TwitterCLIApp(controller);

        // run the TwitterCLI app
        twitterCLIApp.run(args);

    }

    public void run(String[] args) throws Exception {

        if(args.length == 0){
            throw new IllegalArgumentException(ARGUMENT_USAGE);
        }

        // cases, method type as first argument
        String method = args[0];
        switch(method){

            // post tweet using TwitterController
            case "post":
                Tweet postTweet = controller.postTweet(args);
                printEnvironment();
                System.out.println(POST_USAGE);
                printArgs(args);
                printPrettyJson(postTweet);
                break;

            // show tweet using TwitterController
            case "show":
                Tweet showTweet = controller.showTweet(args);
                printEnvironment();
                System.out.println(SHOW_USAGE);
                printArgs(args);
                printPrettyJson(showTweet);
                break;

            // delete tweets using TwitterController
            case "delete":
                List<Tweet> deletedTweets = controller.deleteTweet(args);
                printEnvironment();
                System.out.println(DELETE_USAGE);
                printArgs(args);
                for (Tweet deletedTweet : deletedTweets) {
                    printPrettyJson(deletedTweet);
                }
                break;

            default:
                throw new IllegalArgumentException(ARGUMENT_USAGE);
        }
    }

    // print environment variables
    public void printEnvironment(){
        System.out.println("\n--- TwitterCLIApp Class ---");
    }

    // print command line args
    public void printArgs(String[] args){
        System.out.print("TwitterApp");
        for(String argument : args){
            System.out.print(" " + argument);
        }
        System.out.println("\n\nJSON Output:");
    }

    // print tweet as pretty json
    public void printPrettyJson(Tweet tweet) throws JsonProcessingException {
        String jsonString = JsonParser.toJson(tweet, true, false);
        System.out.println(jsonString);
    }
}