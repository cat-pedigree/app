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
import com.catpedigree.capstone.catpedigreebase.data.network.item.MessageItems
import com.catpedigree.capstone.catpedigreebase.data.network.item.RoomMessageItems
import com.catpedigree.capstone.catpedigreebase.databinding.ItemMessageBinding
import com.catpedigree.capstone.catpedigreebase.databinding.ItemMessageChatBinding
import com.catpedigree.capstone.catpedigreebase.presentation.ui.message.MessageFragmentDirections

class MessageAdapter : ListAdapter<MessageItems, MessageAdapter.ViewHolder>(DIFF_CALLBACK) {
    class ViewHolder(private var binding: ItemMessageChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: MessageItems) {
            val profilePath = "${BuildConfig.BASE_API_PHOTO}${message.profile_photo_path}"

            binding.apply {
                Glide.with(root)
                    .load(profilePath)
                    .placeholder(R.drawable.ic_avatar)
                    .signature(ObjectKey(profilePath))
                    .centerCrop()
                    .into(imgProfile)

                tvItemName.text = message.name
                tvItemMessage.text = message.messages
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding =
            ItemMessageChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = getItem(position)
        holder.bind(message)
    }

    companion object {
        val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<MessageItems>() {
                override fun areItemsTheSame(oldItem: MessageItems, newItem: MessageItems): Boolean =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(oldItem: MessageItems, newItem: MessageItems): Boolean =
                    oldItem == newItem
            }
    }
}