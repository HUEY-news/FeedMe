package com.gootax.feedme.data.model

import com.google.gson.annotations.SerializedName

data class AddressDetailsDTO(
    @SerializedName("city_fias_id") val id: String?,
    @SerializedName("street_with_type") val street: String?,
    @SerializedName("settlement") val settlement: String?,
    @SerializedName("city_with_type") val city: String?,
    @SerializedName("region_with_type") val region: String?,
    @SerializedName("country") val country: String?
)
