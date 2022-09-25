package ru.zatsoft.mynet.tab

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ru.zatsoft.mynet.activity.EventsFragment

import ru.zatsoft.mynet.activity.JobsFragment
import ru.zatsoft.mynet.activity.PostsFragment

class MyTabAdapter(fm:FragmentManager ) : FragmentPagerAdapter(fm )   {
     override fun getItem(position: Int): Fragment {
        return when (position) {
            1 -> EventsFragment()
            2 -> JobsFragment()
            else -> PostsFragment()
            }
    }

     override fun getCount(): Int {
        return 3
    }

     override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Posts"
            1 -> "Events"
            else -> {
                return "Jobs"
            }
        }
    }
}
