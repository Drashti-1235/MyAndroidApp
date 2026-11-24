
package com.example.aistudymate.data.repository

import com.example.aistudymate.data.local.UserDao
import com.example.aistudymate.data.local.UserEntity

class UserRepository(
    private val userDao: UserDao
) {

    suspend fun insertUser(user: UserEntity) {
        userDao.insertUser(user)
    }

    suspend fun getUserByEmail(email: String): UserEntity? {
        return userDao.getUserByEmail(email)
    }
}
