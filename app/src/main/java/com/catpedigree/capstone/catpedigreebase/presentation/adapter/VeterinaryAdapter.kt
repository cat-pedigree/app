package com.catpedigree.capstone.catpedigreebase.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.catpedigree.capstone.catpedigreebase.data.network.item.VeterinaryItems
import com.catpedigree.capstone.catpedigreebase.databinding.ItemVeterinaryBinding

class VeterinaryAdapter(private val context: Context) : ListAdapter<VeterinaryItems, VeterinaryAdapter.ViewHolder>(DIFF_CALLBACK) {
    class ViewHolder(val binding: ItemVeterinaryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(veterinary: VeterinaryItems) {


            binding.apply {
                nameClinic.text = veterinary.name
                locationClinic.text = "${veterinary.city}, ${veterinary.country}"
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding =
            ItemVeterinaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.root.context
        val veterinary = getItem(position)
        holder.bind(veterinary)

//        val gmmIntentUri = Uri.parse("geo:${veterinary.lat},${veterinary.lon}")
        val gmmIntentUri = Uri.parse("google.navigation:q=${veterinary.lat},${veterinary.lon}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")

        holder.binding.root.setOnClickListener {
            mapIntent.resolveActivity(context.packageManager)?.let {
                context.startActivity(mapIntent)
            }
        }

    }

    companion object {
        val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<VeterinaryItems>() {
                override fun areItemsTheSame(oldItem: VeterinaryItems, newItem: VeterinaryItems): Boolean =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(oldItem: VeterinaryItems, newItem: VeterinaryItems): Boolean =
                    oldItem == newItem
            }
    }
}