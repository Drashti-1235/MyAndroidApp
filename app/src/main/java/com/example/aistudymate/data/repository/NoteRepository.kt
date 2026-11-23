package com.example.aistudymate.data.repository

class NoteRepository {

    private val notes = mutableListOf<Note>()

    fun addNote(note: Note) {
        notes.add(note)
    }

    fun getAllNotes(): List<Note> {
        return notes.toList()
    }

    fun getNoteById(id: String): Note? {
        return notes.find { it.id == id }
    }

    fun updateNote(updatedNote: Note) {
        val index = notes.indexOfFirst {
            it.id == updatedNote.id
        }

        if (index != -1) {
            notes[index] = updatedNote
        }
    }

    fun deleteNote(id: String) {
        notes.removeAll { it.id == id }
    }
}