package com.nammashale.shalepride.ui.admin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.nammashale.shalepride.ShaleApplication
import com.nammashale.shalepride.data.repository.FeedbackRepository
import com.nammashale.shalepride.databinding.FragmentReportBinding
import com.nammashale.shalepride.util.PdfReportGenerator
import com.nammashale.shalepride.util.hide
import com.nammashale.shalepride.util.show
import com.nammashale.shalepride.util.showSnackbar
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Feature 4: PDF Report Generation Screen
 * Admin selects a report type and downloads a styled PDF.
 */
class ReportFragment : Fragment() {

    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarReport.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val db = ShaleApplication.instance.database

        // School Summary Report
        binding.cardSummaryReport.setOnClickListener {
            showProgress(true)
            lifecycleScope.launch {
                try {
                    val meals    = db.mealDao().getMealCount().first()
                    val posts    = db.postDao().getTotalPostCount().first()
                    val stars    = db.studentStarDao().getStudentStarCount().first()
                    val feedback = db.feedbackDao().getFeedbackCount().first()

                    val data = PdfReportGenerator.ReportData(meals, posts, stars, feedback)
                    val file = PdfReportGenerator.generateSchoolSummaryReport(requireContext(), data)

                    showProgress(false)
                    if (file != null) sharePdf(file)
                    else binding.root.showSnackbar("Failed to generate report")
                } catch (e: Exception) {
                    showProgress(false)
                    binding.root.showSnackbar("Error: ${e.message}")
                }
            }
        }

        // Feedback Report
        binding.cardFeedbackReport.setOnClickListener {
            showProgress(true)
            lifecycleScope.launch {
                try {
                    val feedbackList = FeedbackRepository(db.feedbackDao()).getAllFeedback().first()
                    val file = PdfReportGenerator.generateFeedbackReport(requireContext(), feedbackList)

                    showProgress(false)
                    if (file != null) sharePdf(file)
                    else binding.root.showSnackbar("Failed to generate report")
                } catch (e: Exception) {
                    showProgress(false)
                    binding.root.showSnackbar("Error: ${e.message}")
                }
            }
        }
    }

    private fun showProgress(show: Boolean) {
        if (show) {
            binding.layoutReportProgress.show()
            binding.cardSummaryReport.isEnabled = false
            binding.cardFeedbackReport.isEnabled = false
        } else {
            binding.layoutReportProgress.hide()
            binding.cardSummaryReport.isEnabled = true
            binding.cardFeedbackReport.isEnabled = true
        }
    }

    private fun sharePdf(file: java.io.File) {
        try {
            val uri = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.provider",
                file
            )
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(Intent.createChooser(intent, "Open PDF Report"))
            binding.root.showSnackbar("Report saved: ${file.name}")
        } catch (e: Exception) {
            val uri = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.provider",
                file
            )
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(Intent.createChooser(shareIntent, "Share PDF Report"))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
