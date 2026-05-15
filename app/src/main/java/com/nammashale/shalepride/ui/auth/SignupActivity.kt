package com.nammashale.shalepride.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nammashale.shalepride.R
import com.nammashale.shalepride.ShaleApplication
import com.nammashale.shalepride.data.repository.AuthRepository
import com.nammashale.shalepride.databinding.ActivitySignupBinding
import com.nammashale.shalepride.ui.main.MainActivity
import com.nammashale.shalepride.util.*

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var viewModel: AuthViewModel

    override fun attachBaseContext(newBase: android.content.Context) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val authRepo = AuthRepository(ShaleApplication.instance.sessionManager)
        viewModel = AuthViewModel(authRepo)

        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        viewModel.signupResult.observe(this) { result ->
            result.onSuccess {
                showToast(getString(R.string.success))
                startActivity(Intent(this, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
                finish()
            }
            result.onFailure { error ->
                binding.root.showSnackbar(error.message ?: getString(R.string.something_went_wrong))
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) binding.progressBar.show() else binding.progressBar.hide()
            binding.btnSignup.isEnabled = !isLoading
        }
    }

    private fun setupClickListeners() {
        binding.btnSignup.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val role = if (binding.chipAdmin.isChecked) Constants.ROLE_ADMIN else Constants.ROLE_PARENT

            var isValid = true
            if (name.isBlank()) { binding.tilName.error = getString(R.string.enter_name); isValid = false } else binding.tilName.error = null
            if (email.isBlank()) { binding.tilEmail.error = getString(R.string.enter_email); isValid = false } else binding.tilEmail.error = null
            if (password.isBlank()) { binding.tilPassword.error = getString(R.string.enter_password); isValid = false } else binding.tilPassword.error = null

            if (isValid) viewModel.signup(name, email, password, role)
        }

        binding.tvLogin.setOnClickListener { finish() }
    }
}
