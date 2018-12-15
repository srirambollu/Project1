package com.bat.petra.photorecognition.einstein;

import com.bat.petra.photorecognition.einstein.client.EinsteinClient;
import com.bat.petra.photorecognition.einstein.model.TokenResult;
import com.bat.petra.photorecognition.einstein.model.DetectionResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.io.File;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EinsteinClientTest {

    private static final String EINSTEIN_API_URL = "https://api.einstein.ai/v2/";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private EinsteinClient einsteinClient;

    private MockRestServiceServer mockServer;

    @Before
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void executeDetectRequest() {
        mockServer.expect(requestTo(EINSTEIN_API_URL + "vision/detect"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("{\"probabilities\":[{\n" +
                        "            \"label\": \"labelName\",\n" +
                        "            \"probability\": 0.1,\n" +
                        "            \"boundingBox\": {\n" +
                        "                \"minX\": 1,\n" +
                        "                \"minY\": 2,\n" +
                        "                \"maxX\": 3,\n" +
                        "                \"maxY\": 4\n" +
                        "            }\n" +
                        "        }],\"object\":\"\"}", MediaType.APPLICATION_JSON));

        DetectionResponse result = einsteinClient.executeDetectRequest(EINSTEIN_API_URL,"TOKEN",
                "MODEL_ID", new File("temp.file"));

        System.out.println("result " + result );

        mockServer.verify();

        assertNotNull(result.getProbabilities());
    }

    @Test
    public void createAccessTokenRequest() {
        mockServer.expect(requestTo(EINSTEIN_API_URL + "oauth2/token/"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("{\"access_token\": \"TOKEN\",\n" +
                        "  \"token_type\": \"Bearer\",\n" +
                        "  \"expires_in\": 9999902}", MediaType.APPLICATION_JSON));

        TokenResult result = einsteinClient.accessTokenRequest(EINSTEIN_API_URL, "assertion");
        System.out.println("result " + result );
        mockServer.verify();
        assertNotNull(result.getAccessToken());
    }
}