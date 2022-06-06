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
import com.catpedigree.capstone.catpedigreebase.data.network.item.MessageRoomItems
import com.catpedigree.capstone.catpedigreebase.databinding.ItemMessageBinding

class MessageRoomAdapter : ListAdapter<MessageRoomItems, MessageRoomAdapter.ViewHolder>(DIFF_CALLBACK) {
    class ViewHolder(private var binding: ItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: MessageRoomItems) {
            val profilePath = "${BuildConfig.BASE_API_PHOTO}${message.profile_photo_path}"

            binding.apply {
                Glide.with(root)
                    .load(profilePath)
                    .placeholder(R.drawable.ic_avatar)
                    .signature(ObjectKey(profilePath))
                    .centerCrop()
                    .into(ivAvatar)

                tvName.text = message.name
                tvMessage.text = message.messages
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding =
            ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = getItem(position)
        holder.bind(message)
    }

    companion object {
        val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<MessageRoomItems>() {
                override fun areItemsTheSame(oldItem: MessageRoomItems, newItem: MessageRoomItems): Boolean =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(oldItem: MessageRoomItems, newItem: MessageRoomItems): Boolean =
                    oldItem == newItem
            }
    }
}