package com.nammashale.shalepride.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nammashale.shalepride.R
import com.nammashale.shalepride.ShaleApplication
import com.nammashale.shalepride.databinding.FragmentSettingsBinding
import com.nammashale.shalepride.ui.auth.LoginActivity
import com.nammashale.shalepride.ui.feedback.FeedbackFragment
import com.nammashale.shalepride.util.Constants
import com.nammashale.shalepride.util.LocaleHelper

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val session by lazy { ShaleApplication.instance.sessionManager }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupProfile()
        setupLanguageToggle()
        setupDarkModeToggle()
        setupButtons()
    }

    private fun setupProfile() {
        binding.tvUserName.text = session.getUserName()
        binding.tvUserEmail.text = session.getUserEmail()
        binding.tvUserRole.text = if (session.isAdmin())
            getString(R.string.role_admin) else getString(R.string.role_parent)
    }

    private fun setupLanguageToggle() {
        val isKannada = session.getLanguage() == Constants.LANG_KANNADA
        binding.switchLanguage.isChecked = isKannada
        binding.tvCurrentLang.text = if (isKannada) getString(R.string.kannada) else getString(R.string.english)

        binding.switchLanguage.setOnCheckedChangeListener { _, isChecked ->
            val lang = if (isChecked) Constants.LANG_KANNADA else Constants.LANG_ENGLISH
            session.setLanguage(lang)
            binding.tvCurrentLang.text = if (isChecked) getString(R.string.kannada) else getString(R.string.english)

            // Recreate activity to apply language
            activity?.recreate()
        }
    }

    private fun setupDarkModeToggle() {
        binding.switchDarkMode.isChecked = session.isDarkMode()

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            session.setDarkMode(isChecked)
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun setupButtons() {
        binding.btnFeedback.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, FeedbackFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnLogout.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.logout))
                .setMessage(getString(R.string.logout_confirm))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    session.logout()
                    startActivity(Intent(requireContext(), LoginActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                    activity?.finish()
                }
                .setNegativeButton(getString(R.string.no), null)
                .show()
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
