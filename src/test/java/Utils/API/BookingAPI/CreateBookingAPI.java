package Utils.API.BookingAPI;

import Utils.Base.BasePathBookingAPI;
import Utils.Base.BaseRequest;
import StepImplementation.Common.ReadFile;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class CreateBookingAPI {

    private String body;
    private RequestSpecification requestCreateBooking;

    private String setBodyBookingAPI() throws IOException {
        final String fileURL = "data/DataBookingAPI/createBooking.json";
        File bodyFile = new File(fileURL);
        body = ReadFile.readFileToString(bodyFile, Charset.defaultCharset());
        return body;
    }

    public RequestSpecification getRequestCreateBooking() throws IOException {
        setBodyBookingAPI();

        requestCreateBooking = BaseRequest.getBaseRequest()
                .basePath(BasePathBookingAPI.PATHCREATEBOOKING)
                .body(body);

        return requestCreateBooking;
    }

    public RequestSpecification getRequestCreateBookingWithBody(String body) {

        requestCreateBooking = BaseRequest.getBaseRequest()
                .basePath(BasePathBookingAPI.PATHCREATEBOOKING)
                .body(body);
        return requestCreateBooking;
    }


}
