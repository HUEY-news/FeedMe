package com.gootax.feedme.domain.api

import com.gootax.feedme.domain.model.Address
import kotlinx.coroutines.flow.Flow

interface Interactor {
    fun search(query: String): Flow<Pair<List<Address>?, String?>>
}
