package com.nammashale.shalepride.ui.feedback

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nammashale.shalepride.R
import com.nammashale.shalepride.data.model.Feedback
import com.nammashale.shalepride.databinding.ItemFeedbackBinding
import com.nammashale.shalepride.util.DateUtils
import com.nammashale.shalepride.util.SentimentAnalyzer
import com.nammashale.shalepride.util.hide
import com.nammashale.shalepride.util.show
import android.graphics.Color

class FeedbackAdapter : ListAdapter<Feedback, FeedbackAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(val binding: ItemFeedbackBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFeedbackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val feedback = getItem(position)
        holder.binding.apply {
            tvFeedbackMessage.text = feedback.message
            tvFeedbackTime.text = DateUtils.getRelativeTime(feedback.timestamp)

            if (feedback.isAnonymous) {
                tvSenderName.text = root.context.getString(R.string.anonymous_parent)
                tvAnonymousBadge.show()
            } else {
                tvSenderName.text = feedback.senderName.ifBlank { "Parent" }
                tvAnonymousBadge.hide()
            }

            // Sentiment badge
            tvSentimentBadge.text = "${SentimentAnalyzer.getEmoji(feedback.sentiment)} ${SentimentAnalyzer.getLabel(feedback.sentiment)}"
            tvSentimentBadge.show()
            
            // Set text color for sentiment badge
            val textColor = when (feedback.sentiment) {
                SentimentAnalyzer.POSITIVE -> Color.parseColor("#2E7D32")
                SentimentAnalyzer.NEGATIVE -> Color.parseColor("#C62828")
                else -> Color.parseColor("#F57F17")
            }
            tvSentimentBadge.setTextColor(textColor)
            
            // Set background color for sentiment badge
            val bgColor = when (feedback.sentiment) {
                SentimentAnalyzer.POSITIVE -> Color.parseColor("#E8F5E9")
                SentimentAnalyzer.NEGATIVE -> Color.parseColor("#FFEBEE")
                else -> Color.parseColor("#FFFDE7")
            }
            tvSentimentBadge.background.setTint(bgColor)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Feedback>() {
        override fun areItemsTheSame(old: Feedback, new: Feedback) = old.id == new.id
        override fun areContentsTheSame(old: Feedback, new: Feedback) = old == new
    }
}
