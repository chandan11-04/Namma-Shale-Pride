package com.nammashale.shalepride.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nammashale.shalepride.R
import com.nammashale.shalepride.ShaleApplication
import com.nammashale.shalepride.data.repository.AuthRepository
import com.nammashale.shalepride.databinding.ActivityForgotPasswordBinding
import com.nammashale.shalepride.util.*

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var viewModel: AuthViewModel

    override fun attachBaseContext(newBase: android.content.Context) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val authRepo = AuthRepository(ShaleApplication.instance.sessionManager)
        viewModel = AuthViewModel(authRepo)

        viewModel.forgotPasswordResult.observe(this) { result ->
            result.onSuccess { msg ->
                binding.root.showSnackbar(msg)
            }
            result.onFailure { error ->
                binding.root.showSnackbar(error.message ?: getString(R.string.something_went_wrong))
            }
        }

        binding.btnReset.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            if (email.isBlank()) {
                binding.tilEmail.error = getString(R.string.enter_email)
            } else {
                binding.tilEmail.error = null
                viewModel.forgotPassword(email)
            }
        }

        binding.tvBackToLogin.setOnClickListener { finish() }
    }
}
