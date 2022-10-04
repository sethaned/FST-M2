package Project;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class GitHub_RestAssured_Project {

    RequestSpecification requestSpec;
    ResponseSpecification responseSpec;

    String SSH_Key = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDhmCjLppeU1ZLdwgejbABn9jDEZlJjkKJP+8bIaMHzLSjUnul8p6CCucD6xfFRD91laCH4S6B1AApeq7robM7M67mDqEgoyS32oK2lhpcL/ddpjyaXt3mPVqihdcVvxWRfIJ6U+g5rNLCgtP3IBUlmGfzLJGDpU9ObxwoXz9sFN5boljqhJ7CJioaLKNQLn2vQKD48rY8vGTok3xE0GgSNmJUr8Uwq55hQE3h7tyIzBSNUHCbQJUod2nGNAXIxSX0d1AxOm0TTwZBpSDp7oyOvtRJ6lvJO6Mf9cCom3VUMoYaWB7uUV7yhLdIZwTmmBEYfsgfNeT4vBK8yP1UyYCTt";
    int sshid;

    @BeforeClass

    public void SetUp() {
        //RequestSpecification
        requestSpec = new RequestSpecBuilder()
                .setBaseUri("https://api.github.com")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", " token 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")
                .build();


    }

    @Test(priority = 1)
    public void PostRequest() {
        // request body
        Map<String, String> reqBody = new HashMap<>();
        reqBody.put("title", "TestAPIKey");
        reqBody.put("key", SSH_Key);

        Response response = given().spec(requestSpec).body(reqBody).when().post("/user/keys");
        System.out.println(response.getBody().asPrettyString());


        //Extract id of SSH Key
        sshid = response.then().extract().path("id");
        System.out.println(sshid);

        // assertion
        response.then().statusCode(201).body("key", equalTo(SSH_Key));
    }

    @Test(priority = 2)
    public void GetRequest() {
        //generate response and assert

        Response response = given().spec(requestSpec).pathParam("keyId", sshid)
                .when().get("/user/keys/{keyId}");
        System.out.println(response.getBody().asPrettyString());

        // assertion
        response.then().statusCode(200).body("key", equalTo(SSH_Key));
    }

    @Test(priority =3)
    public void Deleterequest(){

        Response response = given().spec(requestSpec).pathParam("keyId", sshid)
                .when().delete("/user/keys/{keyId}");
        System.out.println(response.getBody().asPrettyString());

        // assertion
        response.then().statusCode(204).body("key", equalTo(SSH_Key));

    }

}





