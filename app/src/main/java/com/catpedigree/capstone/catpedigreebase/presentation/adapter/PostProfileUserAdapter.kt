package com.catpedigree.capstone.catpedigreebase.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.catpedigree.capstone.catpedigreebase.BuildConfig
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.data.network.item.PostItems
import com.catpedigree.capstone.catpedigreebase.databinding.ItemPostProfileBinding
import com.catpedigree.capstone.catpedigreebase.presentation.ui.profile.detail.PostDetailProfileFragmentDirections
import com.catpedigree.capstone.catpedigreebase.presentation.ui.profile.user.ProfileFragmentDirections

class PostProfileUserAdapter : ListAdapter<PostItems, PostProfileUserAdapter.ViewHolderProfile>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolderProfile {
        val binding =
            ItemPostProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderProfile(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderProfile, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

    class ViewHolderProfile(val binding: ItemPostProfileBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: PostItems) {
            val photo = "${BuildConfig.BASE_API_PHOTO}${post.photo}"
            binding.apply {
                Glide.with(root)
                    .load(photo)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_loading).error(R.drawable.ic_error))
                    .signature(ObjectKey(photo))
                    .into(imgPoster)



//                imgPoster.setOnClickListener {
//                    Navigation.findNavController(imgPoster).navigate(R.id.action_myProfileFragment_to_postDetailProfileFragment)
//                }

                imgPoster.setOnClickListener {
                    Navigation.findNavController(imgPoster).navigate(
                        ProfileFragmentDirections.actionProfileFragmentToPostDetailUserFragment(
                            post
                        )
                    )
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK : DiffUtil.ItemCallback<PostItems> =
            object : DiffUtil.ItemCallback<PostItems>() {
                override fun areItemsTheSame(oldItem: PostItems, newItem: PostItems): Boolean =
                    oldItem.title == newItem.title

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldItem: PostItems, newItem: PostItems): Boolean =
                    oldItem == newItem
            }
    }
}