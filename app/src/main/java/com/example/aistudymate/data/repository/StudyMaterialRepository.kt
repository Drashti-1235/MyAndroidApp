package com.example.aistudymate.data.repository



import com.example.aistudymate.data.local.StudyMaterialDao
import com.example.aistudymate.data.local.StudyMaterialEntity
import kotlinx.coroutines.flow.Flow

class StudyMaterialRepository(
    private val studyMaterialDao: StudyMaterialDao
) {

    val allMaterials: Flow<List<StudyMaterialEntity>> =
        studyMaterialDao.getAllMaterials()

    suspend fun insertMaterial(
        material: StudyMaterialEntity
    ) {
        studyMaterialDao.insertMaterial(material)
    }

    suspend fun deleteMaterial(
        material: StudyMaterialEntity
    ) {
        studyMaterialDao.deleteMaterial(material)
    }

    fun getMaterialsBySubject(
        subject: String
    ): Flow<List<StudyMaterialEntity>> {
        return studyMaterialDao.getMaterialsBySubject(subject)
    }
}
