package com.gootax.feedme.data.network

import com.gootax.feedme.data.dto.SearchRequest
import com.gootax.feedme.data.dto.SearchResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers(
        "Content-Type: application/json",
        "Accept: application/json",
        "Authorization: Token 7d40225da55f11e2d97b86de89aac7c5bd9b6b66"
    )

    @POST("suggestions/api/4_1/rs/suggest/address")
    suspend fun search(
        @Body request: SearchRequest
    ): SearchResponse
}
