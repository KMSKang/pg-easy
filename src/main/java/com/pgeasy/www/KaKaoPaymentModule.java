package com.pgeasy.www;

import lombok.Builder;

import java.util.List;
import java.util.Map;

/**
 * Request Body Payload
 *
 * 카드사 코드* : SHINHAN, KB, HYUNDAI, LOTTE, SAMSUNG, NH, BC, HANA, CITI, KAKAOBANK, KAKAOPAY, WOORIONLY
 * KAKAOBANK, KAKAOPAY, CITY는 KB/BC에 미포함, 별도 지정 필요
 * SUHYUP, SHINHYUP, JEONBUK, JEJU, SC 등 기타 상세 분류가 필요한 경우 협의필요
 * 컵 보증금(green_deposit) 사용 시 부분 취소 불가
 *
 * Name                  | Data Type                | Required | Description
 * -------------------------------------------------------------------------
 * cid                   | String                   | O        | 가맹점 코드, 10자
 * cid_secret            | String                   | X        | 가맹점 코드 인증키, 24자, 숫자와 영문 소문자 조합
 * partner_order_id      | String                   | O        | 가맹점 주문번호, 최대 100자
 * partner_user_id       | String                   | O        | 가맹점 회원 id, 최대 100자 (실명, ID와 같은 개인정보가 포함되지 않도록 유의)
 * item_name             | String                   | O        | 상품명, 최대 100자
 * item_code             | String                   | X        | 상품코드, 최대 100자
 * quantity              | Integer                  | O        | 상품 수량
 * total_amount          | Integer                  | O        | 상품 총액
 * tax_free_amount       | Integer                  | O        | 상품 비과세 금액
 * vat_amount            | Integer                  | X        | 상품 부가세 금액, 값을 보내지 않을 경우 다음과 같이 VAT 자동 계산: (상품총액 - 상품 비과세 금액) / 11, 소수점 이하 반올림
 * green_deposit         | Integer                  | X        | 컵 보증금
 * approval_url          | String                   | O        | 결제 성공 시 redirect URL, 최대 255자
 * cancel_url            | String                   | O        | 결제 취소 시 redirect URL, 최대 255자
 * fail_url              | String                   | O        | 결제 실패 시 redirect URL, 최대 255자
 * available_cards       | JSON Array               | X        | 결제 수단으로 사용할 카드사 코드 배열 && 카카오페이와 사전 협의 필요, ex) ["HANA", "BC"] (기본값: 모든 카드사 허용)
 * payment_method_type   | String                   | X        | 사용 허가할 결제 수단, 지정하지 않으면 모든 결제 수단 허용 && CARD 또는 MONEY 중 하나
 * install_month         | Integer                  | X        | 카드 할부개월, 0~12
 * use_share_installment | String                   | X        | 분담무이자 설정 (Y/N), 사용 시 사전 협의 필요
 * custom_json           | JSON Map {String:String}	| X        | 사전에 정의된 기능: 1. 결제 화면에 보여줄 사용자 정의 문구 (카카오페이와 사전 협의 필요) && 2. iOS에서 사용자 인증 완료 후 가맹점 앱으로 자동 전환 기능 && (iOS만 처리가능, 안드로이드 동작불가) ex) return_custom_url과 함께 key 정보에 앱스킴을 넣어서 전송 && "return_custom_url":"kakaotalk://"
 */

/**
 * Response Body Payload
 *
 * Name                          | Data      | Type      | Description
 * --------------------------------------------------------------------------------------------------------------------------------------------
 * tid                      | String   | 결제 고유 번호, 20자
 * next_redirect_app_url    | String   | 요청한 클라이언트(Client)가 모바일 앱일 경우 카카오톡 결제 페이지 Redirect URL
 * next_redirect_mobile_url | String   | 요청한 클라이언트가 모바일 웹일 경우 카카오톡 결제 페이지 Redirect URL
 * next_redirect_pc_url     | String   | 요청한 클라이언트가 PC 웹일 경우 카카오톡으로 결제 요청 메시지(TMS)를 보내기 위한 사용자 정보 입력 화면 Redirect URL
 * android_app_scheme       | String   | 카카오페이 결제 화면으로 이동하는 Android 앱 스킴(Scheme) - 내부 서비스용
 * ios_app_scheme           | String   | 카카오페이 결제 화면으로 이동하는 iOS 앱 스킴 - 내부 서비스용
 * created_at               | Datetime | 결제 준비 요청 시간
 */

@Builder
public class KaKaoPaymentModule implements BasePaymentModule {
    private final String apiUrl = "https://open-api.kakaopay.com/online/v1/payment/ready";
    private String cid; // 가맹점 코드
    private String cid_secret; // 가맹점 코드 인증키
    private Long partner_order_id; // 가맹점 주문번호
    private String partner_user_id; // 가맹점 회원 id
    private String item_name; // 상품명
    private String item_code; // 상품코드
    private Integer quantity; // 상품 수량
    private Integer total_amount; // 상품 총액
    private Integer tax_free_amount; // 상품 비과세 금액
    private Integer vat_amount; // 상품 부가세 금액
    private Integer green_deposit; // 컵 보증금
    private String approval_url; // 결제 성공 시 redirect URL
    private String cancel_url; // 결제 취소 시 redirect URL
    private String fail_url; // 결제 실패 시 redirect URL
    private List<String> available_cards; // 결제 수단으로 사용할 카드사 코드 배열
    private String payment_method_type; // 사용 허가할 결제 수단
    private Integer install_month; // 카드 할부개월
    private String use_share_installment; // 분담무이자 설정
    private Map<String, String> custom_json; // 사전에 정의된 기능

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

    @Override
    public String toString() {
        return "KaKaoPaymentModule{" +
                "apiUrl='" + apiUrl + '\'' +
                ", cid='" + cid + '\'' +
                ", cid_secret='" + cid_secret + '\'' +
                ", partner_order_id=" + partner_order_id +
                ", partner_user_id='" + partner_user_id + '\'' +
                ", item_name='" + item_name + '\'' +
                ", item_code='" + item_code + '\'' +
                ", quantity=" + quantity +
                ", total_amount=" + total_amount +
                ", tax_free_amount=" + tax_free_amount +
                ", vat_amount=" + vat_amount +
                ", green_deposit=" + green_deposit +
                ", approval_url='" + approval_url + '\'' +
                ", cancel_url='" + cancel_url + '\'' +
                ", fail_url='" + fail_url + '\'' +
                ", available_cards=" + available_cards +
                ", payment_method_type='" + payment_method_type + '\'' +
                ", install_month=" + install_month +
                ", use_share_installment='" + use_share_installment + '\'' +
                ", custom_json=" + custom_json +
                '}';
    }

    public record Result(
            String tid, // 결제 고유 번호, 20자
            String next_redirect_app_url, // 요청한 클라이언트(Client)가 모바일 앱일 경우 && 카카오톡 결제 페이지 Redirect URL
            String next_redirect_mobile_url, // 요청한 클라이언트가 모바일 웹일 경우 && 카카오톡 결제 페이지 Redirect URL
            String next_redirect_pc_url, // 요청한 클라이언트가 PC 웹일 경우 && 카카오톡으로 결제 요청 메시지(TMS)를 보내기 위한 사용자 정보 입력 화면 Redirect URL
            String android_app_scheme, // 카카오페이 결제 화면으로 이동하는 Android 앱 스킴(Scheme) - 내부 서비스용
            String ios_app_scheme, // 카카오페이 결제 화면으로 이동하는 iOS 앱 스킴 - 내부 서비스용
            String created_at, // 결제 준비 요청 시간
            boolean tms_result) implements BaseResult {
        @Override
        public String toString() {
            return "Result{" +
                    "tid='" + tid + '\'' +
                    ", next_redirect_app_url='" + next_redirect_app_url + '\'' +
                    ", next_redirect_mobile_url='" + next_redirect_mobile_url + '\'' +
                    ", next_redirect_pc_url='" + next_redirect_pc_url + '\'' +
                    ", android_app_scheme='" + android_app_scheme + '\'' +
                    ", ios_app_scheme='" + ios_app_scheme + '\'' +
                    ", created_at='" + created_at + '\'' +
                    ", tms_result=" + tms_result +
                    '}';
        }
    }
}
