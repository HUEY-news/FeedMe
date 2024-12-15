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
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
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
    val bottomSheetBehavior by lazy {
        BottomSheetBehavior.from(binding.bottomSheetLayout).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomSheet()
        setupRecycler()
        setupTextWatchers()
        setupClickListeners()

        viewModel.getSearchState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is SearchState.Content -> updateSearchResult(state.list)
                is SearchState.Error -> Log.e("TEST", "Error: ${state.message}")
            }
        }
    }

    private fun updateSearchResult(list: List<Address>) {
        adapter?.submitList(list)
    }

    private fun setupBottomSheet() {
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
                            hideKeyboard()
                        }
                        else -> {
                            binding.overlay.isVisible = true
                            hideKeyboard()
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    binding.overlay.alpha = slideOffset + 1
                }
            })

        binding.addressButton.setOnClickListener {
            Log.w("TEST", "addressButton clicked!")
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN) bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                else if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) binding.drawerLayout.closeDrawer(GravityCompat.START)
                else findNavController().navigateUp()
            }
        })
    }
    private fun setupRecycler() {
        adapter = Adapter { address: Address ->
            Log.w("TEST", "${address.shortAddress} clicked!")
            onClickDebounce(address)
        }

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
            Log.w("TEST", "addressResetButton clicked!")
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

        with(binding) {
            menuButton.setOnClickListener {
                Log.w("TEST", "menuButton clicked!")
                if (!drawerLayout.isDrawerOpen(GravityCompat.START))
                    drawerLayout.openDrawer(GravityCompat.START)
            }

            favoriteButton.setOnClickListener { Log.w("TEST", "favoriteButton clicked!") }

            promoSection1.setOnClickListener { Log.w("TEST", "promoSection1 clicked!") }
            promoSection2.setOnClickListener { Log.w("TEST", "promoSection2 clicked!") }
            promoSection3.setOnClickListener { Log.w("TEST", "promoSection3 clicked!") }
            promoSection4.setOnClickListener { Log.w("TEST", "promoSection4 clicked!") }
            promoSection5.setOnClickListener { Log.w("TEST", "promoSection5 clicked!") }
            promoSection6.setOnClickListener { Log.w("TEST", "promoSection6 clicked!") }
            promoSection7.setOnClickListener { Log.w("TEST", "promoSection7 clicked!") }

            promoBanner1.setOnClickListener { Log.w("TEST", "promoBanner1 clicked!") }
            promoBanner2.setOnClickListener { Log.w("TEST", "promoBanner2 clicked!") }
            promoBanner3.setOnClickListener { Log.w("TEST", "promoBanner3 clicked!") }
            promoBanner4.setOnClickListener { Log.w("TEST", "promoBanner4 clicked!") }
            promoBanner5.setOnClickListener { Log.w("TEST", "promoBanner5 clicked!") }
            promoBanner6.setOnClickListener { Log.w("TEST", "promoBanner6 clicked!") }
            promoBanner7.setOnClickListener { Log.w("TEST", "promoBanner7 clicked!") }

            showAllButton.setOnClickListener { Log.w("TEST", "showAllButton clicked!") }

            imageDish1.setOnClickListener { Log.w("TEST", "imageDish1 clicked!") }
            imageDish2.setOnClickListener { Log.w("TEST", "imageDish2 clicked!") }
            imageDish3.setOnClickListener { Log.w("TEST", "imageDish3 clicked!") }
            imageDish4.setOnClickListener { Log.w("TEST", "imageDish4 clicked!") }
            imageDish5.setOnClickListener { Log.w("TEST", "imageDish5 clicked!") }
            imageDish6.setOnClickListener { Log.w("TEST", "imageDish6 clicked!") }
            imageDish7.setOnClickListener { Log.w("TEST", "imageDish7 clicked!") }

            minusButton1.setOnClickListener { Log.w("TEST", "minusButton1 clicked!") }
            plusButton1.setOnClickListener { Log.w("TEST", "plusButton1 clicked!") }
            plusYellowButton2.setOnClickListener { Log.w("TEST", "plusYellowButton2 clicked!") }
            plusYellowButton3.setOnClickListener { Log.w("TEST", "plusYellowButton3 clicked!") }
            plusYellowButton4.setOnClickListener { Log.w("TEST", "plusYellowButton4 clicked!") }
            plusYellowButton5.setOnClickListener { Log.w("TEST", "plusYellowButton5 clicked!") }
            plusYellowButton6.setOnClickListener { Log.w("TEST", "plusYellowButton6 clicked!") }
            plusYellowButton7.setOnClickListener { Log.w("TEST", "plusYellowButton7 clicked!") }

            catalogImage1.setOnClickListener { Log.w("TEST", "catalogImage1 clicked!") }
            catalogImage2.setOnClickListener { Log.w("TEST", "catalogImage2 clicked!") }
            catalogImage3.setOnClickListener { Log.w("TEST", "catalogImage3 clicked!") }
            catalogImage4.setOnClickListener { Log.w("TEST", "catalogImage4 clicked!") }
            catalogImage5.setOnClickListener { Log.w("TEST", "catalogImage5 clicked!") }
            catalogImage6.setOnClickListener { Log.w("TEST", "catalogImage6 clicked!") }
            catalogImage7.setOnClickListener { Log.w("TEST", "catalogImage7 clicked!") }
            catalogImage8.setOnClickListener { Log.w("TEST", "catalogImage8 clicked!") }
            catalogImage9.setOnClickListener { Log.w("TEST", "catalogImage9 clicked!") }

            currentLocationText.setOnClickListener { Log.w("TEST", "currentLocationText clicked!") }

            profileImage.setOnClickListener { Log.w("TEST", "profileImage clicked!") }
            profileName.setOnClickListener { Log.w("TEST", "profileName clicked!") }
            profilePhoneNumber.setOnClickListener { Log.w("TEST", "profilePhoneNumber clicked!") }
            profilePaymentText.setOnClickListener { Log.w("TEST", "profilePaymentText clicked!") }
            profileCreditCardText.setOnClickListener { Log.w("TEST", "profileCreditCardText clicked!") }
            profileMyLocationsText.setOnClickListener { Log.w("TEST", "profileMyLocationsText clicked!") }
            profileMyOrdersText.setOnClickListener { Log.w("TEST", "profileMyOrdersText clicked!") }
            profileFavoritesText.setOnClickListener { Log.w("TEST", "profileFavoritesText clicked!") }
            profileNewsText.setOnClickListener { Log.w("TEST", "profileNewsText clicked!") }
            profileCouponsText.setOnClickListener { Log.w("TEST", "profileCouponsText clicked!") }
            profileAboutText.setOnClickListener { Log.w("TEST", "profileAboutText clicked!") }
            profileInviteText.setOnClickListener { Log.w("TEST", "profileInviteText clicked!") }
            profileSettingsText.setOnClickListener { Log.w("TEST", "profileSettingsText clicked!") }
            profileContactUsIcon.setOnClickListener { Log.w("TEST", "profileContactUsIcon clicked!") }
            profileContactUsText.setOnClickListener { Log.w("TEST", "profileContactUsText clicked!") }
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager = requireContext()
            .getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(
            binding.searchField.windowToken,
            0
        )
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
            var location = address.addressDetails.street.ifEmpty {
                address.addressDetails.settlement.ifEmpty {
                    address.addressDetails.city.ifEmpty {
                        address.addressDetails.region.ifEmpty {
                            address.addressDetails.country
                        }
                    }
                }
            }

            if (address.addressDetails.house.isNotEmpty()) location += ", ${address.addressDetails.house}"

            binding.addressText.text = location
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            hideKeyboard()
        }
    }
}
