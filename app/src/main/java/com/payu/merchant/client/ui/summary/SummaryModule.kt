package com.payu.merchant.client.ui.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.payu.merchant.client.repository.AuthenticationRepository
import com.payu.merchant.client.repository.PersistentRepository
import com.payu.merchant.client.ui.di.ViewModelKey
import com.payu.merchant.client.api.PayUApi
import com.payu.merchant.client.repository.PaymentMethodsRepository
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module(includes = [SummaryModule.Providers::class])
abstract class SummaryModule {
    @ContributesAndroidInjector(modules = [Injectors::class])
    abstract fun bind(): RollSummaryActivity

    @Module
    class Providers {
        @Provides
        @IntoMap
        @ViewModelKey(SummaryViewModel::class)
        fun provideSummaryViewModel(
            authRepository: AuthenticationRepository,
            paymentMethodsRepository: PaymentMethodsRepository,
            persistentRepository: PersistentRepository,
            api: PayUApi,
            gson: Gson
        ): ViewModel = SummaryViewModel(authRepository, paymentMethodsRepository, persistentRepository, api, gson)
    }

    @Module
    class Injectors {
        @Provides
        fun provideSummaryViewModel(factory: ViewModelProvider.Factory, target: RollSummaryActivity): SummaryViewModel
                = ViewModelProvider(target, factory).get(SummaryViewModel::class.java)
    }
}