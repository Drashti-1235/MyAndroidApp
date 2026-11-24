
package com.example.aistudymate.data.repository

import com.example.aistudymate.data.model.User

class AuthRepository {

    private val users = mutableListOf<User>()

    var currentUser: User? = null
        private set

    fun register(user: User): Boolean {
        if (users.any { it.email == user.email }) {
            return false
        }

        users.add(user)
        currentUser = user
        return true
    }

    fun login(email: String): User? {
        val user = users.find {
            it.email == email
        }

        currentUser = user
        return user
    }

    fun logout() {
        currentUser = null
    }
}
