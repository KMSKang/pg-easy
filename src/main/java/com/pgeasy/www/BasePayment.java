package com.pgeasy.www;

public interface BasePayment<T> {
    String getApiUrl();
    String getAuthorization(String secretKey);
    Class<BaseResult> getResultClass();
}
