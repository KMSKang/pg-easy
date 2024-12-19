package com.pgeasy.www;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Builder
public class TossPaymentApprove implements BasePaymentApprove {
    private final String apiUrl = "https://api.tosspayments.com/v1/payments/confirm";
    private String paymentKey;
    private String orderId;
    private Integer amount;
    private Result result;

    @Override
    public String getApiUrl() {
        return apiUrl;
    }

    @Override
    public String getAuthorization(String secretKey) {
        return "Basic " + Base64.getEncoder().encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public Class<Result> getResultClass() {
        return Result.class;
    }

    public record Result(
            @JsonProperty("mId") String mId,
            String lastTransactionKey,
            String paymentKey,
            String orderId,
            String orderName,
            Integer taxExemptionAmount,
            String status,
            String requestedAt,
            String approvedAt,
            Boolean useEscrow,
            Boolean cultureExpense,
            String card,
            String virtualAccount,
            String transfer,
            String mobilePhone,
            String giftCertificate,
            String cashReceipt,
            String cashReceipts,
            String discount,
            String cancels,
            String secret,
            String type,
            EasyPay easyPay,
            String country,
            String failure,
            Boolean isPartialCancelable,
            Receipt receipt,
            Checkout checkout,
            String currency,
            Integer totalAmount,
            Integer balanceAmount,
            Integer suppliedAmount,
            Integer vat,
            Integer taxFreeAmount,
            String method,
            String version,
            String metadata) implements BaseResult {
        public record EasyPay(String provider, Integer amount, Integer discountAmount) {
        }

        public record Receipt(String url) {
        }

        public record Checkout(String url) {
        }
    }
}
