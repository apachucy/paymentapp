package com.payu.merchant.client.repository

import android.content.Context

private const val SETTINGS_NAME = "appSettings"
private const val SETTINGS_POSID_KEY = "posid"
private const val SETTINGS_CLIENT_SECRET_KEY = "client_secret"
private const val SETTINGS_SAVE_CREDENTIALS_KEY = "save_credentials"

// Production credentials
//TODO: for login purposes please change this values
private const val POS_ID_DEFAULT_VALUE = "wrong POS" //wrong POS
private const val CLIENT_SECRET_DEFAULT_VALUE = "wrong Secret" //wrong secret

class PersistentRepository(private val context: Context) {

    private fun getSettings() = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE)
    var saveCredentials: Boolean
        get() = getSettings().getBoolean(SETTINGS_SAVE_CREDENTIALS_KEY, false)
        set(value) {
            getSettings().edit().putBoolean(SETTINGS_SAVE_CREDENTIALS_KEY, value).apply()
        }

    var posid: String
        get() = getSettings().getString(SETTINGS_POSID_KEY, POS_ID_DEFAULT_VALUE)
            ?.ifEmpty { POS_ID_DEFAULT_VALUE }
            ?: POS_ID_DEFAULT_VALUE
        set(value) {
            getSettings().edit().putString(SETTINGS_POSID_KEY, value).apply()
        }

    var clientSecret
        get() = getSettings().getString(SETTINGS_CLIENT_SECRET_KEY, CLIENT_SECRET_DEFAULT_VALUE)
            ?.ifEmpty { CLIENT_SECRET_DEFAULT_VALUE }
            ?: CLIENT_SECRET_DEFAULT_VALUE
        set(value) {
            getSettings().edit().putString(SETTINGS_CLIENT_SECRET_KEY, value).apply()
        }
}
