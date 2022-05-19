package com.catpedigree.capstone.catpedigreebase.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.catpedigree.capstone.catpedigreebase.data.item.PostItems
import com.catpedigree.capstone.catpedigreebase.databinding.ItemPostBinding

class PostAdapter : PagingDataAdapter<PostItems, PostAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(private var binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: PostItems) {
            val photo = "http://192.168.1.4/api-cat/public/storage/${post.photo}"
            val profilePhotoPath = "http://192.168.1.4/api-cat/public/storage/${post.profile_photo_path}"
            Glide.with(binding.root)
                .load(photo)
                .signature(ObjectKey(photo))
                .into(binding.ivPost)

            Glide.with(binding.root)
                .load(profilePhotoPath)
                .signature(ObjectKey(profilePhotoPath))
                .into(binding.ivProfile)

            binding.apply {
                val description = post.description
                tvPost.text = description?.substring(0,200)
                tvProfile.text = post.name
                tvCountLove.text = post.loves_count.toString()
                tvCountComment.text = post.comments_count.toString()
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding =
            ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = getItem(position)
        post?.let { holder.bind(it) }
    }

    companion object {
        val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<PostItems>() {
                override fun areItemsTheSame(oldItem: PostItems, newItem: PostItems): Boolean =
                    oldItem == newItem

                override fun areContentsTheSame(oldItem: PostItems, newItem: PostItems): Boolean =
                    oldItem.id == newItem.id
            }
    }
}