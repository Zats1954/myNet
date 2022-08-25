package ru.zatsoft.mynet.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import ru.zatsoft.mynet.R
import ru.zatsoft.mynet.adapter.OnInteractionListener
import ru.zatsoft.mynet.adapter.PostsAdapter
import ru.zatsoft.mynet.auth.AppAuth
import ru.zatsoft.mynet.databinding.FragmentInitBinding
import ru.zatsoft.mynet.dto.Post
import ru.zatsoft.mynet.dto.Token
import ru.zatsoft.mynet.model.FeedState

import ru.zatsoft.mynet.viewmodel.PostViewModel

class InitFragment: Fragment() {
    private val viewModel: PostViewModel by viewModels(
//        ownerProducer = ::requireParentFragment
    )
    private var myToken: Token? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentInitBinding.inflate(
            inflater,
            container,
            false)
//        binding.btnSign.setOnClickListener {
//            val token = bundleOf("token" to myToken)
//             findNavController(this)
//                .navigate(R.id.action_placeholder_to_signInFragment , token)
//            myToken?.let{ AppAuth.getInstance().setAuth(it.id, it.token)}
//        }
//         Set the toolbar as support action bar
//
//        binding.setSupportActionBar(binding.toolbar)
//
//        supportActionBar?.apply {
//            // Set toolbar title/app title
//            title = "Toolbar Title"
//
//            // Set action bar/toolbar sub title
//            subtitle = "Toolbar sub title"
//
//            // Display the app icon in action bar/toolbar
//            setDisplayShowHomeEnabled(true)
//            setLogo(R.mipmap.ic_launcher)
//            setDisplayUseLogoEnabled(true)
//        }
//        -------------------------------------------------------------------------------------
        val adapter = PostsAdapter(object : OnInteractionListener {

            override fun onEdit(post: Post) {
                viewModel.edit(post)
                val bundle = Bundle()
                bundle.putParcelable("post",post)
                findNavController().navigate(R.id.action_placeholder_to_newPostFragment, bundle)
            }

            override fun onLike(post: Post) {
                viewModel.likeById(post.id)

            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onShare(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }

                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)
            }
//
//
//            override fun onShowImage(post:Post){
//                val bundle = Bundle()
//                bundle.putString("postImage", post.attachment?.url)
//                findNavController().navigate(R.id.action_feedFragment_to_showImageFragment, bundle)
//            }
        })


        binding.list.adapter = adapter

        viewModel.posts.asLiveData().observe(viewLifecycleOwner, {
            adapter.submitList(it.posts)
            binding.emptyText.isVisible = it.empty
        })
        viewModel.data.observe(viewLifecycleOwner){state ->
            when(state){
                FeedState.Loading -> {
                    binding.progress.isVisible = true
                    binding.errorGroup.isVisible = false
                    binding.list.isVisible = false
                }
                FeedState.Error -> {
                    binding.progress.isVisible = false
                    binding.errorGroup.isVisible = true
                    binding.errorMessage.text = viewModel.errorMessage
                    binding.list.isVisible = false
                }
                FeedState.Refreshing, FeedState.Success ->{
                    binding.progress.isVisible = false
                    binding.errorGroup.isVisible = false
                    binding.list.isVisible = true
                    binding.newsButton.isVisible = false
                }
            }
        }

        binding.retryButton.setOnClickListener {
            viewModel.loadPosts()

        }

        binding.fab.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("post",viewModel.empty)
            findNavController().navigate(R.id.action_placeholder_to_newPostFragment, bundle)
        }

        binding.newsButton.setOnClickListener {
            viewModel.showNews()
            binding.newsButton.isVisible = false
            viewModel.clearCountNews()
            viewModel.loadPosts()
        }

        viewModel.newer.observe(viewLifecycleOwner){
            if(viewModel.countNewPosts > 0){
                binding.newsButton.text ="${viewModel.countNewPosts} new posts"
                binding.newsButton.isVisible = true
            }
        }

       return binding.root
    }

}