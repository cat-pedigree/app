package com.catpedigree.capstone.catpedigreebase.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.catpedigree.capstone.catpedigreebase.BuildConfig
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.data.network.item.AlbumItems
import com.catpedigree.capstone.catpedigreebase.databinding.ItemAlbumBinding

class AlbumAdapter : ListAdapter<AlbumItems, AlbumAdapter.ViewHolderAlbum>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolderAlbum {
        val binding =
            ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderAlbum(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderAlbum, position: Int) {
        val album = getItem(position)
        holder.bind(album)
    }

    class ViewHolderAlbum(val binding: ItemAlbumBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(album: AlbumItems) {
            val photo = "${BuildConfig.BASE_API_PHOTO}${album.photo}"
            binding.apply {
                Glide.with(root)
                    .load(photo)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_error)
                    )
                    .signature(ObjectKey(photo))
                    .into(imgAlbum)

//                imgPoster.setOnClickListener {
//                    Navigation.findNavController(imgPoster).navigate(R.id.action_myProfileFragment_to_postDetailProfileFragment)
//                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<AlbumItems> =
            object : DiffUtil.ItemCallback<AlbumItems>() {
                override fun areItemsTheSame(oldItem: AlbumItems, newItem: AlbumItems): Boolean =
                    oldItem.id == newItem.id

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldItem: AlbumItems, newItem: AlbumItems): Boolean =
                    oldItem == newItem
            }
    }
}