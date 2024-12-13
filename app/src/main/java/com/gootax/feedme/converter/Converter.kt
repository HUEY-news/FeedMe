package com.gootax.feedme.converter

import com.gootax.feedme.data.model.AddressDTO
import com.gootax.feedme.data.model.AddressDetailsDTO
import com.gootax.feedme.domain.model.Address
import com.gootax.feedme.domain.model.AddressDetails

class Converter {

    fun map(suggestions: List<AddressDTO>): List<Address> {
        return suggestions.map { address -> map(address) }
    }

    private fun map(address: AddressDTO): Address {
        return Address(
            shortAddress = address.shortAddress,
            fullAddress = address.fullAddress,
            addressDetails = map(address.addressDetails)
        )
    }

    private fun map(details: AddressDetailsDTO): AddressDetails {
        return AddressDetails(
            id = details.id ?: "null",
            house = details.house ?: "",
            street = details.street ?: "",
            settlement = details.settlement ?: "",
            city = details.city ?: "",
            region = details.region ?: "",
            country = details.country ?: ""
        )
    }

}
