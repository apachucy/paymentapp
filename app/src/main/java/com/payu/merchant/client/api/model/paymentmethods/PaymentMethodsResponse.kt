package com.payu.merchant.client.api.model.paymentmethods

import com.google.gson.annotations.SerializedName

data class PaymentMethodsResponse(
    @SerializedName("blikTokens")
    val blikTokens: List<BlikTokens>?,
    @SerializedName("cardTokens")
    val cardTokens: List<CardTokens>,
    @SerializedName("pexTokens")
    val pexTokens: List<PexTokens>,
    @SerializedName("payByLinks")
    val payByLinks: List<PayByLinks>
)