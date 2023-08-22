package io.github.minhhoangvn.utils;

public class StringUtils {

    private StringUtils() {}

    public static String convertSnakeToTitle(String snakeCase) {
        StringBuilder titleCase = new StringBuilder();
        boolean capitalizeNext = true;

        for (char c : snakeCase.toCharArray()) {
            if (c == '_') {
                titleCase.append(' ');
                capitalizeNext = true;
            } else {
                if (capitalizeNext) {
                    titleCase.append(Character.toUpperCase(c));
                    capitalizeNext = false;
                } else {
                    titleCase.append(c);
                }
            }
        }

        return titleCase.toString();
    }
}
