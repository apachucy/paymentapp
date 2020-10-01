package com.payu.merchant.client.api.model.ocr

import com.google.gson.annotations.SerializedName

data class PayMethods(
    @SerializedName("payMethod")
    val payMethod: PayMethod
)