package src.main.ca.jrvs.apps.twitter.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Entities {

    @JsonProperty("hashtags")
    private Hashtag[] hashtags;

    @JsonProperty("user_mentions")
    private UserMention[] user_mentions;

    // getters and setters

    public UserMention[] getUser_mentions() {
        return user_mentions;
    }

    public void setUser_mentions(UserMention[] user_mentions) {
        this.user_mentions = user_mentions;
    }

    public Hashtag[] getHashtags() {
        return hashtags;
    }

    public void setHashtags(Hashtag[] hashtags) {
        this.hashtags = hashtags;
    }
}
