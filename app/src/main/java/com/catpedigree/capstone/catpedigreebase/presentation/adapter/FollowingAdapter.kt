package com.catpedigree.capstone.catpedigreebase.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.catpedigree.capstone.catpedigreebase.BuildConfig
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.data.network.item.FollowingItems
import com.catpedigree.capstone.catpedigreebase.databinding.ItemUserBinding

class FollowingAdapter : ListAdapter<FollowingItems, FollowingAdapter.ViewHolder>(DIFF_CALLBACK) {
    class ViewHolder(private var binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(following: FollowingItems) {
            val profilePhotoPath = "${BuildConfig.BASE_API_PHOTO}${following.profile_photo_path}"

            binding.apply {
                Glide.with(root)
                    .load(profilePhotoPath)
                    .placeholder(R.drawable.ic_avatar)
                    .signature(ObjectKey(profilePhotoPath))
                    .circleCrop()
                    .into(ivAvatar)

                tvUsername.text = following.username
                tvName.text = following.name
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
        val following = getItem(position)
        holder.bind(following)
    }

    companion object {
        val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<FollowingItems>() {
                override fun areItemsTheSame(
                    oldItem: FollowingItems,
                    newItem: FollowingItems
                ): Boolean =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: FollowingItems,
                    newItem: FollowingItems
                ): Boolean =
                    oldItem == newItem
            }
    }
}