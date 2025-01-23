package one.microproject.auth.tests.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import one.microproject.auth.controller.URLs;
import one.microproject.auth.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class RestClientImpl implements RestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestClientImpl.class);

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String baseUrl;

    public RestClientImpl(Environment environment) {
        httpClient = new OkHttpClient();
        objectMapper = new ObjectMapper();
        baseUrl = "http://localhost:" + environment.getProperty("local.server.port") + "/auth-service";
    }

    @Override
    public ServerResponse<TokenResponse> token(UserAuthRequest request) {
        return post(URLs.AUTH_TOKEN_GET, request, new TypeReference<ServerResponse<TokenResponse>>() {});
    }

    @Override
    public ServerResponse<IntrospectResponse> introspect(IntrospectRequest request) {
        return post(URLs.AUTH_TOKEN_INTROSPECT, request, new TypeReference<ServerResponse<IntrospectResponse>>() {});
    }

    @Override
    public ServerResponse<List<UserData>> getAll() {
        return get(null, URLs.USERS_GET, new TypeReference<ServerResponse<List<UserData>>>() {});
    }

    private <T> ServerResponse<T> get(String accessToken, String uri, TypeReference<ServerResponse<T>> typeReference) {
        try {
            long timestamp = System.nanoTime();
            String requestURL = baseUrl + uri;
            LOGGER.info("Request URL: {}", requestURL);
            Request.Builder requestBuilder = new Request.Builder()
                    .url(requestURL)
                    .get();
            if (accessToken != null) {
                requestBuilder.addHeader("Authorization", "Bearer " + accessToken);
            }
            Response response = httpClient.newCall(requestBuilder.build()).execute();
            int statusCode = response.code();
            LOGGER.info("Response code: {}", response.code());
            LOGGER.info("Response time: {}ms", (System.nanoTime() - timestamp)/1_000_000);
            ServerResponse<T> data = objectMapper.readValue(response.body().string(), typeReference);
            return new ServerResponse<>(data.data(), ErrorData.of(statusCode, ""));
        } catch (IOException e) {
            LOGGER.error("Auth-Client ERROR: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private <T> ServerResponse<T> post(String uri, Object body, TypeReference<ServerResponse<T>> typeReference) {
        return post(null, uri, body, typeReference);
    }

    private <T> ServerResponse<T> post(String accessToken, String uri, Object body, TypeReference<ServerResponse<T>> typeReference) {
        try {
            long timestamp = System.nanoTime();
            String requestURL = baseUrl + uri;
            LOGGER.info("Request URL: {}", requestURL);
            String bodyString = objectMapper.writeValueAsString(body);
            Request.Builder requestBuilder = new Request.Builder()
                    .url(requestURL)
                    .post(RequestBody.create(MediaType.parse("application/json"), bodyString));
            if (accessToken != null) {
                requestBuilder.addHeader("Authorization", "Bearer " + accessToken);
            }
            Response response = httpClient.newCall(requestBuilder.build()).execute();
            int statusCode = response.code();
            LOGGER.info("Response code: {}", response.code());
            LOGGER.info("Response time: {}ms", (System.nanoTime() - timestamp)/1_000_000);
            ServerResponse<T> data = objectMapper.readValue(response.body().string(), typeReference);
            return new ServerResponse<>(data.data(), ErrorData.of(statusCode, ""));
        } catch (IOException e) {
            LOGGER.error("Voltia Client ERROR: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

}
