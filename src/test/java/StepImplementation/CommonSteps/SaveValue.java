package StepImplementation.CommonSteps;

import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.ScenarioDataStore;
import com.thoughtworks.gauge.datastore.SpecDataStore;
import com.thoughtworks.gauge.datastore.SuiteDataStore;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;

public class SaveValue {

    @Step("Lưu <value> vào biến <fullKey>")
    public static void saveValueToKey(Object value, String fullKey) {
        String[] parts = fullKey.split(":", 2);

        if (parts.length != 2) {
            throw new IllegalArgumentException("Key không hợp lệ: " + fullKey + ". Phải có format dạng 'scope:key'.");
        }

        String scope = parts[0].toLowerCase();
        String key = parts[1];

        switch (scope) {
            case "scenario":
                ScenarioDataStore.put(key, value);
                break;
            case "spec":
                SpecDataStore.put(key, value);
                break;
            case "suite":
                SuiteDataStore.put(key, value);
                break;
            default:
                throw new IllegalArgumentException("Scope không hợp lệ: " + scope + ". Chỉ hỗ trợ: scenario, spec, suite.");
        }

        System.out.println("Đã lưu '" + key + "' = '" + value + "' vào " + scope + " store");
    }


    // Parser value from response and save to variable
    public static void extractKeyAndSave(Response response, String keyToFind, String fullKey) {
        String body = response.getBody().asString();
        JSONObject json = new JSONObject(body);

        Object value = findKeyRecursive(json, keyToFind);
        if (value == null) {
            throw new RuntimeException("Không tìm thấy '" + keyToFind + "' trong response");
        }

        String[] parts = fullKey.split(":", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Key không hợp lệ: " + fullKey);
        }

        String scope = parts[0].toLowerCase();
        String key = parts[1];

        switch (scope) {
            case "scenario":
                ScenarioDataStore.put(key, value);
                break;
            case "spec":
                SpecDataStore.put(key, value);
                break;
            case "suite":
                SuiteDataStore.put(key, value);
                break;
            default:
                throw new IllegalArgumentException("Scope không hợp lệ: " + scope);
        }

        System.out.println("Đã lưu '" + keyToFind + "' = '" + value + "' vào " + scope + " store với key '" + key + "'");
    }

    //Handle parser value from json contain json object and json array
    private static Object findKeyRecursive(Object node, String keyToFind) {
        if (node instanceof JSONObject jsonObj) {
            for (String key : jsonObj.keySet()) {
                Object value = jsonObj.get(key);
                if (keyToFind.equalsIgnoreCase(key)) {
                    return value;
                }
                Object result = findKeyRecursive(value, keyToFind);
                if (result != null) return result;
            }
        } else if (node instanceof JSONArray jsonArray) {
            for (int i = 0; i < jsonArray.length(); i++) {
                Object result = findKeyRecursive(jsonArray.get(i), keyToFind);
                if (result != null) return result;
            }
        }
        return null;
    }

}
