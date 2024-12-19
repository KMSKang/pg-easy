package com.examples.www.kakao;

import lombok.Builder;

@Builder
public record KaKaoPaymentCancelRequest(String cid, String cid_secret, String tid, Integer cancel_amount, Integer cancel_tax_free_amount, Integer cancel_vat_amount, Integer cancel_available_amount, String payload) {
}
