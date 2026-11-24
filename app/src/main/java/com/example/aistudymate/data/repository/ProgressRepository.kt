
package com.example.aistudymate.data.repository

import com.example.aistudymate.data.model.Progress

class ProgressRepository {

    private val progressList = mutableListOf<Progress>()

    fun saveProgress(progress: Progress) {
        val index = progressList.indexOfFirst {
            it.userId == progress.userId
        }

        if (index != -1) {
            progressList[index] = progress
        } else {
            progressList.add(progress)
        }
    }

    fun getProgress(userId: String): Progress? {
        return progressList.find {
            it.userId == userId
        }
    }

    fun getAllProgress(): List<Progress> {
        return progressList.toList()
    }
}
