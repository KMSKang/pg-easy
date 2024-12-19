package com.pgeasy.www;

import lombok.Builder;

@Builder
public record PaymentApprove(String secretKey, BasePaymentApprove basePaymentApprove) {

}
