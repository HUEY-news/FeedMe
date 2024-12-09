package com.gootax.feedme.data.model

import com.google.gson.annotations.SerializedName

data class AddressDetailsDTO(
    @SerializedName("city_with_type") val city: String,
    @SerializedName("region_with_type") val region: String,
    @SerializedName("country") val country: String
)
