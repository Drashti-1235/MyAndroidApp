package com.example.aistudymate.data.local




import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_materials")
data class StudyMaterialEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,

    val subject: String,

    val pdfUri: String,

    val addedAt: Long = System.currentTimeMillis()
)
