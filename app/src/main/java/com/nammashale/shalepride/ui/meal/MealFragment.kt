package com.nammashale.shalepride.ui.meal

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nammashale.shalepride.R
import com.nammashale.shalepride.ShaleApplication
import com.nammashale.shalepride.data.model.Meal
import com.nammashale.shalepride.data.repository.MealRepository
import com.nammashale.shalepride.data.repository.PostRepository
import com.nammashale.shalepride.databinding.FragmentMealBinding
import com.nammashale.shalepride.util.*

class MealFragment : Fragment() {

    private var _binding: FragmentMealBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MealViewModel
    private var selectedImageUri: Uri? = null
    private var isAdmin = false

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedImageUri = it
            showUploadDialog()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMealBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = ShaleApplication.instance.database
        val mealRepo = MealRepository(db.mealDao())
        val postRepo = PostRepository(db.postDao())
        viewModel = MealViewModel(mealRepo, postRepo)

        isAdmin = ShaleApplication.instance.sessionManager.isAdmin()

        if (isAdmin) {
            binding.fabUploadMeal.show()
        }

        setupMealHistory()
        observeData()

        binding.fabUploadMeal.setOnClickListener {
            if (isAdmin) {
                viewModel.checkTodayMeal()
            }
        }

        viewModel.checkTodayMeal()
    }

    private fun setupMealHistory() {
        binding.rvMealHistory.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeData() {
        viewModel.todayMeal.observe(viewLifecycleOwner) { meal ->
            if (meal != null) {
                binding.cardTodayMeal.show()
                binding.cardNoMeal.hide()
                binding.cardMealUploaded.show()
                binding.ivTodayMeal.loadImage(meal.imageUrl)
                binding.tvTodayMenu.text = getString(R.string.meal_menu) + ": " + meal.menu
                binding.tvTodayDate.text = DateUtils.formatForDisplay(meal.date)
            } else {
                binding.cardTodayMeal.hide()
                binding.cardNoMeal.show()
                binding.cardMealUploaded.hide()
            }
        }

        viewModel.allMeals.observe(viewLifecycleOwner) { meals ->
            val adapter = MealHistoryAdapter(meals)
            binding.rvMealHistory.adapter = adapter
        }

        viewModel.isMealUploadedToday.observe(viewLifecycleOwner) { uploaded ->
            if (isAdmin) {
                if (uploaded) {
                    binding.root.showSnackbar(getString(R.string.meal_already_uploaded))
                } else {
                    pickImage.launch("image/*")
                }
            }
        }

        viewModel.uploadResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                binding.root.showSnackbar(getString(R.string.upload_success))
                // Feature 1: Trigger AI Meal Quality Scanner
                val menuText = viewModel.todayMeal.value?.menu ?: ""
                val scanner = MealScannerSheet(menuText)
                scanner.show(parentFragmentManager, "MealScanner")
            }
            result.onFailure { error ->
                binding.root.showSnackbar(error.message ?: getString(R.string.upload_failed))
            }
        }
    }

    private fun showUploadDialog() {
        selectedImageUri?.let { uri ->
            val dialog = MealUploadDialog(requireContext(), uri) { menu ->
                val imageUriString = uri.toString()
                val userName = ShaleApplication.instance.sessionManager.getUserName()

                try {
                    CloudinaryHelper.uploadImage(uri, Constants.CLOUDINARY_FOLDER_MEALS,
                        object : CloudinaryHelper.UploadListener {
                            override fun onStart() {
                                activity?.runOnUiThread {
                                    binding.root.showSnackbar(getString(R.string.uploading))
                                }
                            }
                            override fun onProgress(progress: Int) {}
                            override fun onSuccess(imageUrl: String, publicId: String) {
                                activity?.runOnUiThread {
                                    viewModel.uploadMeal(menu, imageUrl, userName)
                                }
                            }
                            override fun onError(error: String) {
                                activity?.runOnUiThread {
                                    viewModel.uploadMeal(menu, imageUriString, userName)
                                }
                            }
                        })
                } catch (e: Exception) {
                    viewModel.uploadMeal(menu, imageUriString, userName)
                }
            }
            dialog.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class MealHistoryAdapter(private val meals: List<Meal>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<MealHistoryAdapter.ViewHolder>() {

    class ViewHolder(val binding: com.nammashale.shalepride.databinding.ItemMealBinding) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = com.nammashale.shalepride.databinding.ItemMealBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val meal = meals[position]
        holder.binding.tvMealDate.text = DateUtils.formatForDisplay(meal.date)
        holder.binding.tvMealMenu.text = meal.menu
        holder.binding.ivMealThumb.loadImage(meal.imageUrl)
    }

    override fun getItemCount() = meals.size
}
