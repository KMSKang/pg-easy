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
     * -----------------------------------------------------
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
            String payment_method_type, // 결제 수단, CARD 또는 MONEY 중 하나
            String item_code, // 상품 코드, 최대 100자
            Amount amount, // 결제 금액 정보
            Long quantity, // 상품 수량
            String created_at, // 결제 준비 요청 시각
            String item_name, // 상품 이름, 최대 100자
            String tid, // 결제 고유 번호 - 승인/취소가 동일한 결제번호
            String sequential_payment_methods, // 순차 결제 수단
            String sid, // 정기 결제용 ID, 정기 결제 CID로 단건 결제 요청 시 발급
            String partner_order_id, // 가맹점 주문번호, 최대 100자
            String payload, // 결제 승인 요청에 대해 저장한 값, 요청 시 전달된 내용
            String approved_at, // 결제 승인 시각
            String partner_user_id, // 가맹점 회원 id, 최대 100자
            String aid, // 요청 고유 번호 - 승인/취소가 구분된 결제번호
            CardInfo card_info, // 결제 상세 정보, 결제 수단이 카드일 경우만 포함
            String cid // 가맹점 코드
    ) implements BaseResult {
        public record Amount(
                int total, // 총 결제 금액
                int tax_free, // 비과세 금액
                int vat, // 부가세 금액
                int discount, // 할인 금액
                int point, // 포인트 결제 금액
                int green_deposit // 컵 보증금 (환경부담금)
        ) { }

        public record CardInfo(
                String purchase_corp, // 매입 카드사
                String purchase_corp_code, // 매입 카드사 코드
                String issuing_corp, // 발급 카드사
                String issuing_corp_code, // 발급 카드사 코드
                String kakaopay_purchase_corp, // 카카오페이 매입 카드사
                String kakaopay_purchase_corp_code, // 카카오페이 매입 카드사 코드
                String kakaopay_issuing_corp, // 카카오페이 발급 카드사
                String kakaopay_issuing_corp_code, // 카카오페이 발급 카드사 코드
                String bin, // 카드 BIN (카드번호 앞 6자리)
                String card_type, // 카드 타입 (신용, 체크 등)
                String install_month, // 할부 개월 수
                String approved_id, // 카드사 승인 번호
                String card_mid, // 카드사 가맹점 번호
                String interest_free_install, // 무이자 할부 여부
                String card_item_code // 카드 상품 코드
        ) { }
    }
}
