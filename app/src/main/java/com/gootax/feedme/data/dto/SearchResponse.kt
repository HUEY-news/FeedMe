package com.gootax.feedme.data.dto

import com.google.gson.annotations.SerializedName
import com.gootax.feedme.data.model.AddressDTO

data class SearchResponse(
    @SerializedName("suggestions") val suggestions: List<AddressDTO>
) : Response()
