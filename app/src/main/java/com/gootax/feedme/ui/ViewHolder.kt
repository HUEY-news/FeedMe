package com.gootax.feedme.ui

import androidx.recyclerview.widget.RecyclerView
import com.gootax.feedme.databinding.ItemAddressBinding
import com.gootax.feedme.domain.model.Address

class ViewHolder(
    private val binding: ItemAddressBinding,
    onItemClick: (position: Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        itemView.setOnClickListener {
            onItemClick(adapterPosition)
        }
    }

    fun bind(address: Address) {

        binding.locationText.text =
            address.addressDetails.street.ifEmpty {
                address.addressDetails.settlement.ifEmpty {
                    address.addressDetails.city.ifEmpty {
                        address.addressDetails.region.ifEmpty {
                            address.addressDetails.country
                        }
                    }
                }
            }

        binding.locationDetailsText.text = address.shortAddress
    }
}
