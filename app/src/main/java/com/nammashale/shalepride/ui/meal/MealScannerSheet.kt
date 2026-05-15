package com.nammashale.shalepride.ui.meal

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.nammashale.shalepride.databinding.BottomSheetMealScannerBinding
import com.nammashale.shalepride.util.show
import com.nammashale.shalepride.util.hide

/**
 * Feature 1: AI Meal Quality Scanner (Simulated)
 * Shows a scanning animation after meal upload, then reveals
 * an AI-style nutrition analysis result.
 */
class MealScannerSheet(private val menuText: String) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetMealScannerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetMealScannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startScan()
        binding.btnScanDone.setOnClickListener { dismiss() }
    }

    private fun startScan() {
        binding.layoutScanning.show()
        binding.cardResult.hide()
        binding.btnScanDone.hide()
        binding.tvScanSubtitle.text = "Analyzing nutritional content..."

        // Simulate AI scan delay
        Handler(Looper.getMainLooper()).postDelayed({
            if (isAdded && _binding != null) showResult()
        }, 2200)
    }

    private fun showResult() {
        binding.layoutScanning.hide()
        binding.tvScanSubtitle.text = "Analysis complete ✓"

        val lower = menuText.lowercase()
        val detected = detectItems(lower)
        val status = analyzeNutrition(lower)

        binding.cardResult.show()
        binding.tvNutritionStatus.text = status.label
        binding.tvNutritionDetail.text = status.detail
        binding.tvDetectedItems.text = detected.joinToString(" • ") { "🍽 $it" }

        // Add nutrition chips
        status.chips.forEach { chipText ->
            val chip = Chip(requireContext()).apply {
                text = chipText
                isCheckable = false
                setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_LabelSmall)
            }
            binding.chipGroupNutrition.addView(chip)
        }

        binding.btnScanDone.show()
    }

    private data class NutritionStatus(
        val label: String,
        val detail: String,
        val chips: List<String>
    )

    private fun detectItems(menu: String): List<String> {
        val items = mutableListOf<String>()
        val foodMap = mapOf(
            "rice" to "Rice", "dal" to "Dal", "sambar" to "Sambar",
            "roti" to "Roti", "chapati" to "Chapati", "vegetables" to "Vegetables",
            "veg" to "Mixed Vegetables", "milk" to "Milk", "egg" to "Egg",
            "rasam" to "Rasam", "curd" to "Curd / Yogurt", "palya" to "Palya",
            "chitranna" to "Chitranna", "upma" to "Upma", "idli" to "Idli",
            "dosa" to "Dosa", "fruit" to "Fruit", "salad" to "Salad"
        )
        for ((key, value) in foodMap) {
            if (menu.contains(key)) items.add(value)
        }
        return if (items.isEmpty()) listOf("Mixed Items") else items.take(5)
    }

    private fun analyzeNutrition(menu: String): NutritionStatus {
        val hasProtein = listOf("dal", "egg", "milk", "curd").any { menu.contains(it) }
        val hasCarbs = listOf("rice", "roti", "chapati", "idli", "dosa", "upma").any { menu.contains(it) }
        val hasVeg = listOf("vegetables", "veg", "palya", "salad", "sambar", "rasam").any { menu.contains(it) }

        return when {
            hasProtein && hasCarbs && hasVeg -> NutritionStatus(
                label = "✅ Nutritious & Well-Balanced",
                detail = "Excellent! Carbs + Protein + Vegetables detected",
                chips = listOf("🌾 Carbohydrates", "💪 Protein", "🥦 Vegetables", "⭐ Recommended")
            )
            hasCarbs && hasVeg -> NutritionStatus(
                label = "🟡 Good — Add Protein",
                detail = "Consider adding dal, egg or milk for complete nutrition",
                chips = listOf("🌾 Carbohydrates", "🥦 Vegetables", "⚠ Low Protein")
            )
            hasCarbs -> NutritionStatus(
                label = "🟠 Moderate — Add Vegetables",
                detail = "Adding vegetables and protein will improve nutrition",
                chips = listOf("🌾 Carbohydrates", "⚠ Low Vegetables", "⚠ Low Protein")
            )
            else -> NutritionStatus(
                label = "📋 Meal Recorded",
                detail = "Meal logged successfully. Ensure balanced nutrition daily.",
                chips = listOf("📋 Logged", "🔍 Review Suggested")
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
