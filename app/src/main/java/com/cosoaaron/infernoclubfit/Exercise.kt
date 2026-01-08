package com.cosoaaron.infernoclubfit

data class Exercise(
    val id: Long = System.currentTimeMillis(), // Identificador único
    val name: String,
    val weight: String,
    val reps: String
)