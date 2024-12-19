package com.examples.www.toss;

import lombok.Builder;

@Builder
public record TossConfirmPaymentRequest(String paymentKey, String orderId, Integer amount) {

}
