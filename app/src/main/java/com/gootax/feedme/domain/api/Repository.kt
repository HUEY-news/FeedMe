package com.gootax.feedme.domain.api

import com.gootax.feedme.domain.model.Address
import com.gootax.feedme.util.Resource
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun search(query: String): Flow<Resource<List<Address>>>
}
