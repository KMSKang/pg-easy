package com.pgeasy.www;

import lombok.Builder;

@Builder
public record PaymentModule(String secretKey, BasePaymentModule basePaymentModule) {
}
