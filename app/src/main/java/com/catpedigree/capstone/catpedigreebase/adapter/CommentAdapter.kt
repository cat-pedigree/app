package com.catpedigree.capstone.catpedigreebase.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.catpedigree.capstone.catpedigreebase.data.item.CommentItems
import com.catpedigree.capstone.catpedigreebase.databinding.ItemCommentBinding

class CommentAdapter : PagingDataAdapter<CommentItems, CommentAdapter.ViewHolder>(DIFF_CALLBACK) {
    inner class ViewHolder(private var binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: CommentItems) {
//            val profilePhotoPath = "http://192.168.1.3/api-cat/public/storage/${comment.profile_photo_path}"

//            Glide.with(binding.root)
//                .load(profilePhotoPath)
//                .signature(ObjectKey(profilePhotoPath))
//                .placeholder(R.drawable.ic_baseline_person_pin_24)
//                .into(binding.ivAvatarComment)

            binding.apply {
                tvNameComment.text = comment.id.toString()
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
        comment?.let { holder.bind(it) }
    }

    companion object {
        val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<CommentItems>() {
                override fun areItemsTheSame(oldItem: CommentItems, newItem: CommentItems): Boolean =
                    oldItem == newItem

                override fun areContentsTheSame(oldItem: CommentItems, newItem: CommentItems): Boolean =
                    oldItem.id == newItem.id
            }
    }
}