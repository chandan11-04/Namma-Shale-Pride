package com.nammashale.shalepride.ui.stars

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nammashale.shalepride.R
import com.nammashale.shalepride.ShaleApplication
import com.nammashale.shalepride.data.repository.PostRepository
import com.nammashale.shalepride.data.repository.StudentStarRepository
import com.nammashale.shalepride.databinding.FragmentStarsBinding
import com.nammashale.shalepride.util.*

class StarsFragment : Fragment() {

    private var _binding: FragmentStarsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: StarsViewModel
    private lateinit var adapter: StudentStarAdapter
    private var selectedPhotoUri: Uri? = null
    private var isAdmin = false

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedPhotoUri = it
            showAddStarDialog()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentStarsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = ShaleApplication.instance.database
        isAdmin = ShaleApplication.instance.sessionManager.isAdmin()
        viewModel = StarsViewModel(
            StudentStarRepository(db.studentStarDao()),
            PostRepository(db.postDao())
        )

        // Admin: long-press to delete
        adapter = StudentStarAdapter(
            onLongClick = if (isAdmin) { star ->
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(getString(R.string.delete))
                    .setMessage(getString(R.string.confirm_delete))
                    .setPositiveButton(getString(R.string.yes)) { _, _ ->
                        viewModel.deleteStar(star.id)
                        binding.root.showSnackbar("Deleted")
                    }
                    .setNegativeButton(getString(R.string.no), null)
                    .show()
            } else null
        )

        if (isAdmin) {
            binding.fabAddStar.show()
        }

        binding.rvStars.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvStars.adapter = adapter

        binding.fabAddStar.setOnClickListener {
            pickImage.launch("image/*")
        }

        viewModel.allStars.observe(viewLifecycleOwner) { stars ->
            adapter.submitList(stars)
            if (stars.isEmpty()) { binding.tvEmpty.show(); binding.rvStars.hide() }
            else { binding.tvEmpty.hide(); binding.rvStars.show() }
        }
    }

    private fun showAddStarDialog() {
        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(64, 32, 64, 16)
        }

        val etName = EditText(requireContext()).apply { hint = getString(R.string.enter_student_name) }
        val etClass = EditText(requireContext()).apply { hint = getString(R.string.enter_class) }
        val etAchievement = EditText(requireContext()).apply { hint = getString(R.string.enter_achievement) }

        layout.addView(etName)
        layout.addView(etClass)
        layout.addView(etAchievement)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.add_student_star))
            .setView(layout)
            .setPositiveButton(getString(R.string.save)) { _, _ ->
                val name = etName.text.toString().trim()
                val className = etClass.text.toString().trim()
                val achievement = etAchievement.text.toString().trim()
                val photoUrl = selectedPhotoUri?.toString() ?: ""

                if (name.isNotBlank() && className.isNotBlank() && achievement.isNotBlank()) {
                    viewModel.addStar(name, className, achievement, photoUrl)
                    binding.root.showSnackbar(getString(R.string.success))
                } else {
                    binding.root.showSnackbar(getString(R.string.something_went_wrong))
                }
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
