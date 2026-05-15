package com.nammashale.shalepride.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nammashale.shalepride.R
import com.nammashale.shalepride.data.model.Post
import com.nammashale.shalepride.databinding.ItemPostBinding
import com.nammashale.shalepride.util.Constants
import com.nammashale.shalepride.util.DateUtils
import com.nammashale.shalepride.util.loadImage

class PostAdapter(
    private val onPostClick: (Post) -> Unit = {},
    private val onDeleteClick: ((Post) -> Unit)? = null,
    private val onSpeakClick: ((Post) -> Unit)? = null // Feature 3: TTS
) : ListAdapter<Post, PostAdapter.PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PostViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) {
            binding.apply {
                tvPostTitle.text = post.title
                tvPostDescription.text = post.description
                tvTimestamp.text = DateUtils.getRelativeTime(post.timestamp)

                val context = root.context

                // Set type badge with colored background
                when (post.type) {
                    Constants.POST_TYPE_ACTIVITY -> {
                        tvPostType.text = context.getString(R.string.activity).uppercase()
                        tvPostType.background.setTint(context.getColor(R.color.badge_activity))
                    }
                    Constants.POST_TYPE_MEAL -> {
                        tvPostType.text = context.getString(R.string.meal).uppercase()
                        tvPostType.background.setTint(context.getColor(R.color.badge_meal))
                    }
                    Constants.POST_TYPE_ACHIEVEMENT -> {
                        tvPostType.text = context.getString(R.string.achievement).uppercase()
                        tvPostType.background.setTint(context.getColor(R.color.badge_achievement))
                    }
                    Constants.POST_TYPE_ANNOUNCEMENT -> {
                        tvPostType.text = context.getString(R.string.announcement).uppercase()
                        tvPostType.background.setTint(context.getColor(R.color.badge_announcement))
                    }
                }

                // Load image
                if (post.imageUrl.isNotBlank()) {
                    ivPostImage.loadImage(post.imageUrl)
                } else {
                    // Set a colored placeholder based on type
                    val color = when (post.type) {
                        Constants.POST_TYPE_ACTIVITY -> context.getColor(R.color.badge_activity)
                        Constants.POST_TYPE_MEAL -> context.getColor(R.color.badge_meal)
                        Constants.POST_TYPE_ACHIEVEMENT -> context.getColor(R.color.badge_achievement)
                        else -> context.getColor(R.color.badge_announcement)
                    }
                    ivPostImage.setBackgroundColor(color)
                    ivPostImage.setImageResource(R.drawable.ic_placeholder)
                    ivPostImage.scaleType = android.widget.ImageView.ScaleType.CENTER
                }

                root.setOnClickListener { onPostClick(post) }
                root.setOnLongClickListener {
                    onDeleteClick?.invoke(post)
                    true
                }
                
                btnSpeakPost.setOnClickListener {
                    onSpeakClick?.invoke(post)
                }
            }
        }
    }

    class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem
    }
}
