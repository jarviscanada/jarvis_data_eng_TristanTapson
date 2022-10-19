package ca.jrvs.apps.trading.example;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;

public class JsonParser {

    /**
     * Converts a java object to JSON string
     *
     * @param object            input object
     * @param prettyJson        bool to check if json formatted nicely
     * @param includeNullValues bool to check for null values
     * @return JSON String
     * @throws JsonProcessingException
     */
    public static String toJson(Object object, boolean prettyJson, boolean includeNullValues) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        if (!includeNullValues) {
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }
        if (prettyJson) {
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
        }
        return mapper.writeValueAsString(object);
    }

    /**
     * Parse a JSON string to an object
     *
     * @param json  JSON string
     * @param clazz object class
     * @param <T>   Type
     * @return Object
     * @throws IOException
     */
    public static <T> T toObjectFromJson(String json, Class clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return (T) mapper.readValue(json, clazz);
    }
}
