package com.gootax.feedme.domain.model

data class AddressDetails(
    val id: String,
    val house: String,
    val street: String,
    val settlement: String,
    val city: String,
    val region: String,
    val country: String
)
