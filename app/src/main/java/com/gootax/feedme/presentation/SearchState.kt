package com.gootax.feedme.presentation

import com.gootax.feedme.domain.model.Address

sealed interface SearchState {
    object Loading: SearchState
    data class Content (val data: List<Address>): SearchState
    data class Error (val message: String): SearchState
}
