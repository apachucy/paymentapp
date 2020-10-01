package com.payu.merchant.client.di

import android.content.Context
import com.payu.merchant.MerchantApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    fun provideContext(application: MerchantApplication): Context {
        return application
    }
}