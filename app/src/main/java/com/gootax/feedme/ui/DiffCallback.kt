package com.gootax.feedme.ui

import androidx.recyclerview.widget.DiffUtil
import com.gootax.feedme.domain.model.Address

class DiffCallback(
    private val oldList: List<Address>,
    private val newList: List<Address>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].addressDetails.id == newList[newItemPosition].addressDetails.id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]

}
