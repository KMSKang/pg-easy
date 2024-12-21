package com.examples.www.kakao;

import com.pgeasy.www.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/kakao")
@Controller
public class KaKaoPaymentController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final PgPaymentService pgPaymentService;

    @Value("${kakao-client-id}")
    private String CLIENT_ID;

    @Value("${kakao-secret-key}")
    private String KAKAO_SECRET_KEY;

    private String lastTid;
    private String lastUserId;

    @ResponseBody
    @PostMapping("/payment/ready")
    public ResponseEntity<BaseResult> paymentModule(@RequestBody KaKaoPaymentModuleRequest kaKaoPaymentModuleRequest) {
        PaymentModule paymentModule = createPaymentModule(kaKaoPaymentModuleRequest);
        CommonResponse<BaseResult> commonResponse = pgPaymentService.paymentModule(paymentModule);
        logger.info("commonResponse: {}", commonResponse);
        updateLastOrder(kaKaoPaymentModuleRequest, commonResponse);
        return ResponseEntity.status(commonResponse.code()).body(commonResponse.data());
    }

    private PaymentModule createPaymentModule(KaKaoPaymentModuleRequest confirmPaymentRequest) {
        Long orderId = confirmPaymentRequest.orderId();

        return PaymentModule.builder()
                            .secretKey(KAKAO_SECRET_KEY)
//                            .basePaymentModule(KaKaoPaymentModule.builder()
//                                                                 .cid(CLIENT_ID)
//                                                                 .cid_secret("")
//                                                                 .partner_order_id(orderId)
//                                                                 .partner_user_id(confirmPaymentRequest.userId())
//                                                                 .item_name(confirmPaymentRequest.itemName())
//                                                                 .item_code("PRD15511")
//                                                                 .quantity(confirmPaymentRequest.quantity())
//                                                                 .total_amount(confirmPaymentRequest.totalAmount())
//                                                                 .tax_free_amount(0)
//                                                                 .approval_url("http://localhost:8080/kakao/payment/approve/" +orderId)
//                                                                 .cancel_url("http://localhost:8080/kakao/payment/cancel")
//                                                                 .fail_url("http://localhost:8080/kakao/payment/fail")
//                                                                 .available_cards("HANA")
//                                                                 .payment_method_type("CARD")
//                                                                 .install_month(0)
//                                                                 .use_share_installment("N")
//                                                                 .custom_json("카카오페이 테스트")
//                                                                 .build())
                            .build();
    }

    private void updateLastOrder(KaKaoPaymentModuleRequest confirmPaymentRequest, CommonResponse<BaseResult> commonResponse) {
        KaKaoPaymentModule.Result data = (KaKaoPaymentModule.Result) commonResponse.data();
        lastTid = data.tid();
        lastUserId = confirmPaymentRequest.userId();
    }

    @ResponseBody
    @GetMapping("/payment/approve/{id}")
    public ResponseEntity<BaseResult> paymentApprove(@PathVariable("id") String orderId, @RequestParam("pg_token") String pgToken) {
        PaymentApprove paymentApprove = createPaymentApprove(orderId, pgToken);
        CommonResponse<BaseResult> commonResponse = pgPaymentService.paymentApprove(paymentApprove);
        logger.info("commonResponse: {}", commonResponse);
        return ResponseEntity.status(commonResponse.code()).body(commonResponse.data());
    }

    private PaymentApprove createPaymentApprove(String orderId, String pgToken) {
        return PaymentApprove.builder()
                             .secretKey(KAKAO_SECRET_KEY)
                             .basePaymentApprove(KaKaoPaymentApprove.builder()
                                                                       .cid(CLIENT_ID)
                                                                       .tid(lastTid)
                                                                       .partner_order_id(orderId)
                                                                       .partner_user_id(lastUserId)
                                                                       .pg_token(pgToken)
                                                                       .build())
                             .build();
    }

    @ResponseBody
    @PostMapping("/payment/cancel")
    public ResponseEntity<BaseResult> paymentCacnel(@RequestBody KaKaoPaymentCancelRequest kaKaoPaymentCancelRequest) {
        PaymentCancel paymentCancel = createPaymentCancel(kaKaoPaymentCancelRequest);
        CommonResponse<BaseResult> commonResponse = pgPaymentService.paymentCancel(paymentCancel);
        logger.info("commonResponse: {}", commonResponse);
        return ResponseEntity.status(commonResponse.code()).body(commonResponse.data());
    }

    private PaymentCancel createPaymentCancel(KaKaoPaymentCancelRequest kaKaoPaymentCancelRequest) {
        return PaymentCancel.builder()
                            .secretKey(KAKAO_SECRET_KEY)
                            .basePaymentCancel(KaKaoPaymentCancel.builder()
                                                                 .cid(kaKaoPaymentCancelRequest.cid())
                                                                 .cid_secret(kaKaoPaymentCancelRequest.cid_secret())
                                                                 .tid(kaKaoPaymentCancelRequest.tid())
                                                                 .cancel_amount(kaKaoPaymentCancelRequest.cancel_amount())
                                                                 .cancel_tax_free_amount(kaKaoPaymentCancelRequest.cancel_tax_free_amount())
                                                                 .cancel_vat_amount(kaKaoPaymentCancelRequest.cancel_vat_amount())
                                                                 .cancel_available_amount(kaKaoPaymentCancelRequest.cancel_available_amount())
                                                                 .payload(kaKaoPaymentCancelRequest.payload())
                                                                 .build())
                            .build();
    }
}
