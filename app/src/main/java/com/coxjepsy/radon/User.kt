package com.coxjepsy.radon

import com.android.installreferrer.api.ReferrerDetails
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val uid: String? = null,
    val name: String? = null,
    val email: String? = null,
    val utm_source: String? = null,
    val referral: ReferrerDetails? = null,
    val creationTimestamp: Long? = null) {

    // https://stackoverflow.com/questions/69468513/how-to-elegantly-create-a-map-with-only-non-null-values-in-kotlin
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "name" to name,
            "email" to email,
            "utm_source" to utm_source,
            "referral" to referral,
            "creationTimestamp" to creationTimestamp
        ).filterValues { it != null }
    }
}