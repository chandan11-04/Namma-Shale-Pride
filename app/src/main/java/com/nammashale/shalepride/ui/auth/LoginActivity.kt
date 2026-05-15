package com.nammashale.shalepride.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nammashale.shalepride.R
import com.nammashale.shalepride.ShaleApplication
import com.nammashale.shalepride.data.repository.AuthRepository
import com.nammashale.shalepride.databinding.ActivityLoginBinding
import com.nammashale.shalepride.ui.main.MainActivity
import com.nammashale.shalepride.util.*

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: AuthViewModel

    override fun attachBaseContext(newBase: android.content.Context) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val authRepo = AuthRepository(ShaleApplication.instance.sessionManager)
        viewModel = AuthViewModel(authRepo)

        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        viewModel.loginResult.observe(this) { result ->
            result.onSuccess {
                showToast(getString(R.string.success))
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            result.onFailure { error ->
                binding.root.showSnackbar(error.message ?: getString(R.string.something_went_wrong))
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) binding.progressBar.show() else binding.progressBar.hide()
            binding.btnLogin.isEnabled = !isLoading
        }
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val role = if (binding.chipAdmin.isChecked) Constants.ROLE_ADMIN else Constants.ROLE_PARENT

            // Validation
            var isValid = true
            if (email.isBlank()) {
                binding.tilEmail.error = getString(R.string.enter_email)
                isValid = false
            } else {
                binding.tilEmail.error = null
            }
            if (password.isBlank()) {
                binding.tilPassword.error = getString(R.string.enter_password)
                isValid = false
            } else {
                binding.tilPassword.error = null
            }

            if (isValid) {
                viewModel.login(email, password, role)
            }
        }

        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        binding.tvSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
}
