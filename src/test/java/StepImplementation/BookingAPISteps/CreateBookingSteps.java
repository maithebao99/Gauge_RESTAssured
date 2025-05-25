package StepImplementation.BookingAPISteps;

import StepImplementation.Common.GetValueFromDataStore;
import StepImplementation.Common.JsonUtil;
import StepImplementation.Common.ReadFile;
import StepImplementation.Common.SaveValueToDataStore;
import Utils.API.BookingAPI.CreateBookingAPI;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.Table;
import com.thoughtworks.gauge.TableRow;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import static io.restassured.RestAssured.given;

public class CreateBookingSteps {

    CreateBookingAPI createBookingAPI = new CreateBookingAPI();

    Response responseCreateBooking;

    @Step("Call api tạo mới booking")
    public void sendCreateBookingAPI() throws IOException {
        responseCreateBooking = given().spec(createBookingAPI.getRequestCreateBooking()).post();
        System.out.println(responseCreateBooking.asPrettyString());
        Assert.assertEquals(responseCreateBooking.statusCode(), 200);
    }

    @Step("Verify response sau khi tạo mới booking thành công với json body được lưu ở key <fullkey>")
    public void verifyResponseCreateBooking(String fullkey)
    {
        String responseString = responseCreateBooking.asString();
        String valueBooking = JsonUtil.parseJsonByKey(responseString, "booking");
        JSONObject bookingJson = new JSONObject(valueBooking);

        Object body = GetValueFromDataStore.getValueFromKey(fullkey);
        String bodyString = body.toString();
        JSONObject expectedBody = new JSONObject(bodyString);

        JSONAssert.assertEquals(expectedBody, bookingJson, false);
    }


    @Step("Lấy <key> từ response tạo mới booking và lưu vào biến <fullKey>")
    public void extractKeyFromResponseAndStore(String key, String fullKey) {
        SaveValueToDataStore.extractKeyAndSave(responseCreateBooking, key, fullKey);
    }

    @Step("Với từng trường hợp trong bảng, thực hiện tạo booking và kiểm tra response <table>")
    public void createBookingWithMultipleBodies(Table table) throws Exception {
        for (TableRow row : table.getTableRows()) {
            String bodyCase = row.getCell("bodyCase");
            int expectedStatus = Integer.parseInt(row.getCell("expectedStatus"));
            boolean compareResponse = Boolean.parseBoolean(row.getCell("compareResponse"));

            // Đọc JSON body từ file tương ứng với bodyCase
            final String fileURL = "data/DataBookingAPI/createBooking.json";
            File bodyFile = new File(fileURL);
            JSONObject requestBody = ReadFile.readFileToJsonObject(bodyFile, Charset.defaultCharset());
            JSONObject bodyObject = requestBody.getJSONObject(bodyCase);
            String bodyString = bodyObject.toString();

            // Lưu body vào ScenarioDataStore để dùng trong bước so sánh
            SaveValueToDataStore.saveValueToKey(bodyObject, "scenario:bodyCreateBooking");

            // Gửi request tạo booking
            responseCreateBooking = given()
                    .spec(createBookingAPI.getRequestCreateBookingWithBody(bodyString)) // sử dụng request body
                    .post();

            // Log response
            System.out.println("========== Response [" + bodyCase + "] ==========");
            System.out.println(responseCreateBooking.asPrettyString());

            // Kiểm tra status code
            Assert.assertEquals(responseCreateBooking.getStatusCode(), expectedStatus,
                    "Status code sai cho case: " + bodyCase);
//
            // So sánh response nếu được yêu cầu và thành công
            if (expectedStatus == 200 && compareResponse) {
                String responseString = responseCreateBooking.asString();
                String valueBooking = JsonUtil.parseJsonByKey(responseString, "booking");

                JSONObject actualBooking = new JSONObject(valueBooking);

                Object expectedBodyObject = GetValueFromDataStore.getValueFromKey("scenario:bodyCreateBooking");
                JSONObject expectedBooking = new JSONObject(expectedBodyObject.toString());

                JSONAssert.assertEquals(expectedBooking, actualBooking, false);
            }

            // Nếu thành công, lưu bookingId
            if (responseCreateBooking.getStatusCode() == 200) {
                SaveValueToDataStore.extractKeyAndSave(responseCreateBooking, "bookingid", "scenario:bookingId");
            }
        }
    }

}
