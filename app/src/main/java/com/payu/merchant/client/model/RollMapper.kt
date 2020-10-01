package com.payu.merchant.client.model

import com.payu.merchant.client.api.model.ocr.Product


fun RollModel.toProduct() = Product(namePrice, "1", rollPrice.toString())