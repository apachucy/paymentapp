package com.payu.merchant

import androidx.multidex.MultiDexApplication
import com.payu.merchant.client.di.AppComponent
import com.payu.merchant.client.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class MerchantApplication: MultiDexApplication(), HasAndroidInjector {
    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
                .builder()
                .application(this)
                .build()
        appComponent.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}
