package io.github.minhhoangvn.client;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

public class MSTeamsWebHookClient {

    private final OkHttpClient client;

    public MSTeamsWebHookClient() {
        this.client = new OkHttpClient();
    }

    public Response sendNotify(String webhookUrl, String payload) throws IOException {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(payload, mediaType);
        Request request =
                new Request.Builder()
                        .url(webhookUrl)
                        .post(requestBody)
                        .addHeader("Content-Type", "application/json")
                        .build();
        return client.newCall(request).execute();
    }
}
