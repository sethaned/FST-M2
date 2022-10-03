package Project;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@ExtendWith(PactConsumerTestExt.class)
public class ConsumerTest {

    //Header
    Map<String,String> headers = new HashMap<>();
    //Resource Path
    String resourcePath = "/api/users";

    //Genearte Contract
    @Pact(consumer = "UserConsumer", provider = "UserProvider")
    public RequestResponsePact createPact(PactDslWithProvider builder){
         //Add Header
        headers.put("Content-Type", "application/json");

        //Create a Json Body for request and response
        DslPart requestResponseBody = new PactDslJsonBody()
                .numberType("id", 124)
                .stringType("firstName", "seema")
                .stringType("lastName", "Thaned")
                .stringType("email", "seema@exmaple.com");

        // Write the fragment for POST
        return builder.given("A request to create a user")
                .uponReceiving("A request to create a user")
                .method("POST")
                .headers(headers)
                .path(resourcePath)
                .body(requestResponseBody)
                .willRespondWith()
                .status(201)
                .body(requestResponseBody)
                .toPact();

    }

    @Test
    @PactTestFor(providerName = "UserProvider", port = "8080")
    public void consumerTest(){
        //Base URI
        String requestURI ="http://localhost:8080"+resourcePath;

        //RequestBody
        Map<String, Object> reqBody = new HashMap<>();
                reqBody.put("id", 123);
        reqBody.put("firstName","seema");
        reqBody.put("lastName", "Thaned");
        reqBody.put("email","seema@example.com");

        //Generate Response
        given().headers(headers).body(reqBody).log().all().
                when().post(requestURI).
                then().statusCode(201).log().all();

    }

}
