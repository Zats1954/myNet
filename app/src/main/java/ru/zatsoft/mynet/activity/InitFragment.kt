package ru.zatsoft.mynet.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import ru.zatsoft.mynet.adapter.TabAdapter
import ru.zatsoft.mynet.databinding.FragmentInitBinding
import ru.zatsoft.mynet.dto.Token
import ru.zatsoft.mynet.tab.MyTabAdapter

class InitFragment: Fragment() {
//    private val viewModel: PostViewModel by viewModels(
////        ownerProducer = ::requireParentFragment
//    )
    private var myToken: Token? = null
    private lateinit var adapter: TabAdapter
    private val tabNames: Array<String> = arrayOf("posts","events","jobs")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentInitBinding.inflate(
            inflater,
            container,
            false)
        val viewPager: ViewPager = binding.viewPager
        val  tabAdapter =  MyTabAdapter(getChildFragmentManager())
         viewPager.adapter =  tabAdapter
        val tabs : TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)
        return binding.root
    }

}