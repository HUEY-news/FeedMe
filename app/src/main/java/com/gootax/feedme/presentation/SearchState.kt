package com.gootax.feedme.presentation

import com.gootax.feedme.domain.model.Address

sealed interface SearchState {
    data class Content (val list: List<Address>): SearchState
    data class Error (val message: String): SearchState
}
