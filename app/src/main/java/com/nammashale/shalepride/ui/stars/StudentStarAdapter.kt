package com.nammashale.shalepride.ui.stars

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nammashale.shalepride.data.model.StudentStar
import com.nammashale.shalepride.databinding.ItemStudentStarBinding
import com.nammashale.shalepride.util.loadCircleImage

class StudentStarAdapter(
    private val onLongClick: ((StudentStar) -> Unit)? = null
) : ListAdapter<StudentStar, StudentStarAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(val binding: ItemStudentStarBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStudentStarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val star = getItem(position)
        holder.binding.apply {
            tvStudentName.text = star.name
            tvClassName.text = star.className
            tvAchievement.text = star.achievement
            ivStudentPhoto.loadCircleImage(star.photoUrl)

            root.setOnLongClickListener {
                onLongClick?.invoke(star)
                true
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<StudentStar>() {
        override fun areItemsTheSame(old: StudentStar, new: StudentStar) = old.id == new.id
        override fun areContentsTheSame(old: StudentStar, new: StudentStar) = old == new
    }
}
