package com.payu.merchant.client.api.model.ocr

import com.google.gson.annotations.SerializedName
import com.payu.merchant.client.api.model.ocr.BlikData

data class PayMethod(
    @SerializedName("type")
    val type: String,
    @SerializedName("value")
    val value: String,
    @SerializedName("authorizationCode")
    val authorizationCode: String? = null,
    @SerializedName("blikData")
    val blikData: BlikData? = null
)