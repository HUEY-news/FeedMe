package com.gootax.feedme.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gootax.feedme.databinding.ItemAddressBinding
import com.gootax.feedme.domain.model.Address

class Adapter(
    private val onItemClick: (address: Address) -> Unit
) : RecyclerView.Adapter<ViewHolder>(){
    private var itemList: List<Address> = emptyList()

    fun submitList(list: List<Address>) {
        val diffCallback = DiffCallback(itemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemList = list
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return ViewHolder(ItemAddressBinding.inflate(layoutInspector, parent, false))
        { position: Int ->
            if (position != RecyclerView.NO_POSITION) {
                itemList.getOrNull(position)?.let { address: Address ->
                    onItemClick(address)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        itemList.getOrNull(position)?.let { address: Address -> holder.bind(address) }
    }

    override fun getItemCount(): Int = itemList.size
}
