package com.example.aistudymate.data.repository



import com.example.aistudymate.data.local.QuizResultDao
import com.example.aistudymate.data.local.QuizResultEntity
import kotlinx.coroutines.flow.Flow

class QuizResultRepository(
    private val quizResultDao: QuizResultDao
) {

    val allResults: Flow<List<QuizResultEntity>> =
        quizResultDao.getAllResults()

    suspend fun insertResult(result: QuizResultEntity) {
        quizResultDao.insertResult(result)
    }

    suspend fun deleteAllResults() {
        quizResultDao.deleteAllResults()
    }
}

