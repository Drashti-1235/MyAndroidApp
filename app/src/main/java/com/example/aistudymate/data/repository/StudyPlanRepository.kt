package com.example.aistudymate.data.repository

class StudyPlanRepository {

    private val plans = mutableListOf<StudyPlan>()

    fun addPlan(plan: StudyPlan) {
        plans.add(plan)
    }

    fun getAllPlans(): List<StudyPlan> {
        return plans.toList()
    }

    fun updatePlan(updatedPlan: StudyPlan) {
        val index = plans.indexOfFirst {
            it.id == updatedPlan.id
        }

        if (index != -1) {
            plans[index] = updatedPlan
        }
    }

    fun deletePlan(id: String) {
        plans.removeAll { it.id == id }
    }
}