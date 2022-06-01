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
import com.catpedigree.capstone.catpedigreebase.data.network.item.UserDataItems
import com.catpedigree.capstone.catpedigreebase.databinding.ItemUserBinding
import com.catpedigree.capstone.catpedigreebase.presentation.ui.home.HomeFragmentDirections

class SearchAdapter : ListAdapter<UserDataItems, SearchAdapter.ViewHolder>(DIFF_CALLBACK) {
    class ViewHolder(private var binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserDataItems) {
            val profilePhotoPath = "${BuildConfig.BASE_API_PHOTO}${user.profile_photo_path}"

            binding.apply {
                Glide.with(root)
                    .load(profilePhotoPath)
                    .placeholder(R.drawable.ic_avatar)
                    .signature(ObjectKey(profilePhotoPath))
                    .circleCrop()
                    .into(ivAvatar)

                tvUsername.text = user.username
                tvName.text = user.name

                root.setOnClickListener {
                    Navigation.findNavController(root).navigate(
                        HomeFragmentDirections.actionHomeFragmentToProfileFragment(
                            user
                        )
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding =
            ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = getItem(position)
        holder.bind(comment)
    }

    companion object {
        val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<UserDataItems>() {
                override fun areItemsTheSame(
                    oldItem: UserDataItems,
                    newItem: UserDataItems
                ): Boolean =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: UserDataItems,
                    newItem: UserDataItems
                ): Boolean =
                    oldItem == newItem
            }
    }
}