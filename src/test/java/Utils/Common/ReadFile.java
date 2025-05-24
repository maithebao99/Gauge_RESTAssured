package Utils.Common;

import io.cucumber.guice.ScenarioScoped;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

@ScenarioScoped
public class ReadFile {

    //Read json from Json Object to String
    public static String readFileToString(File file, Charset charset) throws IOException {
        return new String(Files.readAllBytes(file.toPath()), charset);
    }


    //Read json from Json Object to Object
    public static JSONObject readFileToJsonObject(File file, Charset charset) throws IOException {
        // Đọc tất cả byte từ file và chuyển đổi thành String
        String content = new String(Files.readAllBytes(file.toPath()), charset);

        // Chuyển String thành JSONObject và trả về
        return new JSONObject(content);
    }
}
