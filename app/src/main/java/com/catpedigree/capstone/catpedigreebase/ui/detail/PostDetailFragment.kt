package com.catpedigree.capstone.catpedigreebase.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.data.item.UserItems
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentPostDetailBinding
import com.catpedigree.capstone.catpedigreebase.factory.ViewModelFactory
import com.catpedigree.capstone.catpedigreebase.utils.ToastUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PostDetailFragment : Fragment() {

    private lateinit var _binding: FragmentPostDetailBinding
    private val binding get() = _binding

    private val args:PostDetailFragmentArgs by navArgs()
    private lateinit var user: UserItems

    private val viewModel: PostDetailViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
        setupViewModel()
    }

    private fun setupAction(){
        val post = args.post
        val profilePhotoPath = "http://192.168.1.3/api-cat/public/storage/${post.profile_photo_path}"
        val photo = "http://192.168.1.3/api-cat/public/storage/${post.photo}"

        Glide.with(binding.root)
            .load(photo)
            .signature(ObjectKey(photo))
            .into(binding.ivPost)

            Glide.with(binding.root)
                .load(profilePhotoPath)
                .signature(ObjectKey(profilePhotoPath))
                .placeholder(R.drawable.ic_baseline_person_pin_24)
                .into(binding.ivProfile)

        binding.apply {
            tvPost.text = post.description
            tvProfile.text = post.id.toString()
            tvCountLove.text = post.loves_count.toString()
            tvCountComment.text = post.comments_count.toString()

            ivComment.setOnClickListener {
                Navigation.findNavController(ivComment).navigate(
                    PostDetailFragmentDirections.actionPostDetailFragmentToCommentFragment(
                        post
                    )
                )
            }
        }

        post.id?.let { addFavorite(it) }

        post.id?.let { addLove(it) }
    }

    private fun setupViewModel(){
        viewModel.userItems.observe(viewLifecycleOwner) { userItems ->
            if (userItems?.isLoggedIn == false) {
                findNavController().navigateUp()
            }
            this.user = userItems
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { state ->
            showLoading(state)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            ToastUtils.showToast(requireContext(), message)
        }
    }

    private fun addFavorite(post_id: Int){
        val post = args.post
        var isCheck = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkPost(post_id)
            withContext(Dispatchers.Main){
                if(count != null){
                    if(count > 0){
                        binding.toggleFavorite.isChecked = true
                        isCheck = true
                    }else{
                        binding.toggleFavorite.isChecked = false
                        isCheck = false
                    }
                }
            }
        }

        binding.toggleFavorite.setOnClickListener{
            isCheck = !isCheck
            if(isCheck){
                viewModel.addToFavoritePost(
                    post.id,
                    post.name,
                    post.profile_photo_path,
                    post.photo,
                    post.description,
                    post.loves_count,
                    post.comments_count
                )
                ToastUtils.showToast(requireContext(),"Successfully add post to favorite")
            }else{
                post.id?.let { data -> viewModel.removeFromFavorite(data) }
                ToastUtils.showToast(requireContext(),"Successfully remove post from favorite")
            }
            binding.toggleFavorite.isChecked = isCheck
        }
    }

    private fun addLove(post_id:Int){
        val post = args.post
        var isCheck = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkLove(post_id)
            withContext(Dispatchers.Main){
                if(count != null){
                    if(count > 0){
                        binding.toggleLove.isChecked = true
                        isCheck = true
                    }else{
                        binding.toggleLove.isChecked = false
                        isCheck = false
                    }
                }
            }
        }

        binding.toggleLove.setOnClickListener{
            isCheck = !isCheck
            if(isCheck){
                viewModel.addToLovePost(
                    post.id,
                    post.id,
                    user.id
                )
                user.token?.let { it1 -> post.id?.let { it2 ->
                    user.id?.let { it3 ->
                        viewModel.loveCreate(it1,
                            it2, it3
                        )
                    }
                }}
                ToastUtils.showToast(requireContext(),"Successfully add like to post")
            }else{
                user.id?.let { it1 -> post.id?.let { it2 -> viewModel.removeFromLove(it2, it1) } }
                user.token?.let { it1 -> post.id?.let { it2 ->
                    user.id?.let { it3 ->
                        viewModel.loveDelete(it1,
                            it2, it3
                        )
                    }
                } }
                ToastUtils.showToast(requireContext(),"Successfully remove post from favorite")
            }
            binding.toggleLove.isChecked = isCheck
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }
}