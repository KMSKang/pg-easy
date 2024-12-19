package com.pgeasy.www;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
public class PgPaymentServiceImpl implements PgPaymentService {
    private final HttpClient httpClient = HttpClient.newHttpClient();

    // 결제 모듈창
    public CommonResponse<BaseResult> paymentModule(PaymentModule paymentModule) {
        BasePaymentModule basePaymentModule = paymentModule.basePaymentModule();
        return sendRequest(basePaymentModule, basePaymentModule.getAuthorization(paymentModule.secretKey()));
    }

    // 결제 승인
    public CommonResponse<BaseResult> paymentApprove(PaymentApprove paymentApprove) {
        BasePaymentApprove basePaymentApprove = paymentApprove.basePaymentApprove();
        return sendRequest(basePaymentApprove, basePaymentApprove.getAuthorization(paymentApprove.secretKey()));
    }

    // 결제 취소
    public CommonResponse<BaseResult> paymentCancel(PaymentCancel paymentCancel) {
        BasePaymentCancel basePaymentCancel = paymentCancel.basePaymentCancel();
        return sendRequest(basePaymentCancel, basePaymentCancel.getAuthorization(paymentCancel.secretKey()));
    }

    // 결제 결과 callback
    public void handlePaymentCallback() {
        // Implementation here
    }

    // 가맹점 승인
    public void approveMerchant() {
        // Implementation here
    }

    // 결제 환불
    public void refundPayment() {
        // Implementation here
    }

    // 결제 상태 확인
    public PaymentStatus checkPaymentStatus() {
        // Implementation here
        return null;
    }

    private CommonResponse<BaseResult> sendRequest(BasePayment<?> basePayment, String authorization) {
        try {
            HttpRequest request = createHttpRequest(basePayment, authorization);
            log.info("HttpRequest : {}", request);

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("HttpResponse : {}", response);

            int statusCode = response.statusCode();
            JSONObject responseBody = processStringToJson(response.body());
            log.info("responseBody : {}", responseBody);

            return CommonResponse.<BaseResult>builder()
                                 .code(response.statusCode())
                                 .message(getMessage(statusCode, responseBody))
                                 .data(getData(statusCode, responseBody, basePayment.getResultClass()))
                                 .build();
        } catch (Exception e) {
            log.error("sendRequest() Exception : {}", e.getMessage(), e);
            throw new RuntimeException("Exception: ", e);
        }
    }

    private JSONObject processStringToJson(String body) {
        try {
            return (JSONObject) new JSONParser().parse(body);
        } catch (ParseException e) {
            log.error("processStringToJson() Exception: {}", e.getMessage(), e);
            throw new RuntimeException("Exception: ", e);
        }
    }

    private static String getMessage(int statusCode, JSONObject responseBody) {
        if (200 == statusCode) {
            return "OK";
        }
        else if (responseBody.containsKey("message")) {
            return (String) responseBody.get("message");
        }
        else if (responseBody.containsKey("error_message")) {
            return (String) responseBody.get("error_message");
        }
        else {
            return "Unexpected Error";
        }
    }

    private BaseResult getData(int statusCode, JSONObject responseBody, Class<BaseResult> resultClass) {
        if (200 != statusCode) {
            return null;
        }

        return processMapToPaymentClass(responseBody, resultClass);
    }

    private BaseResult processMapToPaymentClass(JSONObject responseBody, Class<BaseResult> targetClass) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // 인식 못하는 필드는 무시

        try {
            return objectMapper.readValue(responseBody.toJSONString(), targetClass);
        } catch (JsonProcessingException e) {
            log.error("processMapToPaymentClass() Exception: {}", e.getMessage(), e);
            throw new RuntimeException("Exception: ", e);
        }
    }

    private HttpRequest createHttpRequest(BasePayment<?> basePayment, String authorization) {
        return HttpRequest.newBuilder()
                          .uri(URI.create(basePayment.getApiUrl()))
                          .header("Authorization", authorization)
                          .header("Content-Type", "application/json")
                          .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(basePayment)))
                          .build();
    }
}
