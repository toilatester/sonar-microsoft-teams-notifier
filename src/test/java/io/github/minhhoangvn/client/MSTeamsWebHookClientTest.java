package io.github.minhhoangvn.client;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class MSTeamsWebHookClientTest {

    private WireMockServer wireMockServer;

    @Before
    public void setUp() {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().port(8080));
        wireMockServer.start();
    }

    @After
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void testSendNotifyPayload() throws IOException {
        stubFor(
                post(urlEqualTo("/mock"))
                        .withHeader(
                                "Content-Type", WireMock.equalTo("application/json; charset=UTF-8"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withHeader("Content-Type", "application/json")
                                        .withBody("{\"message\": \"Mock response\"}")));

        JSONObject dummy = new JSONObject();
        dummy.put("status", "dummy");
        var client = new MSTeamsWebHookClient();
        var response = client.sendNotify("http://localhost:8080/mock", dummy.toString());
        Assert.assertEquals(200, response.code());
        Assert.assertEquals("{\"message\": \"Mock response\"}", response.body().string());
    }
}
