package Utils.Base;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;

public class BaseRequest {

    private static Map<String, String> header;

    private static void setBaseHeader() {
        header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("Accept", "*/*");
    }


    public static RequestSpecification getBaseRequest()
    {
        setBaseHeader();
        return new RequestSpecBuilder()
                .setBaseUri(BasePathBookingAPI.BASE_URL)
                .addHeaders(header)
                .build();
    }
}
