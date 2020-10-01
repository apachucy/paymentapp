package com.payu.merchant.client.ui.base.binding

import androidx.databinding.ObservableField


class ObservableString : ObservableField<String> {
    constructor() : super()

    constructor(text: String) : super(text)
}