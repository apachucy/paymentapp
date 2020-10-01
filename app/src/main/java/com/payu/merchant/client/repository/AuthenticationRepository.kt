package com.payu.merchant.client.repository

class AuthenticationRepository {
    var accessToken: String = ""
        get() = if (!field.contains("Bearer ", true)) "Bearer $field" else field

    var posId: String = ""
}