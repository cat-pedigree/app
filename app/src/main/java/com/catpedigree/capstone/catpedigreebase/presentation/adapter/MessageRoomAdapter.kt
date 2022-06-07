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
import com.catpedigree.capstone.catpedigreebase.data.network.item.RoomMessageItems
import com.catpedigree.capstone.catpedigreebase.databinding.ItemMessageBinding
import com.catpedigree.capstone.catpedigreebase.presentation.ui.message.MessageFragmentDirections

class MessageRoomAdapter : ListAdapter<RoomMessageItems, MessageRoomAdapter.ViewHolder>(DIFF_CALLBACK) {
    class ViewHolder(private var binding: ItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(room: RoomMessageItems) {
            val profilePath = "${BuildConfig.BASE_API_PHOTO}${room.profile_photo_path}"

            binding.apply {
                Glide.with(root)
                    .load(profilePath)
                    .placeholder(R.drawable.ic_avatar)
                    .signature(ObjectKey(profilePath))
                    .centerCrop()
                    .into(ivAvatar)

                tvName.text = room.name
                tvMessage.text = room.messages

                root.setOnClickListener {
                    Navigation.findNavController(root).navigate(
                        MessageFragmentDirections.actionMessageFragmentToMessageChatFragment(
                            room
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
            ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = getItem(position)
        holder.bind(message)
    }

    companion object {
        val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<RoomMessageItems>() {
                override fun areItemsTheSame(oldItem: RoomMessageItems, newItem: RoomMessageItems): Boolean =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(oldItem: RoomMessageItems, newItem: RoomMessageItems): Boolean =
                    oldItem == newItem
            }
    }
}