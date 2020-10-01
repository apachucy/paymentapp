package com.payu.merchant.client.config

import android.content.Context
import com.payu.merchant.client.repository.AuthenticationRepository
import com.payu.android.front.sdk.payment_library_payment_chooser.payment_method.external.listener.PaymentMethodsCallback
import com.payu.android.front.sdk.payment_library_payment_chooser.payment_method.external.listener.PosIdListener
import com.payu.android.front.sdk.payment_library_payment_chooser.payment_method.internal.providers.PaymentMethodActions
import com.payu.android.front.sdk.payment_library_payment_methods.model.PaymentMethod
import com.payu.merchant.MerchantApplication
import com.payu.merchant.client.repository.PaymentMethodsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Payment list to populate test application
 */
class PaymentMethodsProvider(context: Context) : PaymentMethodActions(context) {
    @Inject
    lateinit var paymentMethodsRepository: PaymentMethodsRepository

    @Inject
    lateinit var authenticationRepository: AuthenticationRepository

    init {
        val demoApplication = context.applicationContext as MerchantApplication
        demoApplication.appComponent.inject(this)
    }


    override fun providePaymentMethods(callback: PaymentMethodsCallback) {
        val disposable = paymentMethodsRepository.getPaymentMethods()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { callback.onFetched(it) },
                {
                    println("Error during fetching payment methods: $it")
                }
            )
    }

    override fun onPaymentMethodRemoved(paymentMethod: PaymentMethod) {
        //call to backend to remove  payment method
    }

    override fun providePosId(posIdListener: PosIdListener) {
        posIdListener.onPosId(authenticationRepository.posId)
    }

    override fun provideBlikPaymentMethods(callback: PaymentMethodsCallback) {
        val disposable = paymentMethodsRepository.getBlikPaymentMethods()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { callback.onFetched(it) },
                {
                    println("Error during fetching blik payment methods: $it")
                }
            )
    }
}
