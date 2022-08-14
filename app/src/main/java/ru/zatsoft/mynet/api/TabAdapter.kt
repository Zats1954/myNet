package ru.zatsoft.mynet.api

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ru.zatsoft.mynet.activity.EventsFragment
import ru.zatsoft.mynet.activity.JobsFragment
import ru.zatsoft.mynet.activity.PostsFragment

class TabAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            1 -> EventsFragment()
            2 -> JobsFragment()
            else -> PostsFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            1 -> "events"
            2 -> "jobs"
            else -> "posts"
        }
    }
}
