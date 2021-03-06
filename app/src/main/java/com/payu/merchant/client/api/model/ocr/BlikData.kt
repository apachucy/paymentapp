package com.payu.merchant.client.api.model.ocr

import com.google.gson.annotations.SerializedName

data class BlikData(
    @SerializedName("register")
    val register: Boolean = true,
    @SerializedName("appKey")
    val appKey: String? = null
)