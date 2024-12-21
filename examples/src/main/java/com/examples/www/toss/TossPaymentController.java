package com.examples.www.toss;

import com.pgeasy.www.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/toss")
@Controller
public class TossPaymentController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final PgPaymentService pgPaymentService;
//    private final Map<String, String> billingKeyMap = new HashMap<>();

    @Value("${toss-client-key}")
    private String CLIENT_KEY;

    @Value("${toss-api-secret-key}")
    private String API_SECRET_KEY;

    @Value("${toss-widget-secret-key}")
    private String WIDGET_SECRET_KEY;

    @GetMapping("/payment/ready")
    public String index(Model model) {
        model.addAttribute("clientKey", CLIENT_KEY);
        return "/toss/widget/checkout";
    }

    @PostMapping("/payment/approve")
    public ResponseEntity<BaseResult> confirmPayment(@RequestBody TossConfirmPaymentRequest confirmPaymentRequest) {
        PaymentApprove paymentApprove = createApprovePayment(confirmPaymentRequest.paymentKey(), confirmPaymentRequest.orderId(), confirmPaymentRequest.amount());
        CommonResponse<BaseResult> commonResponse = pgPaymentService.paymentApprove(paymentApprove);
        logger.info("commonResponse: {}", commonResponse);
        return ResponseEntity.status(commonResponse.code()).body(commonResponse.data());
    }

    private PaymentApprove createApprovePayment(String paymentKey, String orderId, Integer amount) {
        return PaymentApprove.builder()
                             .secretKey(WIDGET_SECRET_KEY)
                             .basePaymentApprove(TossPaymentApprove.builder()
                                                                      .paymentKey(paymentKey)
                                                                      .orderId(orderId)
                                                                      .amount(amount)
                                                                      .build())
                             .build();
    }

//    @RequestMapping(value = "/confirm-billing")
//    public ResponseEntity<JSONObject> confirmBilling(@RequestBody String jsonBody) throws Exception {
//        JSONObject requestData = parseRequestData(jsonBody);
//        String billingKey = billingKeyMap.get(requestData.get("customerKey"));
//        JSONObject response = sendRequest(requestData, API_SECRET_KEY, "https://api.tosspayments.com/v1/billing/" + billingKey);
//        return ResponseEntity.status(response.containsKey("error") ? 400 : 200).body(response);
//    }
//
//    @RequestMapping(value = "/issue-billing-key")
//    public ResponseEntity<JSONObject> issueBillingKey(@RequestBody String jsonBody) throws Exception {
//        JSONObject requestData = parseRequestData(jsonBody);
//        JSONObject response = sendRequest(requestData, API_SECRET_KEY, "https://api.tosspayments.com/v1/billing/authorizations/issue");
//
//        if (!response.containsKey("error")) {
//            billingKeyMap.put((String) requestData.get("customerKey"), (String) response.get("billingKey"));
//        }
//
//        return ResponseEntity.status(response.containsKey("error") ? 400 : 200).body(response);
//    }
//
//    @RequestMapping(value = "/callback-auth", method = RequestMethod.GET)
//    public ResponseEntity<JSONObject> callbackAuth(@RequestParam String customerKey, @RequestParam String code) throws Exception {
//        JSONObject requestData = new JSONObject();
//        requestData.put("grantType", "AuthorizationCode");
//        requestData.put("customerKey", customerKey);
//        requestData.put("code", code);
//
//        String url = "https://api.tosspayments.com/v1/brandpay/authorizations/access-token";
//        JSONObject response = sendRequest(requestData, API_SECRET_KEY, url);
//
//        logger.info("commonResponse: {}", response);
//
//        return ResponseEntity.status(response.containsKey("error") ? 400 : 200).body(response);
//    }
//
//    @RequestMapping(value = "/confirm/brandpay", method = RequestMethod.POST, consumes = "application/json")
//    public ResponseEntity<JSONObject> confirmBrandpay(@RequestBody String jsonBody) throws Exception {
//        JSONObject requestData = parseRequestData(jsonBody);
//        String url = "https://api.tosspayments.com/v1/brandpay/payments/confirm";
//        JSONObject response = sendRequest(requestData, API_SECRET_KEY, url);
//        return ResponseEntity.status(response.containsKey("error") ? 400 : 200).body(response);
//    }

    @GetMapping("/payment/success")
    public String successPayment() {
        return "/toss/widget/success";
    }

    @RequestMapping(value = "/fail", method = RequestMethod.GET)
    public String failPayment(HttpServletRequest request, Model model) {
        model.addAttribute("code", request.getParameter("code"));
        model.addAttribute("message", request.getParameter("message"));
        return "/toss/fail";
    }

//    private JSONObject parseRequestData(String jsonBody) {
//        try {
//            return (JSONObject) new JSONParser().parse(jsonBody);
//        } catch (ParseException e) {
//            logger.error("JSON Parsing Error", e);
//            return new JSONObject();
//        }
//    }
//
//    private JSONObject sendRequest(JSONObject requestData, String secretKey, String urlString) throws IOException {
//        HttpURLConnection connection = createConnection(secretKey, urlString);
//        try (OutputStream os = connection.getOutputStream()) {
//            os.write(requestData.toString().getBytes(StandardCharsets.UTF_8));
//        }
//
//        try (InputStream responseStream = connection.getResponseCode() == 200 ? connection.getInputStream() : connection.getErrorStream();
//             Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8)) {
//            return (JSONObject) new JSONParser().parse(reader);
//        } catch (Exception e) {
//            logger.error("Error reading response", e);
//            JSONObject errorResponse = new JSONObject();
//            errorResponse.put("error", "Error reading response");
//            return errorResponse;
//        }
//    }
//
//    private HttpURLConnection createConnection(String secretKey, String urlString) throws IOException {
//        URL url = new URL(urlString);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8)));
//        connection.setRequestProperty("Content-Type", "application/json");
//        connection.setRequestMethod("POST");
//        connection.setDoOutput(true);
//        return connection;
//    }
}
