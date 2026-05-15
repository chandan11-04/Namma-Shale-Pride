package com.nammashale.shalepride.ui.admin

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nammashale.shalepride.R
import com.nammashale.shalepride.ShaleApplication
import com.nammashale.shalepride.data.repository.*
import com.nammashale.shalepride.databinding.FragmentAdminBinding
import com.nammashale.shalepride.ui.feedback.FeedbackFragment
import com.nammashale.shalepride.ui.settings.SettingsFragment
import com.nammashale.shalepride.util.*
// Note: ReportFragment is in the same package (ui.admin), no import needed.

class AdminDashboardFragment : Fragment() {

    private var _binding: FragmentAdminBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AdminViewModel
    private var selectedImageUri: Uri? = null

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedImageUri = it
            showAddPostDialog()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = ShaleApplication.instance.database
        viewModel = AdminViewModel(
            MealRepository(db.mealDao()),
            PostRepository(db.postDao()),
            StudentStarRepository(db.studentStarDao()),
            FeedbackRepository(db.feedbackDao())
        )

        observeStats()
        setupButtons()
    }

    private fun observeStats() {
        viewModel.mealCount.observe(viewLifecycleOwner) { binding.tvMealCount.text = it.toString() }
        viewModel.postCount.observe(viewLifecycleOwner) { binding.tvPostCount.text = it.toString() }
        viewModel.starCount.observe(viewLifecycleOwner) { binding.tvStarCount.text = it.toString() }
        viewModel.feedbackCount.observe(viewLifecycleOwner) { binding.tvFeedbackCount.text = it.toString() }
    }

    private fun setupButtons() {
        binding.btnViewFeedback.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, FeedbackFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnSettings.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, SettingsFragment())
                .addToBackStack(null)
                .commit()
        }
        
        binding.btnGenerateReport.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .add(android.R.id.content, ReportFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnUploadMeal.setOnClickListener {
            activity?.let { act ->
                val bottomNav = act.findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNavigation)
                bottomNav?.selectedItemId = R.id.nav_meals
            }
        }

        binding.btnAddStar.setOnClickListener {
            activity?.let { act ->
                val bottomNav = act.findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNavigation)
                bottomNav?.selectedItemId = R.id.nav_stars
            }
        }

        binding.btnManageFacilities.setOnClickListener {
            activity?.let { act ->
                val bottomNav = act.findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNavigation)
                bottomNav?.selectedItemId = R.id.nav_facilities
            }
        }

        // Add Post button - pick image then show form
        binding.btnAddPost.setOnClickListener {
            pickImage.launch("image/*")
        }
    }

    private fun showAddPostDialog() {
        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(64, 32, 64, 16)
        }

        val etTitle = EditText(requireContext()).apply { hint = getString(R.string.post_title) }
        val etDesc = EditText(requireContext()).apply {
            hint = getString(R.string.post_description)
            minLines = 3
        }
        val spinner = Spinner(requireContext())
        val types = listOf("Activity", "Announcement", "Achievement")
        val typeValues = listOf(Constants.POST_TYPE_ACTIVITY, Constants.POST_TYPE_ANNOUNCEMENT, Constants.POST_TYPE_ACHIEVEMENT)
        spinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, types)

        layout.addView(etTitle)
        layout.addView(etDesc)
        layout.addView(spinner)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.add_post))
            .setView(layout)
            .setPositiveButton(getString(R.string.save)) { _, _ ->
                val title = etTitle.text.toString().trim()
                val desc = etDesc.text.toString().trim()
                val type = typeValues[spinner.selectedItemPosition]
                val imageUrl = selectedImageUri?.toString() ?: ""
                val author = ShaleApplication.instance.sessionManager.getUserName()

                if (title.isNotBlank() && desc.isNotBlank()) {
                    viewModel.createPost(title, desc, imageUrl, type, author)
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
