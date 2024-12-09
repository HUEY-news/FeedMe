package com.gootax.feedme.data.model

import com.google.gson.annotations.SerializedName

data class AddressDTO(
    @SerializedName("value") val shortAddress: String,
    @SerializedName("unrestricted_value") val fullAddress: String,
    @SerializedName("data") val addressDetails: AddressDetailsDTO,
)
