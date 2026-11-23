package com.example.aistudymate.data.repository

class QuizRepository {

    private val quizzes = mutableListOf<Quiz>()

    fun addQuiz(quiz: Quiz) {
        quizzes.add(quiz)
    }

    fun getAllQuizzes(): List<Quiz> {
        return quizzes.toList()
    }

    fun getQuizById(id: String): Quiz? {
        return quizzes.find { it.id == id }
    }

    fun deleteQuiz(id: String) {
        quizzes.removeAll { it.id == id }
    }

    fun getQuizCount(): Int {
        return quizzes.size
    }
}