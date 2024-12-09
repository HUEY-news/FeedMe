package com.gootax.feedme.data.impl

import com.gootax.feedme.converter.Converter
import com.gootax.feedme.data.dto.SearchRequest
import com.gootax.feedme.data.dto.SearchResponse
import com.gootax.feedme.data.network.NetworkClient
import com.gootax.feedme.domain.api.Repository
import com.gootax.feedme.domain.model.Address
import com.gootax.feedme.util.Constants
import com.gootax.feedme.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val client: NetworkClient,
    private val converter: Converter
) : Repository {

    override fun search(query: String): Flow<Resource<List<Address>>> = flow {
        val response = client.doRequest(SearchRequest(query))
        when (response.resultCode) {
            -1 -> emit(Resource.Error(Constants.ERROR_INTERNET_TEXT))
            200 -> {
                val suggestions = converter.map((response as SearchResponse).suggestions)
                if (suggestions.isNotEmpty()) emit(Resource.Success(suggestions))
                else emit(Resource.Error(Constants.ERROR_EMPTY_TEXT))
            }
            else -> emit(Resource.Error(Constants.ERROR_SERVER_TEXT))
        }
    }
}
