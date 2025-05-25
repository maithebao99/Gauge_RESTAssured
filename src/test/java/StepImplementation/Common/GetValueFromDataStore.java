package StepImplementation.Common;

import com.thoughtworks.gauge.datastore.ScenarioDataStore;
import com.thoughtworks.gauge.datastore.SpecDataStore;
import com.thoughtworks.gauge.datastore.SuiteDataStore;

public class GetValueFromDataStore {

    public static Object getValueFromKey(String fullKey) {
        String[] parts = fullKey.split(":", 2);

        if (parts.length != 2) {
            throw new IllegalArgumentException("Key không hợp lệ: " + fullKey + ". Phải có format dạng 'scope:key'.");
        }

        String scope = parts[0].toLowerCase();
        String key = parts[1];

        Object value;

        switch (scope) {
            case "scenario":
                value = ScenarioDataStore.get(key);
                break;
            case "spec":
                value = SpecDataStore.get(key);
                break;
            case "suite":
                value = SuiteDataStore.get(key);
                break;
            default:
                throw new IllegalArgumentException("Scope không hợp lệ: " + scope + ". Chỉ hỗ trợ: scenario, spec, suite.");
        }

        if (value == null) {
            throw new IllegalStateException("Không tìm thấy giá trị với key: '" + key + "' trong scope: " + scope);
        }

        System.out.println("Đã đọc '" + key + "' từ " + scope + " store: " + value);
        return value;
    }
}
