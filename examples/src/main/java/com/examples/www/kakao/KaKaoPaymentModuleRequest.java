package com.examples.www.kakao;

import lombok.Builder;

@Builder
public record KaKaoPaymentModuleRequest(Long orderId, String userId, String itemName, Integer quantity, Integer totalAmount) {

}
