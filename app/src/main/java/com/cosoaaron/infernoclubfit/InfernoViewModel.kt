package com.cosoaaron.infernoclubfit

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class InfernoViewModel : ViewModel() {
    // Esta es nuestra lista de ejercicios "viva"
    private val _exercises = mutableStateListOf<Exercise>()
    val exercises: List<Exercise> get() = _exercises

    // Función para añadir ejercicio
    fun addExercise(name: String, weight: String, reps: String) {
        if (name.isNotEmpty()) {
            _exercises.add(Exercise(name = name, weight = weight, reps = reps))
        }
    }

    // Función para limpiar la lista
    fun clearWorkout() {
        _exercises.clear()
    }
}