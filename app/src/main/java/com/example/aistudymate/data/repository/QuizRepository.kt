package com.example.aistudymate.data.repository

import com.example.aistudymate.data.local.QuizDao
import com.example.aistudymate.data.local.QuizEntity
import kotlinx.coroutines.flow.Flow

class QuizRepository(
    private val quizDao: QuizDao
) {

    val allQuizzes: Flow<List<QuizEntity>> =
        quizDao.getAllQuizzes()

    suspend fun insertQuiz(quiz: QuizEntity) {
        quizDao.insertQuiz(quiz)
    }

    suspend fun deleteQuiz(quiz: QuizEntity) {
        quizDao.deleteQuiz(quiz)
    }
}