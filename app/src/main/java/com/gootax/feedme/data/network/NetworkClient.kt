package com.gootax.feedme.data.network

import com.gootax.feedme.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}
