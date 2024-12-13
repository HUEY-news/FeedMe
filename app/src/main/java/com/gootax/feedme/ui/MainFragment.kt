package com.gootax.feedme.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.gootax.feedme.databinding.FragmentMainBinding
import com.gootax.feedme.domain.model.Address
import com.gootax.feedme.presentation.MainViewModel
import com.gootax.feedme.presentation.SearchState
import com.gootax.feedme.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
    private var searchTextWatcher: TextWatcher? = null
    private var addressTextWatcher: TextWatcher? = null
    private var adapter: Adapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomSheet()
        setupRecyclerView()
        setupTextWatchers()
        setupClickListeners()

        viewModel.getSearchState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is SearchState.Content -> updateSearchResult(state.list)
                is SearchState.Error -> Log.e("TEST", "Error: ${state.message}")
            }
        }
    }

    private fun updateSearchResult(list: List<Address>) { adapter?.submitList(list) }

    private fun setupBottomSheet() {
        val bottomSheetBehavior =
            BottomSheetBehavior.from(binding.bottomSheetLayout).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }

        val peekHeight: Int = (resources.displayMetrics.heightPixels * 0.9).toInt()
        bottomSheetBehavior.peekHeight = peekHeight

        bottomSheetBehavior.addBottomSheetCallback(
            object: BottomSheetBehavior.BottomSheetCallback() {

                override fun onStateChanged(bottomSheet: View, newState: Int) {

                    if (newState == BottomSheetBehavior.STATE_EXPANDED)
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            binding.overlay.isVisible = false
                        }
                        else -> {
                            binding.overlay.isVisible = true
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    binding.overlay.alpha = slideOffset + 1
                }
            })

        binding.addressButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                } else findNavController().navigateUp()
            }
        })
    }
    private fun setupRecyclerView() {
        adapter = Adapter { address: Address -> onClickDebounce(address) }
        binding.recycler.adapter = adapter
    }
    private fun setupTextWatchers() {
        searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                binding.searchIcon.isVisible = text.isNullOrEmpty()
            }
        }
        searchTextWatcher?.let { watcher -> binding.searchField.addTextChangedListener(watcher) }

        addressTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrEmpty()) updateSearchResult(listOf())
                viewModel.searchDebounce(text = text?.toString() ?: "")
            }
        }
        addressTextWatcher?.let { watcher -> binding.addressSearchField.addTextChangedListener(watcher) }
    }
    private fun setupClickListeners() {
        binding.addressResetButton.setOnClickListener {
            binding.addressSearchField.setText("")
            updateSearchResult(listOf())
            val inputMethodManager = requireContext()
                .getSystemService(
                    Context.INPUT_METHOD_SERVICE
                ) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(
                binding.addressSearchField.windowToken,
                0
            )
        }
    }

    private var isClickAllowed = true

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            lifecycleScope.launch {
                delay(Constants.CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    private fun onClickDebounce(address: Address) {
        if (clickDebounce()) {
            // TODO: реализовать реакцию на выбор адреса!
        }
    }
}
