package com.pgeasy.www;

public enum PaymentStatus {
    // 결제 대기 중: 결제 생성 후 구매자의 결제 진행을 대기
    PAY_STANDBY,

    // 구매자 인증 완료: 결제 인증이 완료되고 가맹점 승인 대기
    PAY_APPROVED,

    // 결제 취소: 결제가 완료되기 전에 취소된 상태
    PAY_CANCEL,

    // 결제 진행 중: 구매자가 결제를 승인한 후 출금 처리 중인 상태
    PAY_PROGRESS,

    // 결제 완료: 구매자와 가맹점의 승인 및 출금이 정상적으로 완료된 상태
    PAY_COMPLETE,

    // 환불 진행 중: 전액 또는 부분 환불을 진행 중인 상태
    REFUND_PROGRESS,

    // 환불 성공: 환불 금액이 구매자의 계좌로 입금 완료된 상태
    REFUND_SUCCESS,

    // 정산 완료: 결제 금액 정산이 완료되어 더 이상 환불 불가한 상태
    SETTLEMENT_COMPLETE,

    // 환불 정산 완료: 환불 금액 정산이 완료되어 더 이상 환불 불가한 상태
    SETTLEMENT_REFUND_COMPLETE;
}
