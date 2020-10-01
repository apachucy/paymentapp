package com.payu.merchant.client.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RollModel(
       // @DrawableRes val drawableImage: Int,
        val rollPriceString: String,
        val rollPrice: Int,
        val namePrice: String
) : Parcelable
