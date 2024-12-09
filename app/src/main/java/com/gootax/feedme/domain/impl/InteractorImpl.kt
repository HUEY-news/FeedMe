package com.gootax.feedme.domain.impl

import com.gootax.feedme.domain.api.Interactor
import com.gootax.feedme.domain.api.Repository
import com.gootax.feedme.domain.model.Address
import com.gootax.feedme.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class InteractorImpl @Inject constructor(
    private val repository: Repository
) : Interactor {
    override fun search(query: String): Flow<Pair<List<Address>?, String?>> {
        return repository.search(query).map { result ->
            when (result) {
                is Resource.Success -> Pair(result.data, null)
                is Resource.Error -> Pair(null, result.message)
            }
        }
    }
}
