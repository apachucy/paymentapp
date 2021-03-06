package com.payu.merchant.client.api


import com.payu.merchant.client.api.model.AuthenticateResponse
import com.payu.merchant.client.api.model.ocr.CreateOrderRequest
import com.payu.merchant.client.api.model.ocr.OrderCreateResponse
import com.payu.merchant.client.api.model.paymentmethods.PaymentMethodsResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

interface PayUApi {
    @POST("/pl/standard/user/oauth/authorize?grant_type=client_credentials")
    fun authenticate(
            @Query("client_id") clientId: String,
            @Query("client_secret") clientSecret: String
    ): Single<AuthenticateResponse>

    @POST("/api/v2_1/orders")
    fun createOrder(
            @Header("Authorization") token: String,
            @Body createOrderRequest: CreateOrderRequest
    ): Single<Response<OrderCreateResponse>>

    @GET("api/v2_1/paymethods/")
    fun retrievePaymentMethods(@Header("Authorization") token: String): Single<PaymentMethodsResponse>
}