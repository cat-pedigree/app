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
import com.catpedigree.capstone.catpedigreebase.data.network.item.FollowItems
import com.catpedigree.capstone.catpedigreebase.databinding.ItemUserBinding

class FollowerAdapter : ListAdapter<FollowItems, FollowerAdapter.ViewHolder>(DIFF_CALLBACK) {
    class ViewHolder(private var binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(follower: FollowItems) {
            val profilePhotoPath = "${BuildConfig.BASE_API_PHOTO}${follower.profile_photo_path}"

            binding.apply {
                Glide.with(root)
                    .load(profilePhotoPath)
                    .placeholder(R.drawable.ic_avatar)
                    .signature(ObjectKey(profilePhotoPath))
                    .circleCrop()
                    .into(ivAvatar)

                tvUsername.text = follower.username
                tvName.text = follower.name
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
        val follower = getItem(position)
        holder.bind(follower)
    }

    companion object {
        val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<FollowItems>() {
                override fun areItemsTheSame(
                    oldItem: FollowItems,
                    newItem: FollowItems
                ): Boolean =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: FollowItems,
                    newItem: FollowItems
                ): Boolean =
                    oldItem == newItem
            }
    }
}