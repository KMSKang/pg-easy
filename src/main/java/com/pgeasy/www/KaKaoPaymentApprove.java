package com.pgeasy.www;

import lombok.Builder;

@Builder
public class KaKaoPaymentApprove implements BasePaymentApprove {
    private final String apiUrl = "https://open-api.kakaopay.com/online/v1/payment/approve";
    private String cid; // 가맹점 코드, 10자
    private String cid_secret; // 가맹점 코드 인증키, 24자, 숫자+영문 소문자 조합
    private String tid; // 결제 고유번호, 결제 준비 API 응답에 포함
    private String partner_order_id; // 가맹점 주문번호, 결제 준비 API 요청과 일치해야 함
    private String partner_user_id; // 가맹점 회원 id, 결제 준비 API 요청과 일치해야 함
    private String pg_token; // 결제승인 요청을 인증하는 토큰 && 사용자 결제 수단 선택 완료 시, approval_url로 redirection 해줄 때 pg_token을 query string으로 전달
    private String payload; // 결제 승인 요청에 대해 저장하고 싶은 값, 최대 200자
    private String total_amount	; // 상품 총액, 결제 준비 API 요청과 일치해야 함

    /**
     * Name             | Data Type | Required | Description
     * cid              | String    | O        | 가맹점 코드, 10자
     * cid_secret       | String    | X        | 가맹점 코드 인증키, 24자, 숫자+영문 소문자 조합
     * partner_order_id | String    | O        | 가맹점 주문번호, 결제 준비 API 요청과 일치해야 함
     * partner_user_id  | String    | O        | 가맹점 회원 id, 결제 준비 API 요청과 일치해야 함
     * payload          | String    | X        | 결제 승인 요청에 대해 저장하고 싶은 값, 최대 200자
     * pg_token         | String    | O        | 결제승인 요청을 인증하는 토큰 && 사용자 결제 수단 선택 완료 시, approval_url로 redirection 해줄 때 pg_token을 query string으로 전달
     * tid              | String    | O        | 결제 고유번호, 결제 준비 API 응답에 포함
     * total_amount     | Integer   | X        | 상품 총액, 결제 준비 API 요청과 일치해야 함
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
    public Class<Result> getResultClass() {
        return Result.class;
    }

    public record Result(
            String payment_method_type,
            String item_code,
            Amount amount,
            Long quantity,
            String created_at,
            String item_name,
            String tid,
            String sequential_payment_methods,
            String sid,
            String partner_order_id,
            String payload,
            String approved_at,
            String partner_user_id,
            String aid,
            CardInfo card_info,
            String cid) implements BaseResult {
        public record Amount(
                int total,
                int tax_free,
                int vat,
                int discount,
                int point,
                int green_deposit) {
        }

        public record CardInfo(
                String purchase_corp,
                String purchase_corp_code,
                String issuing_corp,
                String issuing_corp_code,
                String kakaopay_purchase_corp,
                String kakaopay_purchase_corp_code,
                String kakaopay_issuing_corp,
                String kakaopay_issuing_corp_code,
                String bin,
                String card_type,
                String install_month,
                String approved_id,
                String card_mid,
                String interest_free_install,
                String card_item_code) {
        }
    }
}
