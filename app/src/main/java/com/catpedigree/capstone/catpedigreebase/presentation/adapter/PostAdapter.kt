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
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.data.network.item.PostItems
import com.catpedigree.capstone.catpedigreebase.databinding.ItemPostBinding
import com.catpedigree.capstone.catpedigreebase.presentation.ui.home.HomeFragmentDirections
import com.catpedigree.capstone.catpedigreebase.utils.ToastUtils

class PostAdapter(private val onFavoriteClick: (PostItems) -> Unit) : ListAdapter<PostItems, PostAdapter.ViewHolder>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding =
            ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.binding.root.context
        val post = getItem(position)
        holder.bind(post)

        val toggleFavorite = holder.binding.toggleFavorite

        toggleFavorite.isChecked = post.isBookmarked

            toggleFavorite.setOnClickListener {
                if(post.isBookmarked){
                    onFavoriteClick(post)
                    ToastUtils.showToast(context,"Removed from favorites")
                }else{
                    onFavoriteClick(post)
                    ToastUtils.showToast(context,"Added to favorites")
                }

            }
    }

    class ViewHolder(val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: PostItems) {
            val photo = "http://192.168.1.4/api-cat/public/storage/${post.photo}"
            val profilePhotoPath = "http://192.168.1.4/api-cat/public/storage/${post.profile_photo_path}"
            Glide.with(binding.root)
                .load(photo)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_loading).error(R.drawable.ic_error))
                .signature(ObjectKey(photo))
                .into(binding.imgPoster)

            Glide.with(binding.root)
                .load(profilePhotoPath)
                .signature(ObjectKey(profilePhotoPath))
                .placeholder(R.drawable.ic_avatar)
                .circleCrop()
                .into(binding.imgProfile)

            binding.apply {
                tvItemName.text = post.name
                tvItemTitle.text = post.title



                tvItemDescription.text = post.description
                ivComment.setOnClickListener {
                    Navigation.findNavController(ivComment).navigate(
                        HomeFragmentDirections.actionHomeFragmentToCommentFragment(
                            post
                        )
                    )
                }

//                root.setOnClickListener {
//                    Navigation.findNavController(root).navigate(
//                        HomeFragmentDirections.actionHomeFragmentToPostDetail(
//                            post
//                        )
//                    )
//                }
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