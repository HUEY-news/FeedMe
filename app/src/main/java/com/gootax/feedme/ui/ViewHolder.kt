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

        binding.locationText.text = location
        binding.locationDetailsText.text = address.shortAddress
    }
}
