package com.pgeasy.www;

import lombok.Builder;

@Builder
public class KaKaoPaymentModule implements BasePaymentModule {
    private final String apiUrl = "https://open-api.kakaopay.com/online/v1/payment/ready";
    private String cid;
    private Long partner_order_id;
    private String partner_user_id;
    private String item_name;
    private Integer quantity;
    private Integer total_amount;
    private Integer tax_free_amount;
    private String approval_url;
    private String cancel_url;
    private String fail_url;

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
            String next_redirect_pc_url,
            String next_redirect_mobile_url,
            String next_redirect_app_url,
            String created_at,
            String ios_app_scheme,
            String android_app_scheme,
            boolean tms_result,
            String tid) implements BaseResult {
        @Override
        public String toString() {
            return "Result{" +
                    "next_redirect_pc_url='" + next_redirect_pc_url + '\'' +
                    ", next_redirect_mobile_url='" + next_redirect_mobile_url + '\'' +
                    ", next_redirect_app_url='" + next_redirect_app_url + '\'' +
                    ", created_at='" + created_at + '\'' +
                    ", ios_app_scheme='" + ios_app_scheme + '\'' +
                    ", android_app_scheme='" + android_app_scheme + '\'' +
                    ", tms_result=" + tms_result +
                    ", tid='" + tid + '\'' +
                    '}';
        }
    }
}
