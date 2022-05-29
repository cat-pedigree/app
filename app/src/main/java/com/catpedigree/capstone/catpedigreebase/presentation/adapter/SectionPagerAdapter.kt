package com.catpedigree.capstone.catpedigreebase.presentation.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.catpedigree.capstone.catpedigreebase.presentation.ui.profile.my_profile.view.FavoriteMyProfileFragment
import com.catpedigree.capstone.catpedigreebase.presentation.ui.profile.my_profile.view.PostMyProfileFragment

class SectionPagerAdapter internal constructor(activity: AppCompatActivity, bundle: Bundle): FragmentStateAdapter(activity){

    private var fragmentBundle: Bundle = bundle

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position){
            0 -> fragment = PostMyProfileFragment()
            1 -> fragment = FavoriteMyProfileFragment()
        }
        fragment?.arguments = this.fragmentBundle
        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return 2
    }
}