package StepImplementation.BookingAPISteps;

import StepImplementation.Common.GetValueFromDataStore;
import StepImplementation.Common.Helper;
import StepImplementation.Common.JsonUtil;
import StepImplementation.Common.SaveValueToDataStore;
import Utils.API.BookingAPI.CreateBookingAPI;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.Table;
import com.thoughtworks.gauge.TableRow;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;
import org.testng.Assert;

import java.io.IOException;

import static io.restassured.RestAssured.given;

public class CreateBookingSteps {

    private static final String URL_FILEJSONBODY = "data/DataBookingAPI/createBooking.json";

    CreateBookingAPI createBookingAPI = new CreateBookingAPI();

    Response responseCreateBooking;

    private Response sendCreateBookingRequestWithBody(String body) {
        return given()
                .spec(createBookingAPI.getRequestCreateBookingWithBody(body))
                .post();
    }


    private void compareResponseIfNeeded(Response response) {
        String responseString = response.asString();
        String valueBooking = JsonUtil.parseJsonByKey(responseString, "booking");

        JSONObject actualBooking = new JSONObject(valueBooking);
        Object expectedBodyObject = GetValueFromDataStore.getValueFromKey("scenario:bodyCreateBooking");
        JSONObject expectedBooking = new JSONObject(expectedBodyObject.toString());

        JSONAssert.assertEquals(expectedBooking, actualBooking, false);
    }

    private void assertStatusCode(Response response, int expectedStatus) {
        Assert.assertEquals(response.getStatusCode(), expectedStatus, "Sai status code.");
    }

    private void saveBookingIdIfSuccess(Response response) {
        if (response.getStatusCode() == 200) {
            SaveValueToDataStore.extractKeyAndSave(response, "bookingid", "scenario:bookingId");
        }
    }

    @Step("Gửi request tạo mới booking với payload lấy từ key <key>")
    public void sendCreateBookingAPI(String key) throws IOException {
        Object body = GetValueFromDataStore.getValueFromKey(key);
        String bodyString = body.toString();

        responseCreateBooking = given().spec(createBookingAPI.getRequestCreateBookingWithBody(bodyString)).post();
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

            String bodyString = Helper.loadRequestBodyToString(URL_FILEJSONBODY, bodyCase);

            SaveValueToDataStore.saveValueToKey(bodyString, "scenario:bodyCreateBooking");

            responseCreateBooking = sendCreateBookingRequestWithBody(bodyString);

            System.out.println("========== Response [" + bodyCase + "] ==========");
            System.out.println(responseCreateBooking.asPrettyString());

            assertStatusCode(responseCreateBooking, expectedStatus);

            if (expectedStatus == 200 && compareResponse) {
                compareResponseIfNeeded(responseCreateBooking);
            }

            saveBookingIdIfSuccess(responseCreateBooking);
        }
    }
}
