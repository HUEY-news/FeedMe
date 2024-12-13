package com.gootax.feedme.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gootax.feedme.domain.api.Interactor
import com.gootax.feedme.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val interactor: Interactor
) : ViewModel() {

    private var searchState = MutableLiveData<SearchState>()
    fun getSearchState(): LiveData<SearchState> = searchState
    private fun renderState(state: SearchState) { searchState.postValue(state) }

    private fun search(query: String) {
        if (query.isNotEmpty()) {
            viewModelScope.launch {
                interactor
                    .search(query)
                    .collect { pair ->
                        if (pair.first != null) renderState(SearchState.Content(pair.first!!))
                        if (pair.second != null) renderState(SearchState.Error(pair.second!!))
                    }
            }
        } else renderState(SearchState.Content(listOf()))
    }

    private var lastQuery: String? = null
    private var searchJob: Job? = null

    fun searchDebounce(text: String) {
        if (lastQuery != text) {
            lastQuery = text
            val currentQuery = lastQuery ?: ""
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                delay(Constants.SEARCH_DEBOUNCE_DELAY)
                search(currentQuery)
            }
        }
    }
}
