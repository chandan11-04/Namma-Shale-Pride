package com.nammashale.shalepride.data.repository

import android.content.Context
import com.nammashale.shalepride.data.model.User
import com.nammashale.shalepride.util.Constants
import com.nammashale.shalepride.util.SessionManager

class AuthRepository(private val sessionManager: SessionManager) {

    // Simple local auth using SharedPreferences
    // In production, replace with API calls

    fun login(email: String, password: String, role: String): Result<User> {
        return try {
            // Simple validation
            if (email.isBlank() || password.isBlank()) {
                return Result.failure(Exception("Email and password are required"))
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                return Result.failure(Exception("Invalid email format"))
            }

            if (password.length < 6) {
                return Result.failure(Exception("Password must be at least 6 characters"))
            }

            // Create user (in real app, verify against backend)
            val user = User(
                id = email.hashCode().toString(),
                name = email.substringBefore("@").replaceFirstChar { it.uppercase() },
                email = email,
                role = role
            )

            // Save session
            sessionManager.saveUser(user)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signup(name: String, email: String, password: String, role: String): Result<User> {
        return try {
            if (name.isBlank() || email.isBlank() || password.isBlank()) {
                return Result.failure(Exception("All fields are required"))
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                return Result.failure(Exception("Invalid email format"))
            }

            if (password.length < 6) {
                return Result.failure(Exception("Password must be at least 6 characters"))
            }

            val user = User(
                id = email.hashCode().toString(),
                name = name,
                email = email,
                role = role
            )

            sessionManager.saveUser(user)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun forgotPassword(email: String): Result<String> {
        return try {
            if (email.isBlank()) {
                return Result.failure(Exception("Email is required"))
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                return Result.failure(Exception("Invalid email format"))
            }
            // Simulate password reset
            Result.success("Password reset link sent to $email")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun isLoggedIn(): Boolean = sessionManager.isLoggedIn()

    fun logout() = sessionManager.logout()

    fun getCurrentUser(): User = sessionManager.getUser()

    fun isAdmin(): Boolean = sessionManager.isAdmin()
}
