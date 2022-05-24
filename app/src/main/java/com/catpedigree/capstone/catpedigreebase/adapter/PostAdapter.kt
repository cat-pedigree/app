package com.catpedigree.capstone.catpedigreebase.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.catpedigree.capstone.catpedigreebase.data.item.PostItems
import com.catpedigree.capstone.catpedigreebase.databinding.ItemPostBinding
import com.catpedigree.capstone.catpedigreebase.ui.home.HomeFragmentDirections

class PostAdapter : PagingDataAdapter<PostItems, PostAdapter.ViewHolder>(DIFF_CALLBACK) {
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

    inner class ViewHolder(private var binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: PostItems) {
            val photo = "http://192.168.1.3/api-cat/public/storage/${post.photo}"
            Glide.with(binding.root)
                .load(photo)
                .signature(ObjectKey(photo))
                .into(binding.imgPoster)

            binding.apply {
                tvItemName.text = post.name

                val description = post.description
                val length = description?.length

                if (length != null) {
                    if(length <= 100){
                        tvItemDescription.text = description
                    }else{
                        tvItemDescription.text = description.subSequence(1,100)
                    }
                }
                root.setOnClickListener {
                    Navigation.findNavController(root).navigate(
                        HomeFragmentDirections.actionHomeFragmentToPostDetail(
                            post
                        )
                    )
                }
            }
        }
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