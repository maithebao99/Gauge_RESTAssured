package StepImplementation.BookingAPISteps;

import StepImplementation.CommonSteps.SaveValue;
import Utils.API.BookingAPI.CreateBookingAPI;
import com.google.inject.Inject;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.ScenarioDataStore;
import com.thoughtworks.gauge.datastore.SpecDataStore;
import com.thoughtworks.gauge.datastore.SuiteDataStore;
import io.cucumber.guice.ScenarioScoped;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;

@ScenarioScoped
public class CreateBookingSteps {

    CreateBookingAPI createBookingAPI = new CreateBookingAPI();

    Response responseCreateBooking;


    @Step("Call api tạo mới booking")
    public void sendCreateBookingAPI() throws IOException {
        responseCreateBooking = RestAssured.given().spec(createBookingAPI.getRequestCreateBooking()).post();
        System.out.println(responseCreateBooking.asPrettyString());
    }

    @Step("Lấy <key> từ response và lưu vào biến <fullKey>")
    public void extractKeyFromResponseAndStore(String key, String fullKey) {
        SaveValue.extractKeyAndSave(responseCreateBooking, key, fullKey);
    }



}
