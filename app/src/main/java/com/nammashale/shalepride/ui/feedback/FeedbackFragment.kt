package com.nammashale.shalepride.ui.feedback

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nammashale.shalepride.R
import com.nammashale.shalepride.ShaleApplication
import com.nammashale.shalepride.data.repository.FeedbackRepository
import com.nammashale.shalepride.databinding.FragmentFeedbackBinding
import com.nammashale.shalepride.util.hide
import com.nammashale.shalepride.util.show
import com.nammashale.shalepride.util.showSnackbar
import com.nammashale.shalepride.util.SentimentAnalyzer
import androidx.activity.result.contract.ActivityResultContracts

class FeedbackFragment : Fragment() {

    private var _binding: FragmentFeedbackBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FeedbackViewModel
    private val adapter = FeedbackAdapter()

    private val speechRecognizerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val res = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val recognizedText = res?.get(0)
            if (!recognizedText.isNullOrEmpty()) {
                val currentText = binding.etFeedback.text.toString()
                val newText = if (currentText.isEmpty()) recognizedText else "$currentText $recognizedText"
                binding.etFeedback.setText(newText)
                binding.etFeedback.setSelection(binding.etFeedback.text?.length ?: 0)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFeedbackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = ShaleApplication.instance.database
        val session = ShaleApplication.instance.sessionManager
        viewModel = FeedbackViewModel(FeedbackRepository(db.feedbackDao()))

        val isAdmin = session.isAdmin()

        if (isAdmin) {
            // Admin: ONLY show feedback list, hide ALL form elements
            binding.cardSendFeedback.hide()
            binding.switchAnonymous.hide()
            binding.btnSubmitFeedback.hide()
            binding.tvFeedbackHeader.show()
            binding.rvFeedback.show()
            binding.rvFeedback.layoutManager = LinearLayoutManager(requireContext())
            binding.rvFeedback.adapter = adapter

            viewModel.allFeedback.observe(viewLifecycleOwner) { feedbackList ->
                adapter.submitList(feedbackList)
                if (feedbackList.isEmpty()) {
                    binding.tvNoFeedback.show()
                    binding.cardSentimentSummary.hide()
                } else {
                    binding.tvNoFeedback.hide()
                    binding.cardSentimentSummary.show()
                    
                    // Calculate and show sentiment summary
                    val pos = feedbackList.count { it.sentiment == SentimentAnalyzer.POSITIVE }
                    val neu = feedbackList.count { it.sentiment == SentimentAnalyzer.NEUTRAL }
                    val neg = feedbackList.count { it.sentiment == SentimentAnalyzer.NEGATIVE }
                    
                    binding.tvSentimentPositive.text = "🟢 $pos"
                    binding.tvSentimentNeutral.text = "🟡 $neu"
                    binding.tvSentimentNegative.text = "🔴 $neg"
                }
            }
        } else {
            // Parent: ONLY show submit form, hide list
            binding.cardSendFeedback.show()
            binding.switchAnonymous.show()
            binding.btnSubmitFeedback.show()
            binding.tvFeedbackHeader.hide()
            binding.rvFeedback.hide()
            binding.tvNoFeedback.hide()
            binding.cardSentimentSummary.hide()

            // Voice input
            binding.btnVoiceInput.setOnClickListener {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                }
                try {
                    speechRecognizerLauncher.launch(intent)
                } catch (e: Exception) {
                    binding.root.showSnackbar("Speech recognition not supported on this device")
                }
            }

            // Anonymous toggle
            binding.switchAnonymous.setOnCheckedChangeListener { _, isChecked ->
                binding.tvAnonymousHint.text = if (isChecked)
                    getString(R.string.anonymous_hint)
                else
                    getString(R.string.identity_visible)
            }

            // Submit feedback
            binding.btnSubmitFeedback.setOnClickListener {
                val message = binding.etFeedback.text.toString().trim()
                val isAnonymous = binding.switchAnonymous.isChecked

                if (message.isBlank()) {
                    binding.tilFeedback.error = getString(R.string.enter_feedback)
                    return@setOnClickListener
                }
                binding.tilFeedback.error = null

                viewModel.submitFeedback(
                    message, isAnonymous, session.getUserName(), session.getUserEmail()
                )
            }

            viewModel.submitResult.observe(viewLifecycleOwner) { result ->
                result.onSuccess {
                    binding.etFeedback.text?.clear()
                    binding.root.showSnackbar(getString(R.string.feedback_sent))
                }
                result.onFailure {
                    binding.root.showSnackbar(it.message ?: getString(R.string.something_went_wrong))
                }
            }
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
