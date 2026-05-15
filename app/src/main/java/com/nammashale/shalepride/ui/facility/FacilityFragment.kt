package com.nammashale.shalepride.ui.facility

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nammashale.shalepride.R
import com.nammashale.shalepride.ShaleApplication
import com.nammashale.shalepride.data.repository.FacilityRepository
import com.nammashale.shalepride.databinding.FragmentFacilityBinding
import com.nammashale.shalepride.util.*

class FacilityFragment : Fragment() {

    private var _binding: FragmentFacilityBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FacilityViewModel
    private var selectedImageUri: Uri? = null
    private var isAdmin = false
    private val autoSlideHandler = Handler(Looper.getMainLooper())
    private val autoSlideRunnable = object : Runnable {
        override fun run() {
            val currentItem = binding.viewPagerFacilities.currentItem
            val adapter = binding.viewPagerFacilities.adapter
            if (adapter != null && adapter.itemCount > 0) {
                binding.viewPagerFacilities.currentItem = (currentItem + 1) % adapter.itemCount
            }
            autoSlideHandler.postDelayed(this, 4000)
        }
    }

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedImageUri = it
            showAddFacilityDialog()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFacilityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = ShaleApplication.instance.database
        viewModel = FacilityViewModel(FacilityRepository(db.facilityDao()))
        isAdmin = ShaleApplication.instance.sessionManager.isAdmin()

        if (isAdmin) {
            binding.fabAddFacility.show()
        }

        binding.fabAddFacility.setOnClickListener {
            pickImage.launch("image/*")
        }

        setupCategoryChips()
        observeData()
    }

    private fun setupCategoryChips() {
        Constants.FACILITY_CATEGORIES.forEachIndexed { index, category ->
            val chip = Chip(requireContext()).apply {
                text = category
                isCheckable = true
                isChecked = index == 0
                setOnClickListener { viewModel.selectCategory(category) }
            }
            binding.chipGroupCategories.addView(chip)
        }
        viewModel.selectCategory(Constants.FACILITY_CATEGORIES[0])
    }

    private fun observeData() {
        viewModel.allFacilities.observe(viewLifecycleOwner) { facilities ->
            if (facilities.isEmpty()) {
                binding.tvEmpty.show()
                binding.viewPagerFacilities.hide()
            } else {
                binding.tvEmpty.hide()
                binding.viewPagerFacilities.show()
                val adapter = FacilityPagerAdapter(
                    facilities,
                    onLongClick = if (isAdmin) { facility ->
                        // Admin long-press: Edit or Delete
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle(facility.name)
                            .setItems(arrayOf(getString(R.string.delete))) { _, which ->
                                when (which) {
                                    0 -> {
                                        MaterialAlertDialogBuilder(requireContext())
                                            .setTitle(getString(R.string.delete))
                                            .setMessage(getString(R.string.confirm_delete))
                                            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                                                viewModel.deleteFacility(facility.id)
                                                binding.root.showSnackbar("Deleted")
                                            }
                                            .setNegativeButton(getString(R.string.no), null)
                                            .show()
                                    }
                                }
                            }
                            .show()
                    } else null
                )
                binding.viewPagerFacilities.adapter = adapter
                startAutoSlide()
            }
        }
    }

    private fun showAddFacilityDialog() {
        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(64, 32, 64, 16)
        }

        val etName = EditText(requireContext()).apply { hint = getString(R.string.facility_name) }
        val etDesc = EditText(requireContext()).apply {
            hint = getString(R.string.facility_description)
            minLines = 3
        }
        val spinner = Spinner(requireContext())
        spinner.adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, Constants.FACILITY_CATEGORIES
        )

        layout.addView(etName)
        layout.addView(etDesc)
        layout.addView(spinner)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.add_facility))
            .setView(layout)
            .setPositiveButton(getString(R.string.save)) { _, _ ->
                val name = etName.text.toString().trim()
                val desc = etDesc.text.toString().trim()
                val category = spinner.selectedItem.toString()
                val imageUrl = selectedImageUri?.toString() ?: ""

                if (name.isNotBlank() && desc.isNotBlank()) {
                    viewModel.addFacility(name, desc, imageUrl, category)
                    binding.root.showSnackbar(getString(R.string.success))
                } else {
                    binding.root.showSnackbar(getString(R.string.something_went_wrong))
                }
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun startAutoSlide() {
        autoSlideHandler.removeCallbacks(autoSlideRunnable)
        autoSlideHandler.postDelayed(autoSlideRunnable, 4000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        autoSlideHandler.removeCallbacks(autoSlideRunnable)
        _binding = null
    }
}
