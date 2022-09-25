package ru.zatsoft.mynet.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import ru.zatsoft.mynet.R
import ru.zatsoft.mynet.activity.NewPostFragment.Companion.postArg
import ru.zatsoft.mynet.auth.AppAuth
import ru.zatsoft.mynet.databinding.ActivityMainBinding
import ru.zatsoft.mynet.dto.Post
import ru.zatsoft.mynet.dto.Token
import ru.zatsoft.mynet.viewmodel.AuthViewModel

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val viewModel: AuthViewModel by viewModels()
    private var myToken: Token? = null
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent?.let {
            if (it.action != Intent.ACTION_SEND) {
                return@let
            }
            val post = it.getParcelableExtra<Post>(Intent.EXTRA_TEXT)
            if (post?.content?.isNotBlank() != true) {
                return@let
            }
            val fm = supportFragmentManager
            intent.removeExtra(Intent.EXTRA_TEXT)
//            findNavController(R.id.nav_fragment)
//                .navigate(
//                    R.id.action_placeholder_to_newPostFragment,
//                    Bundle().apply {
//                        postArg = post
//                    }
//                )

        }

        viewModel.data.observe(this){
            invalidateOptionsMenu()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        menu.let {
            it.setGroupVisible(R.id.unauthenticated, !viewModel.authenticated)
            it.setGroupVisible(R.id.authenticated, viewModel.authenticated)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.signin -> {
                val token = bundleOf("token" to myToken)
                findNavController(R.id.nav_fragment)
                    .navigate(R.id.action_placeholder_to_signInFragment , token)
                myToken?.let{AppAuth.getInstance().setAuth(it.id, it.token)}
                true
            }
            R.id.signup -> {
                val tokenUp = Bundle()
                tokenUp.putParcelable("token", myToken)
                findNavController(R.id.nav_fragment)
                    .navigate(R.id.action_placeholder_to_signUpFragment)
                myToken?.let{ AppAuth.getInstance().setAuth( it.id, it.token)}
                true
            }
            R.id.signout -> {
                AppAuth.getInstance().removeAuth()
                true
            }
            else -> super.onOptionsItemSelected(item)

        }
    }
}