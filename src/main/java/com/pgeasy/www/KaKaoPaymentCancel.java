package com.pgeasy.www;

import lombok.Builder;

@Builder
public class KaKaoPaymentCancel implements BasePaymentCancel {
    private final String apiUrl = "https://open-api.kakaopay.com/online/v1/payment/cancel";
    private final String cid; // 가맹점 코드
    private final String cid_secret; // 가맹점 코드 인증키
    private final String tid; // 결제 고유번호
    private final Integer cancel_amount; // 취소 금액
    private final Integer cancel_tax_free_amount; // 취소 비과세 금액
    private final Integer cancel_vat_amount; // 취소 부가세 금액
    private final Integer cancel_available_amount; // 취소 가능 금액
    private final String payload; // 요청에 저장하고 싶은 값

    /**
     * Name                    | Data Type | Required | Description
     * cancel_amount           | Integer   | O        | 취소 금액
     * cancel_available_amount | Integer   | X        | 취소 가능 금액(결제 취소 요청 금액 포함)
     * cancel_tax_free_amount  | Integer   | O        | 취소 비과세 금액
     * cancel_vat_amount       | Integer   | X        | 취소 부가세 금액 && 승인시 vat_amount를 보냈다면 취소시에도 동일하게 요청 && 승인과 동일하게 요청 시 값을 전달하지 않을 경우 자동계산 && (취소 금액 - 취소 비과세 금액)/11, 소숫점이하 반올림
     * cid                     | String    | O        | 가맹점 코드, 10자
     * cid_secret              | String    | X        | 가맹점 코드 인증키, 24자, 숫자+영문 소문자 조합
     * payload                 | String    | X        | 해당 요청에 대해 저장하고 싶은 값, 최대 200자
     * tid                     | String    | O        | 결제 고유번호, 20자
     */

    @Override
    public String getApiUrl() {
        return apiUrl;
    }

    @Override
    public String getAuthorization(String secretKey) {
        return "SECRET_KEY " + secretKey;
    }

    @Override
    public Class<KaKaoPaymentApprove.Result> getResultClass() {
        return KaKaoPaymentApprove.Result.class;
    }

    public record Result() {

    }
}
