package io.github.minhhoangvn.utils;

import java.util.HashMap;
import java.util.Map;

public class RatingMapper {
    private static final Map<String, String> mapper = new HashMap<>();

    static {
        // Initialize the mapping
        mapper.put("1", "A");
        mapper.put("2", "B");
        mapper.put("3", "C");
        mapper.put("4", "D");
        mapper.put("NO_VALUE", "0"); // Handle metric with no value
    }

    public static String getRating(String inputKey) {
        return mapper.getOrDefault(inputKey, inputKey);
    }
}
