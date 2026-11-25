package com.example.aistudymate.data.local



import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyMaterialDao {

    @Insert
    suspend fun insertMaterial(
        material: StudyMaterialEntity
    )

    @Delete
    suspend fun deleteMaterial(
        material: StudyMaterialEntity
    )

    @Query("SELECT * FROM study_materials ORDER BY addedAt DESC")
    fun getAllMaterials(): Flow<List<StudyMaterialEntity>>

    @Query("SELECT * FROM study_materials WHERE subject = :subject ORDER BY addedAt DESC")
    fun getMaterialsBySubject(
        subject: String
    ): Flow<List<StudyMaterialEntity>>
}

