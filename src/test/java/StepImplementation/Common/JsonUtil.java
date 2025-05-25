package StepImplementation.Common;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonUtil {

    public static String parseJsonByKey(String json, String key) {
        json = json.trim();

        try {
            // Trường hợp JSON là một object
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                if (!jsonObject.has(key)) {
                    throw new RuntimeException("Không tìm thấy key: " + key + " trong JSON object.");
                }
                return jsonObject.get(key).toString();
            }

            // Trường hợp JSON là một mảng
            else if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                JSONArray resultArray = new JSONArray();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    if (obj.has(key)) {
                        resultArray.put(obj.get(key));
                    } else {
                        resultArray.put(JSONObject.NULL); // hoặc bỏ qua
                    }
                }

                return resultArray.toString();
            }

            // Không phải JSON hợp lệ
            else {
                throw new RuntimeException("Không phải định dạng JSON hợp lệ.");
            }

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi parse JSON: " + e.getMessage(), e);
        }
    }
}
