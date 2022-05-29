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
import com.google.android.material.snackbar.Snackbar
import android.os.Handler
import android.os.Looper
import com.catpedigree.capstone.catpedigreebase.BuildConfig

class PostAdapter(private val onFavoriteClick: (PostItems) -> Unit, private val onLoveClick:(PostItems) -> Unit) : ListAdapter<PostItems, PostAdapter.ViewHolder>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding =
            ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.root.context
        val post = getItem(position)
        holder.bind(post)

        holder.binding.apply {
            val toggleFavorite = toggleFavorite
            val toggleLove = toggleLoves
            val imgPoster = imgPoster
            var tapTap = 0

            toggleFavorite.isChecked = post.isBookmarked
            toggleLove.isChecked = post.isLoved

            toggleFavorite.setOnClickListener {
                if(post.isBookmarked){
                    onFavoriteClick(post)
                    Snackbar.make(toggleFavorite,R.string.remove_favorite,Snackbar.LENGTH_LONG).show()
                }else{
                    onFavoriteClick(post)
                    Snackbar.make(toggleFavorite,R.string.add_favorite,Snackbar.LENGTH_LONG).show()
                }
            }

            toggleLove.setOnClickListener {
                if(post.isLoved){
                    onLoveClick(post)
                    Snackbar.make(toggleLoves,R.string.remove_like,Snackbar.LENGTH_LONG).show()
                }else{
                    onLoveClick(post)
                    Snackbar.make(toggleLoves,R.string.like_post,Snackbar.LENGTH_LONG).show()
                }
            }

            imgPoster.setOnClickListener {
                tapTap++
                Handler(Looper.getMainLooper()).postDelayed({
                    if(tapTap==2){
                        if(post.isLoved){
                            onLoveClick(post)
                            toggleLove.isChecked = post.isLoved
                            Snackbar.make(toggleLoves,R.string.remove_like,Snackbar.LENGTH_LONG).show()
                        }else{
                            onLoveClick(post)
                            toggleLove.isChecked = post.isLoved
                            Snackbar.make(toggleLoves,R.string.like_post,Snackbar.LENGTH_LONG).show()
                        }
                    }
                    tapTap = 0
                }, tapTapTimeOut)
            }
        }
    }

    class ViewHolder(val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: PostItems) {
            val photo = "${BuildConfig.BASE_API_URL_PHOTO}${post.photo}"
            val profilePhotoPath = "${BuildConfig.BASE_API_URL_PHOTO}${post.profile_photo_path}"

        binding.apply {
            Glide.with(root)
                .load(photo)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_loading).error(R.drawable.ic_error))
                .signature(ObjectKey(photo))
                .into(imgPoster)

            Glide.with(root)
                .load(profilePhotoPath)
                .signature(ObjectKey(profilePhotoPath))
                .placeholder(R.drawable.ic_avatar)
                .circleCrop()
                .into(imgProfile)

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