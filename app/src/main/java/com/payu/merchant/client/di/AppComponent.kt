package com.payu.merchant.client.di

import com.payu.merchant.client.repository.RepositoryModule
import com.payu.merchant.MerchantApplication
import com.payu.merchant.client.api.NetworkModule
import com.payu.merchant.client.config.PaymentMethodsProvider
import com.payu.merchant.client.ui.di.UiModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        NetworkModule::class,
        RepositoryModule::class,
        UiModule::class
    ]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: MerchantApplication): Builder

        fun build(): AppComponent
    }

    fun inject(application: MerchantApplication)

    fun inject(paymentMethodProvider: PaymentMethodsProvider)
}