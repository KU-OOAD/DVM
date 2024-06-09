package util;
import java.util.HashMap;
import java.util.Map;

public class JsonParser {

    public Map<String, Object> parse(String json) {
        return parseJsonObject(json.trim());
    }

    private Map<String, Object> parseJsonObject(String json) {
        Map<String, Object> result = new HashMap<>();

        if (json.startsWith("{") && json.endsWith("}")) {
            json = json.substring(1, json.length() - 1).trim();

            int braceDepth = 0;
            StringBuilder currentPair = new StringBuilder();
            for (char ch : json.toCharArray()) {
                if (ch == '{') braceDepth++;
                if (ch == '}') braceDepth--;

                if (ch == ',' && braceDepth == 0) {
                    processKeyValuePair(result, currentPair.toString().trim());
                    currentPair.setLength(0);
                } else {
                    currentPair.append(ch);
                }
            }
            processKeyValuePair(result, currentPair.toString().trim());
        }

        return result;
    }

    private void processKeyValuePair(Map<String, Object> result, String pair) {
        String[] keyValue = pair.split(":", 2);
        if (keyValue.length < 2) return;

        String key = keyValue[0].trim().replace("\"", "");
        String value = keyValue[1].trim();

        if (value.startsWith("\"") && value.endsWith("\"")) {
            result.put(key, value.substring(1, value.length() - 1));
        } else if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
            result.put(key, Boolean.parseBoolean(value));
        } else if (value.startsWith("{") && value.endsWith("}")) {
            result.put(key, parseJsonObject(value)); // 중첩된 JSON 객체 파싱
        } else if (value.contains(".")) {
            try {
                result.put(key, Double.parseDouble(value));
            } catch (NumberFormatException e) {
                result.put(key, value);
            }
        } else {
            try {
                result.put(key, Integer.parseInt(value));
            } catch (NumberFormatException e) {
                result.put(key, value);
            }
        }
    }

}
