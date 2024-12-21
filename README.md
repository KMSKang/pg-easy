# PG사 결제 통합 라이브러리
- PG사 결제 기능을 간편하게 사용할 수 있도록 제공하는 라이브러리입니다.
- 현재 **토스페이**, **카카오페이**를 지원합니다.

## 설치 방법
### build.gralde
```
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.KMSKang:be-library:1.2.1'
}
```

## 사용 방법

### 1. 세팅
```JAVA
@Configuration
public class PaymentConfig {
    @Bean
    public PgPaymentService pgPaymentService() {
        return new PgPaymentServiceImpl();
    }
}

@RequiredArgsConstructor
@RequestMapping("/kakao")
@Controller
public class KaKaoPaymentController {
    private final PgPaymentService pgPaymentService;
}
```

### 2. 결제 모듈
```JAVA
@ResponseBody
@PostMapping("/payment/ready")
public ResponseEntity<BaseResult> confirmPayment(@RequestBody KaKaoConfirmPaymentRequest confirmPaymentRequest) {
    PaymentModule paymentModule = createPaymentModule(confirmPaymentRequest);
    CommonResponse<BaseResult> commonResponse = pgPaymentService.paymentModule(paymentModule);
    logger.info("commonResponse: {}", commonResponse);
    updateLastOrder(confirmPaymentRequest, commonResponse);
    return ResponseEntity.status(commonResponse.code()).body(commonResponse.data());
}

private PaymentModule createPaymentModule(KaKaoConfirmPaymentRequest confirmPaymentRequest) {
    Long orderId = confirmPaymentRequest.orderId();

    return PaymentModule.builder()
                        .secretKey(KAKAO_SECRET_KEY)
                        .basePaymentModule(KaKaoPaymentModule.builder()
                                                             .cid(CLIENT_ID)
                                                             .partner_order_id(orderId)
                                                             .partner_user_id(confirmPaymentRequest.userId())
                                                             .item_name(confirmPaymentRequest.itemName())
                                                             .quantity(confirmPaymentRequest.quantity())
                                                             .total_amount(confirmPaymentRequest.totalAmount())
                                                             .tax_free_amount(0)
                                                             .approval_url("http://localhost:8080/kakao/payment/approve/" +orderId)
                                                             .cancel_url("http://localhost:8080/kakao/payment/cancel")
                                                             .fail_url("http://localhost:8080/kakao/payment/fail")
                                                             .build())
                        .build();
}
```

### 3. 결제 승인
```JAVA
@ResponseBody
@GetMapping("/payment/approve/{id}")
public ResponseEntity<BaseResult> approvePayment(@PathVariable("id") String orderId, @RequestParam("pg_token") String pgToken) {
    ApprovePayment approvePayment = createApprovePayment(orderId, pgToken);
    CommonResponse<BaseResult> commonResponse = pgPaymentService.approvePayment(approvePayment);
    logger.info("commonResponse: {}", commonResponse);
    return ResponseEntity.status(commonResponse.code()).body(commonResponse.data());
}

private ApprovePayment createApprovePayment(String orderId, String pgToken) {
    return ApprovePayment.builder()
                         .secretKey(KAKAO_SECRET_KEY)
                         .baseApprovePayment(KaKaoPayApprovePayment.builder()
                                                                   .cid(CLIENT_ID)
                                                                   .tid(lastTid)
                                                                   .partner_order_id(orderId)
                                                                   .partner_user_id(lastUserId)
                                                                   .pg_token(pgToken)
                                                                   .build())
                         .build();
}
```
## 라이센스
MIT License

Copyright (c) 2024 Backend Engineer

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
