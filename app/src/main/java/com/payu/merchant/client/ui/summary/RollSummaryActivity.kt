package com.payu.merchant.client.ui.summary

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.payu.android.front.sdk.payment_add_card_module.service.CvvValidationService
import com.payu.android.front.sdk.payment_library_card_scanner.scanner.pay.card.PayCardScanner
import com.payu.android.front.sdk.payment_library_core_android.events.AuthorizationDetails
import com.payu.android.front.sdk.payment_library_google_pay_adapter.GooglePayAdapter
import com.payu.android.front.sdk.payment_library_google_pay_module.builder.Cart
import com.payu.android.front.sdk.payment_library_google_pay_module.model.Currency
import com.payu.android.front.sdk.payment_library_google_pay_module.service.GooglePayService
import com.payu.android.front.sdk.payment_library_payment_chooser.payment_method.external.service.BlikAmbiguityService
import com.payu.android.front.sdk.payment_library_payment_methods.model.PaymentMethod
import com.payu.android.front.sdk.payment_library_webview_module.web.authorization.WebPaymentStatus
import com.payu.android.front.sdk.payment_library_webview_module.web.service.WebPaymentService
import com.payu.merchant.client.R
import com.payu.merchant.client.databinding.ActivityRollSummaryBinding
import com.payu.merchant.client.repository.ProductRepository
import com.payu.merchant.client.ui.base.ActivityWithMenu
import dagger.android.AndroidInjection
import javax.inject.Inject

private val TAG = RollSummaryActivity::class.java.name

class RollSummaryActivity : ActivityWithMenu() {
    @Inject
    lateinit var viewModel: SummaryViewModel

    private lateinit var binding: ActivityRollSummaryBinding

    private lateinit var googlePayService: GooglePayService

    private fun setupToolbar() {
        setSupportActionBar(binding.payuToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun populateData() {
        //get item from list - 1rst item
        val rollModel = ProductRepository().provideData()[0]
        viewModel.rollModel = rollModel
        viewModel.paymentChooserController = getPaymentChooserController()

        googlePayService = GooglePayService(this)
    }

    private fun initPaymentChooserWidget() {
        //Additional configuration for Widget
        //Turn on Google Pay Payments
        binding.paymentChooserWidget.setPaymentMethodsAdapter(GooglePayAdapter(googlePayService))
        //Turn on use Card Scanner in Card Scanner Widget Activity
        binding.paymentChooserWidget.setCardScannerAPI(PayCardScanner())
    }

    private fun getPaymentChooserController() = object : PaymentChooserController {
        override val isPaymentMethodSelected: Boolean
            get() = binding.paymentChooserWidget.isPaymentMethodSelected
        override val isBlikAuthorizationCodeNeeded: Boolean
            get() = binding.paymentChooserWidget.isBlikAuthorizationCodeNeeded
        override val blikAuthorizationCode: String?
            get() = binding.paymentChooserWidget.blikAuthorizationCode
        override val isBlikAuthorizationCodeProvided: Boolean
            get() = binding.paymentChooserWidget.isBlikAuthorizationCodeProvided

        override fun getPaymentMethod(): PaymentMethod? =
            binding.paymentChooserWidget.paymentMethod.value

        override fun cleanPaymentMethods() {
            binding.paymentChooserWidget.cleanPaymentMethods()
        }
    }


    private fun observeMessageSuccessEvent() {
        viewModel.paymentSuccessEvent.observe(this, Observer { messageStringRes ->
            messageStringRes?.let { showSuccess(it) }
        })
    }

    private fun observeMessageErrorEvent() {
        viewModel.paymentErrorEvent.observe(this, Observer { messageStringRes ->
            messageStringRes?.let { showError(it) }
        })
    }

    private fun observeCvvValidationEvent() {
        viewModel.cvvValidationEvent.observe(this, Observer { paymentUrl ->
            paymentUrl?.let { handleCvvValidation(it) }
        })
    }

    private fun observeGooglePayEvent() {
        viewModel.googlePayEvent.observe(this, Observer { posId ->
            posId?.let { googlePayService.requestGooglePayCard(createCart(), it, "PayU") }
        })
    }

    private fun observeBlikAmbiguityEvent() {
        viewModel.blikAmbiguityEvent.observe(this, Observer {
            BlikAmbiguityService.selectAmbiguityBlik(this)
        })
    }

    private fun observePaymentEvent() {
        viewModel.paymentEvent.observe(this, Observer { authorizationDetails ->
            authorizationDetails?.let { WebPaymentService.pay(this, it) }
        })
    }


    private fun observePaymentMethodChange() {
        binding.paymentChooserWidget.paymentMethod.observe(this, Observer { paymentMethod ->
            viewModel.showClearPaymentButton.set(paymentMethod != null)
            binding.paymentButton.isEnabled = paymentMethod != null
        })
    }

    private fun showToast(@StringRes stringRes: Int) {
        showToast(getString(stringRes))
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showSuccess(@StringRes stringRes: Int) {
        showSuccess(getString(stringRes))
    }

    private fun showSuccess(message: String) {
        showToast(message)
        binding.success.visibility = View.VISIBLE
        binding.success.postDelayed(
            {
                binding.success.visibility = View.GONE
                viewModel.showProgress.set(false)
            },
            2000
        )
    }

    private fun showError(@StringRes stringRes: Int) {
        showError(getString(stringRes))
    }

    private fun showError(message: String) {
        showToast(message)
        binding.error.visibility = View.VISIBLE
        binding.error.postDelayed(
            {
                binding.error.visibility = View.GONE
                viewModel.showProgress.set(false)
            },
            2000
        )
    }

    private fun createCart() = viewModel.rollModel?.rollPrice?.let { price ->
        Cart.Builder()
            .withTotalPrice(price)
            .withCurrency(Currency.PLN)
            .build()
    } ?: Cart.Builder().build()

    private fun handlePaymentResult(requestCode: Int, data: Intent, resultCode: Int) {
        when (requestCode) {
            WebPaymentService.REQUEST_CODE -> handleWebPaymentResult(data)
            BlikAmbiguityService.REQUEST_CODE -> {
                val paymentMethod = BlikAmbiguityService.extractSelectedBlikResult(data)
                paymentMethod?.let {
                    viewModel.createOrder(it)
                    showToast(getString(R.string.payment_blik_ambiguity, it.toString()))
                }
            }
            GooglePayService.REQUEST_CODE_GOOGLE_PAY_PAYMENT -> handleGooglePayResult(
                resultCode,
                data
            )
        }
    }

    private fun handleWebPaymentResult(data: Intent) {
        val paymentDetails = WebPaymentService.extractPaymentResult(data)
        paymentDetails?.toString()?.let { Log.v(TAG, it) }
        when (paymentDetails?.webPaymentStatus) {
            WebPaymentStatus.SUCCESS -> showSuccess(R.string.payment_status_success)
            WebPaymentStatus.WARNING_CONTINUE_CVV -> {
                showToast(R.string.payment_status_warning_continue_cvv)
                handleCvvValidation(viewModel.paymentUrl)
            }
            WebPaymentStatus.CANCEL_PAYMENT -> showError(R.string.payment_cancel)
            WebPaymentStatus.PAYMENT_ERROR,
            WebPaymentStatus.SSL_VALIDATION_ERROR -> showError(R.string.payment_status_error)
            else -> showError(R.string.payment_status_error)
        }
    }

    private fun handleCvvValidation(cvvPaymentUrl: String) {
        CvvValidationService.validateCvv(
            this,
            AuthorizationDetails.Builder()
                .withLink(cvvPaymentUrl)
                .build()
        ) { showSuccess("Cvv validation completed!") }
    }

    private fun handleGooglePayResult(resultCode: Int, data: Intent) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                val googlePayTokenResponse = googlePayService.handleGooglePayResultData(data)
                binding.paymentChooserWidget.paymentMethod.value.let {
                    if (it != null) {
                        viewModel.createOrder(it, googlePayTokenResponse?.googlePayToken)
                    }
                }
            }
            Activity.RESULT_CANCELED -> {
                //User has canceled payment
                showError("Payment canceled")
            }
            GooglePayService.RESULT_ERROR -> {
                val status = googlePayService.handleGooglePayErrorStatus(data)
                showError(status.errorMessage ?: "GooglePay Error")
            }
            else -> showError("Payment process has failed for the user")
        }
    }

    override fun onDestroy() {
        Log.v("Damian", "OnDestroy Called")
        super.onDestroy()
    }

    override fun onStart() {
        Log.v("Damian", "onStart Called")
        super.onStart()
    }

    override fun onStop() {
        Log.v("Damian", "onStop Called")
        super.onStop()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v("Damian", "OnCreate Called")
        AndroidInjection.inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_roll_summary)
        binding.model = viewModel

        setupToolbar()
        viewModel.login()
        viewModel.loginEvent.observe(this, Observer { loginSuccessful ->
            if (loginSuccessful != true) {
                Snackbar.make(
                    binding.paymentChooserWidget,
                    R.string.login_failed,
                    Snackbar.LENGTH_LONG
                ).show()
                finish()
            } else {
                populateData()
                initPaymentChooserWidget()


                observeMessageSuccessEvent()
                observeMessageErrorEvent()
                observeCvvValidationEvent()
                observeGooglePayEvent()
                observeBlikAmbiguityEvent()
                observePaymentEvent()

                observePaymentMethodChange()
            }
        })


    }


    /**
     * Handling response form  [WebPaymentService]
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //display additional information when last module was WebPayment type
        data?.let {
            handlePaymentResult(requestCode, it, resultCode)
        } ?: super.onActivityResult(requestCode, resultCode, data)
    }
}
