package com.nammashale.shalepride.ui.facility

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nammashale.shalepride.data.model.Facility
import com.nammashale.shalepride.databinding.ItemFacilityPageBinding
import com.nammashale.shalepride.util.loadImage

class FacilityPagerAdapter(
    private val facilities: List<Facility>,
    private val onClick: (Facility) -> Unit = {},
    private val onLongClick: ((Facility) -> Unit)? = null
) : RecyclerView.Adapter<FacilityPagerAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemFacilityPageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFacilityPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val facility = facilities[position]
        holder.binding.apply {
            tvFacilityName.text = facility.name
            tvFacilityDesc.text = facility.description
            if (facility.imageUrl.isNotBlank()) {
                ivFacilityImage.loadImage(facility.imageUrl)
            } else {
                ivFacilityImage.setBackgroundColor(0xFF2E7D32.toInt())
            }
            root.setOnClickListener { onClick(facility) }
            root.setOnLongClickListener {
                onLongClick?.invoke(facility)
                true
            }
        }
    }

    override fun getItemCount() = facilities.size
}
