package com.pgeasy.www;

import lombok.Builder;

@Builder
public record PaymentCancel(String secretKey, BasePaymentCancel basePaymentCancel) {

}
