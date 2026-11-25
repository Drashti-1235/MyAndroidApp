
package com.example.aistudymate.data.repository

import android.util.Base64
import com.example.aistudymate.data.local.UserDao
import com.example.aistudymate.data.local.UserEntity
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

class AuthRepository(private val userDao: UserDao) {

    suspend fun register(
        name: String,
        email: String,
        password: String
    ): UserEntity {

        val cleanName = name.trim()
        val cleanEmail = email.trim().lowercase()

        require(cleanName.isNotEmpty()) {
            "Please enter your name"
        }

        require(cleanEmail.contains("@")) {
            "Please enter a valid email"
        }

        require(password.length >= 8) {
            "Password must be at least 8 characters"
        }

        if (userDao.getUserByEmail(cleanEmail) != null) {
            throw IllegalArgumentException("Email already registered")
        }

        val hashedPassword = PasswordHasher.hash(password)

        val user = UserEntity(
            name = cleanName,
            email = cleanEmail,
            password = hashedPassword
        )

        userDao.insertUser(user)

        return userDao.getUserByEmail(cleanEmail)
            ?: throw IllegalStateException("Registration failed")
    }

    suspend fun login(
        email: String,
        password: String
    ): UserEntity {

        val cleanEmail = email.trim().lowercase()

        val user = userDao.getUserByEmail(cleanEmail)
            ?: throw IllegalArgumentException("Invalid email or password")

        if (!PasswordHasher.verify(password, user.password)) {
            throw IllegalArgumentException("Invalid email or password")
        }

        return user
    }
}

private object PasswordHasher {

    private const val ITERATIONS = 150000
    private const val KEY_LENGTH = 256

    fun hash(password: String): String {
        val salt = ByteArray(16)
        SecureRandom().nextBytes(salt)

        val hash = generate(password, salt)

        val saltText = Base64.encodeToString(salt, Base64.NO_WRAP)
        val hashText = Base64.encodeToString(hash, Base64.NO_WRAP)

        return "$saltText:$hashText"
    }

    fun verify(password: String, storedPassword: String): Boolean {
        return try {
            val parts = storedPassword.split(":")

            if (parts.size != 2) return false

            val salt = Base64.decode(parts[0], Base64.NO_WRAP)
            val expectedHash = Base64.decode(parts[1], Base64.NO_WRAP)

            val actualHash = generate(password, salt)

            MessageDigest.isEqual(expectedHash, actualHash)

        } catch (e: Exception) {
            false
        }
    }

    private fun generate(password: String, salt: ByteArray): ByteArray {
        val spec = PBEKeySpec(
            password.toCharArray(),
            salt,
            ITERATIONS,
            KEY_LENGTH
        )

        return try {
            SecretKeyFactory
                .getInstance("PBKDF2WithHmacSHA256")
                .generateSecret(spec)
                .encoded
        } finally {
            spec.clearPassword()
        }
    }
}
