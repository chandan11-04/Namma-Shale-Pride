package com.nammashale.shalepride.ui.home

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nammashale.shalepride.R
import com.nammashale.shalepride.ShaleApplication
import com.nammashale.shalepride.data.repository.PostRepository
import com.nammashale.shalepride.databinding.FragmentHomeBinding
// Note: BusTrackingFragment is in the same package (ui.home), no import needed.
import com.nammashale.shalepride.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private lateinit var postAdapter: PostAdapter
    private var selectedImageUri: Uri? = null
    
    private var voiceHelper: VoiceHelper? = null // Feature 3: TTS

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedImageUri = it
            showAddPostDialog()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = ShaleApplication.instance.database
        viewModel = HomeViewModel(PostRepository(db.postDao()))
        voiceHelper = VoiceHelper(requireContext())

        setupRecyclerView()
        setupSwipeRefresh()
        setupFab()
        setupBusTracker()
        observeData()
    }
    
    private fun setupBusTracker() {
        // Feature 5: Bus Tracking
        binding.cardBusTracker?.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .add(android.R.id.content, BusTrackingFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setupRecyclerView() {
        val isAdmin = ShaleApplication.instance.sessionManager.isAdmin()
        postAdapter = PostAdapter(
            onDeleteClick = if (isAdmin) { post ->
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(getString(R.string.delete))
                    .setMessage(getString(R.string.confirm_delete))
                    .setPositiveButton(getString(R.string.yes)) { _, _ ->
                        viewModel.deletePost(post.id)
                        binding.root.showSnackbar("Deleted")
                    }
                    .setNegativeButton(getString(R.string.no), null)
                    .show()
            } else null,
            onSpeakClick = { post ->
                val langCode = ShaleApplication.instance.sessionManager.getLanguage()
                val textToSpeak = "${post.title}. ${post.description}"
                if (voiceHelper?.isReady() == true) {
                    voiceHelper?.speak(textToSpeak, langCode)
                    binding.root.showSnackbar("Playing audio...")
                } else {
                    binding.root.showSnackbar("Text-to-speech initializing...")
                }
            }
        )
        binding.rvPosts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = postAdapter
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setColorSchemeResources(
            R.color.primary, R.color.secondary, R.color.tertiary
        )
        binding.swipeRefresh.setOnRefreshListener { viewModel.refresh() }
    }

    private fun setupFab() {
        if (ShaleApplication.instance.sessionManager.isAdmin()) {
            binding.fabAddPost.visibility = View.VISIBLE
            binding.fabAddPost.setOnClickListener {
                pickImage.launch("image/*")
            }
        }
    }

    private fun showAddPostDialog() {
        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(64, 32, 64, 16)
        }
        val etTitle = EditText(requireContext()).apply {
            hint = getString(R.string.post_title)
            textSize = 16f
        }
        val etDesc = EditText(requireContext()).apply {
            hint = getString(R.string.post_description)
            minLines = 3
            textSize = 14f
        }
        val spinner = Spinner(requireContext())
        val types = listOf("Activity", "Announcement", "Achievement")
        val typeValues = listOf(
            Constants.POST_TYPE_ACTIVITY,
            Constants.POST_TYPE_ANNOUNCEMENT,
            Constants.POST_TYPE_ACHIEVEMENT
        )
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
                    binding.root.showSnackbar("Please fill all fields")
                }
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun observeData() {
        viewModel.posts.observe(viewLifecycleOwner) { posts ->
            postAdapter.submitList(posts)
            if (posts.isEmpty()) {
                binding.emptyState.visibility = View.VISIBLE
                binding.rvPosts.visibility = View.GONE
            } else {
                binding.emptyState.visibility = View.GONE
                binding.rvPosts.visibility = View.VISIBLE
            }
        }
        viewModel.isRefreshing.observe(viewLifecycleOwner) { isRefreshing ->
            binding.swipeRefresh.isRefreshing = isRefreshing
        }
    }

    override fun onDestroyView() {
        voiceHelper?.shutdown()
        super.onDestroyView()
        _binding = null
    }
}
