package com.catpedigree.capstone.catpedigreebase.presentation.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.catpedigree.capstone.catpedigreebase.data.local.repository.*
import com.catpedigree.capstone.catpedigreebase.presentation.ui.about.AboutViewModel
import com.catpedigree.capstone.catpedigreebase.presentation.ui.account.AccountViewModel
import com.catpedigree.capstone.catpedigreebase.presentation.ui.auth.login.LoginViewModel
import com.catpedigree.capstone.catpedigreebase.presentation.ui.auth.register.RegisterViewModel
import com.catpedigree.capstone.catpedigreebase.presentation.ui.cat.add.AddCatViewModel
import com.catpedigree.capstone.catpedigreebase.presentation.ui.cat.view.CatProfileViewModel
import com.catpedigree.capstone.catpedigreebase.presentation.ui.classification.ResultViewModel
import com.catpedigree.capstone.catpedigreebase.presentation.ui.dating.DatingViewModel
import com.catpedigree.capstone.catpedigreebase.presentation.ui.home.HomeViewModel
import com.catpedigree.capstone.catpedigreebase.presentation.ui.maps.MapsViewModel
import com.catpedigree.capstone.catpedigreebase.presentation.ui.onboarding.OnBoardingViewModel
import com.catpedigree.capstone.catpedigreebase.presentation.ui.pedigree.PedigreeViewModel
import com.catpedigree.capstone.catpedigreebase.presentation.ui.post.comment.create.CreateCommentViewModel
import com.catpedigree.capstone.catpedigreebase.presentation.ui.post.comment.view.CommentViewModel
import com.catpedigree.capstone.catpedigreebase.presentation.ui.post.create.CreatePostViewModel
import com.catpedigree.capstone.catpedigreebase.presentation.ui.profile.detail.PostDetailProfileViewModel
import com.catpedigree.capstone.catpedigreebase.presentation.ui.profile.my_profile.edit.EditProfileViewModel
import com.catpedigree.capstone.catpedigreebase.presentation.ui.profile.my_profile.favorite.FavoriteViewModel
import com.catpedigree.capstone.catpedigreebase.presentation.ui.profile.my_profile.view.MyProfileViewModel
import com.catpedigree.capstone.catpedigreebase.presentation.ui.profile.user.ProfileViewModel
import com.catpedigree.capstone.catpedigreebase.presentation.ui.services.ServicesViewModel
import com.catpedigree.capstone.catpedigreebase.presentation.ui.veterinary.VeterinaryViewModel
import com.catpedigree.capstone.catpedigreebase.utils.injection.Injection

class ViewModelFactory
    (
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
    private val catRepository: CatRepository,
    private val veterinaryRepository: VeterinaryRepository,
    private val followRepository: FollowRepository,
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(OnBoardingViewModel::class.java) -> {
                OnBoardingViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(userRepository, catRepository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(userRepository, postRepository) as T
            }
            modelClass.isAssignableFrom(CommentViewModel::class.java) -> {
                CommentViewModel(userRepository, commentRepository) as T
            }
            modelClass.isAssignableFrom(CreateCommentViewModel::class.java) -> {
                CreateCommentViewModel(userRepository, commentRepository) as T
            }
            modelClass.isAssignableFrom(CreatePostViewModel::class.java) -> {
                CreatePostViewModel(userRepository, postRepository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(userRepository, catRepository, postRepository, followRepository) as T
            }
            modelClass.isAssignableFrom(MyProfileViewModel::class.java) -> {
                MyProfileViewModel(userRepository, postRepository, catRepository,followRepository) as T
            }
            modelClass.isAssignableFrom(PostDetailProfileViewModel::class.java) -> {
                PostDetailProfileViewModel(userRepository, postRepository) as T
            }
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                FavoriteViewModel(userRepository, postRepository) as T
            }
            modelClass.isAssignableFrom(EditProfileViewModel::class.java) -> {
                EditProfileViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(AddCatViewModel::class.java) -> {
                AddCatViewModel(userRepository, catRepository) as T
            }
            modelClass.isAssignableFrom(CatProfileViewModel::class.java) -> {
                CatProfileViewModel(userRepository, catRepository) as T
            }
            modelClass.isAssignableFrom(AccountViewModel::class.java) -> {
                AccountViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(VeterinaryViewModel::class.java) -> {
                VeterinaryViewModel(userRepository, veterinaryRepository) as T
            }
            modelClass.isAssignableFrom(ServicesViewModel::class.java) -> {
                ServicesViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(ResultViewModel::class.java) -> {
                ResultViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(PedigreeViewModel::class.java) -> {
                PedigreeViewModel(userRepository,catRepository) as T
            }
            modelClass.isAssignableFrom(DatingViewModel::class.java) -> {
                DatingViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(AboutViewModel::class.java) -> {
                AboutViewModel(userRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideUserRepository(context),
                    Injection.providePostRepository(context),
                    Injection.provideCommentRepository(context),
                    Injection.provideCatRepository(context),
                    Injection.provideVeterinaryRepository(context),
                    Injection.provideFollowRepository(context),
                )
            }.also { instance = it }
    }
}