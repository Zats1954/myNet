package ru.zatsoft.mynet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.zatsoft.mynet.BuildConfig
import ru.zatsoft.mynet.R
import ru.zatsoft.mynet.auth.AppAuth

import ru.zatsoft.mynet.databinding.CardPostBinding
import ru.zatsoft.mynet.dto.Post

interface OnInteractionListener {
    fun onLike(post: Post) {}
    fun onEdit(post: Post) {}
    fun onRemove(post: Post) {}
    fun onShare(post: Post) {}
}

class PostsAdapter(private val onInteractionListener: OnInteractionListener) : ListAdapter<Post, PostViewHolder>(PostDiffCallback())
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
    val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return PostViewHolder(binding, onInteractionListener)
}
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}
class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener,
) : RecyclerView.ViewHolder(binding.root){
    val id = AppAuth.getInstance().authStateFlow.value.id
    fun bind(post: Post) {
        binding.apply {
            menu.isVisible =  if(post.authorId == id) true else false
            author.text = post.author
            published.text = post.published
            content.text = post.content
            post.attachment?.let{
                imageView.isVisible = true
                Glide.with(imageView)
                    .load(post.attachment.url )
                    .placeholder(R.drawable.ic_camera_24dp)
                    .error(R.drawable.ic_error_100dp)
                    .override(300,200)
                    .timeout(10_000)
                    .into(imageView)
            }

            Glide.with(avatar)
                .load(BuildConfig.BASE_URL  + "media/" + post.authorAvatar )
                .circleCrop()
                .placeholder(R.drawable.ic_loading_100dp)
                .error(R.drawable.ic_error_100dp)
                .timeout(15_000)
                .into(avatar)
            // в адаптере
            like.isChecked = post.likedByMe
            like.text = "${post.likes}"

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }
                            R.id.edit -> {
                                onInteractionListener.onEdit(post)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }

            like.setOnClickListener {
                onInteractionListener.onLike(post)
            }

            share.setOnClickListener {
                onInteractionListener.onShare(post)
            }
        }
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}
