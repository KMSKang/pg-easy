package com.pgeasy.www;

public interface PgPaymentService {
    CommonResponse<BaseResult> paymentModule(PaymentModule paymentModule);
    CommonResponse<BaseResult> paymentApprove(PaymentApprove approvePayment);
    CommonResponse<BaseResult> paymentCancel(PaymentCancel paymentCancel);
}
