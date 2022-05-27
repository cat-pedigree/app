package com.catpedigree.capstone.catpedigreebase.presentation.adapter

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
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
import com.catpedigree.capstone.catpedigreebase.presentation.ui.post.favorite.FavoriteFragmentDirections
import com.google.android.material.snackbar.Snackbar

class PostFavoriteAdapter(private val onFavoriteClick: (PostItems) -> Unit, private val onLoveClick:(PostItems) -> Unit) : ListAdapter<PostItems, PostFavoriteAdapter.ViewHolderFavorite>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolderFavorite {
        val binding =
            ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderFavorite(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderFavorite, position: Int) {
        holder.binding.root.context
        val post = getItem(position)
        holder.bind(post)

        val toggleFavorite = holder.binding.toggleFavorite
        val toggleLove = holder.binding.toggleLoves
        val imgPoster = holder.binding.imgPoster
        var tapTap = 0

        toggleFavorite.isChecked = post.isBookmarked
        toggleLove.isChecked = post.isLoved

        toggleFavorite.setOnClickListener {
            if(post.isBookmarked){
                onFavoriteClick(post)
                Snackbar.make(holder.binding.toggleFavorite,"Removed from favorites", Snackbar.LENGTH_LONG).show()
            }else{
                onFavoriteClick(post)
                Snackbar.make(holder.binding.toggleFavorite,"Added to favorites", Snackbar.LENGTH_LONG).show()
            }
        }

        toggleLove.setOnClickListener {
            if(post.isLoved){
                onLoveClick(post)
                Snackbar.make(holder.binding.toggleLoves,"Removed from liked", Snackbar.LENGTH_LONG).show()
            }else{
                onLoveClick(post)
                Snackbar.make(holder.binding.toggleLoves,"Liked this post", Snackbar.LENGTH_LONG).show()
            }
        }

        imgPoster.setOnClickListener {
            tapTap++
            Handler(Looper.getMainLooper()).postDelayed({
                if(tapTap==2){
                    if(post.isLoved){
                        onLoveClick(post)
                        toggleLove.isChecked = post.isLoved
                        Snackbar.make(holder.binding.toggleLoves,"Removed from liked", Snackbar.LENGTH_LONG).show()
                    }else{
                        onLoveClick(post)
                        toggleLove.isChecked = post.isLoved
                        Snackbar.make(holder.binding.toggleLoves,"Liked this post", Snackbar.LENGTH_LONG).show()
                    }
                }
                tapTap = 0
            }, tapTapTimeOut)
        }
    }

    class ViewHolderFavorite(val binding: ItemPostBinding) :
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
                        FavoriteFragmentDirections.actionFavoriteFragmentToCommentFragment(
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
        private const val tapTapTimeOut = 500L
    }
}