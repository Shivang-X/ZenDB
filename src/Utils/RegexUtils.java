package src.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {
    public Matcher getMatcher(String input, String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        return pattern.matcher(input);
    }
}
