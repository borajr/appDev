import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class main {
    public static void main(String[] args) {
        // So far Json objects are used to represent a firebase.
        List<String> firebase = new ArrayList<>();
        firebase.add(
                "{\"name\":\"Bora\",\"age\":21,\"gender\":\"Male\",\"Orientation\":\"Straight\",\"preferredAge\":\"18-30\"}");
        firebase.add(
                "{\"name\":\"Yaren\",\"age\":20,\"gender\":\"Female\",\"Orientation\":\"Straight\",\"preferredAge\":\"18-25\"}");
        firebase.add(
                "{\"name\":\"Erdem\",\"age\":20,\"gender\":\"Male\",\"Orientation\":\"Gay\",\"preferredAge\":\"18-30\"}");
        firebase.add(
                "{\"name\":\"Teoman\",\"age\":21,\"gender\":\"Male\",\"Orientation\":\"Gay\",\"preferredAge\":\"18-30\"}");
        firebase.add(
                "{\"name\":\"Anna\",\"age\":30,\"gender\":\"Female\",\"Orientation\":\"Straight\",\"preferredAge\":\"18-30\"}");


        for (int i = 0; i < firebase.size(); i++) {
            for (int j = i + 1; j < firebase.size(); j++) {
                if (isCompatible(firebase.get(i), firebase.get(j))) {
                    System.out.println("User: " + getUserName(firebase.get(i)) + " is compatible with user: "
                            + getUserName(firebase.get(j)));
                }
            }
        }

    }

    private static String getUserName(String json) {
        return getValueForKey(json, "\"name\":\"", "\",\"age\":");
    }

    private static int getUserAge(String json) {
        return Integer.parseInt(getValueForKey(json, "\"age\":", ",\"gender\":"));
    }

    private static String getUserGender(String json) {
        return getValueForKey(json, "\"gender\":\"", "\",\"preferredGender\":");
    }

    private static String getUserOrientation(String json) {
        return getValueForKey(json, "\"Orientation\":\"", "\",\"preferredAge\":");
    }

    private static String getUserPreferredAge(String json) {
        return getValueForKey(json, "\"preferredAge\":\"", "\"}");
    }

    private static String getValueForKey(String json, String key, String end) {
        String patternString = Pattern.quote(key) + "(.*?)" + Pattern.quote(end);
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(json);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    private static boolean isCompatible(String user1, String user2) {
        String orientation1 = getUserOrientation(user1);
        String orientation2 = getUserOrientation(user2);
        int age1 = getUserAge(user1);
        int age2 = getUserAge(user2);
        String preferredAge1 = getUserPreferredAge(user1);
        String preferredAge2 = getUserPreferredAge(user2);

        if (orientation1.equals(orientation2)) {
            return isAgeInRange(preferredAge1, age2) && isAgeInRange(preferredAge2, age1);
        } else {
            return false;
        }
    }

    private static boolean isAgeInRange(String ageRange, int age) {
        String[] range = ageRange.split("-");
        int minAge = Integer.parseInt(range[0]);
        int maxAge = Integer.parseInt(range[1]);
        return age >= minAge && age <= maxAge;
    }
}
