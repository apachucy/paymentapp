package com.payu.merchant.client.api.model.paymentmethods

import com.google.gson.annotations.SerializedName

data class BlikTokens(
    @SerializedName("value")
    val value: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("brandImageUrl")
    val brandImageUrl: String
)