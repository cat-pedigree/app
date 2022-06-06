package com.catpedigree.capstone.catpedigreebase.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.catpedigree.capstone.catpedigreebase.BuildConfig
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.data.network.item.CatItems
import com.catpedigree.capstone.catpedigreebase.databinding.ItemMateBinding
import com.catpedigree.capstone.catpedigreebase.presentation.ui.pedigree.PedigreeFilterFragmentArgs
import com.catpedigree.capstone.catpedigreebase.presentation.ui.pedigree.PedigreeFilterFragmentDirections

class FilterAdapter : ListAdapter<CatItems, FilterAdapter.ViewHolder>(DIFF_CALLBACK) {
    class ViewHolder(private var binding: ItemMateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cat: CatItems) {
            val catPhotoPath = "${BuildConfig.BASE_API_PHOTO}${cat.photo}"

            binding.apply {
                Glide.with(root)
                    .load(catPhotoPath)
                    .placeholder(R.drawable.ic_avatar)
                    .signature(ObjectKey(catPhotoPath))
                    .into(ivCat)

                tvNameCat.text = cat.name.toString()
                tvBreedCat.text = cat.breed.toString()

                ivInformation.setOnClickListener {
                    Navigation.findNavController(ivInformation).navigate(
                        PedigreeFilterFragmentDirections.actionPedigreeFilterFragmentToInformationFragment(
                            cat
                        )
                    )
                }

//                root.setOnClickListener {
//                    Navigation.findNavController(root).navigate(
//                        PedigreeFragmentDirections.actionPedigreeFragmentToPedigreeFilterFragment(
//                            cat
//                        )
//                    )
//                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding =
            ItemMateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cat = getItem(position)
        holder.bind(cat)
    }

    companion object {
        val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<CatItems>() {
                override fun areItemsTheSame(oldItem: CatItems, newItem: CatItems): Boolean =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(oldItem: CatItems, newItem: CatItems): Boolean =
                    oldItem == newItem
            }
    }
}