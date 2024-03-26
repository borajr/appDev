import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class algo {
    public static void main(String[] args) {
        // So far Json objects are used to represent a firebase.
        List<String> firebase = new ArrayList<>();
        firebase.add(
                "{\"name\":\"Bora\",\"age\":21,\"gender\":\"Male\",\"Preferred Gender\":\"Female\",\"preferredAge\":\"18-25\",\"starsign\":\"Aries\",\"starSignArray\":[\"Gemini\",\"Aquarius\"]}");
        firebase.add(
                "{\"name\":\"Yaren\",\"age\":20,\"gender\":\"Female\",\"Preferred Gender\":\"Male\",\"preferredAge\":\"18-25\",\"starsign\":\"Aquarius\",\"starSignArray\":[\"Pisces\",\"Aries\"]}");
        firebase.add(
                "{\"name\":\"Marko\",\"age\":20,\"gender\":\"Male\",\"Preferred Gender\":\"Male\",\"preferredAge\":\"18-30\",\"starsign\":\"Taurus\",\"starSignArray\":[\"Virgo\",\"Pisces\"]}");
        firebase.add(
                "{\"name\":\"Teoman\",\"age\":21,\"gender\":\"Male\",\"Preferred Gender\":\"Male\",\"preferredAge\":\"18-30\",\"starsign\":\"Cancer\",\"starSignArray\":[\"Leo\",\"Scorpio\"]}");
        firebase.add(
                "{\"name\":\"Anna\",\"age\":30,\"gender\":\"Female\",\"Preferred Gender\":\"Male\",\"preferredAge\":\"18-30\",\"starsign\":\"Pisces\",\"starSignArray\":[\"Aries\",\"Taurus\"]}");

        for (int i = 0; i < firebase.size(); i++) {
            for (int j = i + 1; j < firebase.size(); j++) {
                int compatibilityScore = isCompatible(firebase.get(i), firebase.get(j));
                System.out.println("Compatibility between " + getUserName(firebase.get(i)) + " and "
                        + getUserName(firebase.get(j)) + ": " + compatibilityScore);
                if (compatibilityScore > 50) {
                    System.out.println("User: " + getUserName(firebase.get(i)) + " is very compatible with user: "
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
        return getValueForKey(json, "\"gender\":\"", "\",\"Preferred Gender\":");
    }

    private static String getPrefferedGender(String json) {
        return getValueForKey(json, "\"Preferred Gender\":\"", "\",\"preferredAge\":");
    }

    private static String getUserPreferredAge(String json) {
        return getValueForKey(json, "\"preferredAge\":\"", "\",\"starsign\":");
    }

    private static String getUserStarSign(String json) {
        return getValueForKey(json, "\"starsign\":\"", "\"}");
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

    private static List<String> getUserStarSignArray(String json) {
        String starSignArray = getValueForKey(json, "\"starSignArray\":\\[", "\\]");
        List<String> starSigns = new ArrayList<>();
        if (starSignArray != null && !starSignArray.isEmpty()) {
            String[] signs = starSignArray.split(",");
            for (String sign : signs) {
                starSigns.add(sign.trim().replaceAll("\"", ""));
            }
        }
        return starSigns;
    }

    private static boolean isStarSignInArray(String starSign, List<String> starSignArray) {
        return starSignArray.contains(starSign);
    }

    private static int isCompatible(String user1, String user2) {
        String prefferedGender1 = getPrefferedGender(user1);
        String preferredGender2 = getPrefferedGender(user2);
        int age1 = getUserAge(user1);
        int age2 = getUserAge(user2);
        String preferredAge1 = getUserPreferredAge(user1);
        String preferredAge2 = getUserPreferredAge(user2);
        String gender1 = getUserGender(user1);
        String gender2 = getUserGender(user2);
        String sign1 = getUserStarSign(user1);
        String sign2 = getUserStarSign(user2);
        List<String> signArray1 = getUserStarSignArray(user1);
        List<String> signArray2 = getUserStarSignArray(user2);
        int compatibilityCounter = 50;

        if (prefferedGender1.equals(gender2) && preferredGender2.equals(gender1)) {
            compatibilityCounter += 10;
            if (isAgeInRange(preferredAge1, age2) && isAgeInRange(preferredAge2, age1)) {
                compatibilityCounter += 10;
                if (isStarSignInArray(sign1, signArray2) && isStarSignInArray(sign2, signArray1)) {
                    compatibilityCounter += 10;
                } else {
                    return compatibilityCounter - 10;
                }
            } else {
                return compatibilityCounter - 10;
            }
        } else {
            return compatibilityCounter - 50;
        }
        return compatibilityCounter;
    }

    private static boolean isAgeInRange(String ageRange, int age) {
        String[] range = ageRange.split("-");
        int minAge = Integer.parseInt(range[0]);
        int maxAge = Integer.parseInt(range[1]);
        return age >= minAge && age <= maxAge;
    }
}
