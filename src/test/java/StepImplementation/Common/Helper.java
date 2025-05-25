package StepImplementation.Common;

import com.thoughtworks.gauge.Step;
import io.restassured.response.Response;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class Helper {


    public static String loadRequestBodyToString(String fileURL, String bodyCase) throws IOException {
        File bodyFile = new File(fileURL);
        JSONObject allBodies = ReadFile.readFileToJsonObject(bodyFile, Charset.defaultCharset());
        return allBodies.getJSONObject(bodyCase).toString();
    }

    @Step("Lấy json object của key <bodyCase> từ file <fileURL> và lưu vào key <fullKey>")
    public static String loadRequestBodyToStringAndSave(String bodyCase, String fileURL, String fullKey) throws IOException {
        File bodyFile = new File(fileURL);
        JSONObject allBodies = ReadFile.readFileToJsonObject(bodyFile, Charset.defaultCharset());
        String body = allBodies.getJSONObject(bodyCase).toString();
        SaveValueToDataStore.saveValueToKey(body, fullKey);
        return body;
    }

    public static JSONObject loadRequestBodyToJsonObject(String fileURL, String bodyCase) throws IOException {
        File bodyFile = new File(fileURL);
        JSONObject allBodies = ReadFile.readFileToJsonObject(bodyFile, Charset.defaultCharset());
        return allBodies.getJSONObject(bodyCase);
    }

}
