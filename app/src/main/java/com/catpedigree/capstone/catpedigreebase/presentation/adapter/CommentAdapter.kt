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
import com.catpedigree.capstone.catpedigreebase.data.network.item.CommentItems
import com.catpedigree.capstone.catpedigreebase.databinding.ItemCommentBinding

class CommentAdapter : ListAdapter<CommentItems, CommentAdapter.ViewHolder>(DIFF_CALLBACK) {
    class ViewHolder(private var binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: CommentItems) {
            val profilePhotoPath = "${BuildConfig.BASE_API_PHOTO}${comment.profile_photo_path}"

            binding.apply {
                Glide.with(root)
                    .load(profilePhotoPath)
                    .placeholder(R.drawable.ic_avatar)
                    .signature(ObjectKey(profilePhotoPath))
                    .circleCrop()
                    .into(ivAvatarComment)

                    tvNameComment.text = comment.name.toString()
                    tvComment.text = comment.description
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding =
            ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = getItem(position)
        holder.bind(comment)
    }

    companion object {
        val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<CommentItems>() {
                override fun areItemsTheSame(oldItem: CommentItems, newItem: CommentItems): Boolean =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(oldItem: CommentItems, newItem: CommentItems): Boolean =
                    oldItem == newItem
            }
    }
}