package com.gootax.feedme.domain.model

data class Address(
    val shortAddress: String,
    val fullAddress: String,
    val addressDetails: AddressDetails,
)
