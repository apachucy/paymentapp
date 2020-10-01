package com.payu.merchant.client.config;

import com.payu.android.front.sdk.payment_library_core_android.configuration.DefaultStyleConfiguration;
import com.payu.merchant.client.R;

public class CustomPayUStyle extends DefaultStyleConfiguration {


    @Override
    public int payuStyle() {
        return R.style.MerchantStyle;
    }

 }
