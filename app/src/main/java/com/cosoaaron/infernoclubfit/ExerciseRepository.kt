package com.cosoaaron.infernoclubfit

object ExerciseRepository {
    val groupedExercises = mapOf(
        "PECHO" to listOf(
            "Press de Banca (Barra)", "Press de Banca (Mancuernas)", "Press Inclinado (Barra)",
            "Press Inclinado (Mancuernas)", "Press Declinado", "Aperturas (Mancuernas)",
            "Cruce de Poleas (Alto)", "Cruce de Poleas (Bajo)", "Fondos en Paralelas",
            "Flexiones (Push-ups)", "Press en Máquina (Chest Press)", "Pull-over (Mancuerna)",
            "Aperturas en Máquina (Peck Deck)", "Press Landmine"
        ),
        "ESPALDA" to listOf(
            "Dominadas (Pull-ups)", "Dominadas Supinas (Chin-ups)", "Jalón al Pecho (Polea)",
            "Remo con Barra (Pendlay)", "Remo con Mancuerna (Unilateral)", "Remo en Polea Baja (Gironda)",
            "Remo en Máquina T", "Peso Muerto Convencional", "Peso Muerto Sumo",
            "Pull-over en Polea Alta", "Remo al Mentón", "Hiperextensiones", "Rack Pulls"
        ),
        "PIERNA (CUÁDRICEPS)" to listOf(
            "Sentadilla (Barra Trasera)", "Sentadilla Frontal", "Sentadilla Goblet",
            "Prensa de Piernas 45º", "Prensa Horizontal", "Zancadas (Lunges)",
            "Sentadilla Búlgara", "Extensiones de Cuádriceps", "Sentadilla Hack", "Sissy Squat"
        ),
        "PIERNA (FEMORAL/GLÚTEO)" to listOf(
            "Peso Muerto Rumano", "Peso Muerto Piernas Rígidas", "Hip Thrust (Puente de Glúteo)",
            "Curl Femoral Tumbado", "Curl Femoral Sentado", "Patada de Glúteo (Polea)",
            "Abducción de Cadera (Máquina)", "Buenos Días (Barra)"
        ),
        "HOMBRO" to listOf(
            "Press Militar (Barra)", "Press Militar (Mancuernas)", "Press Arnold",
            "Elevaciones Laterales (Mancuernas)", "Elevaciones Laterales (Polea)",
            "Elevaciones Frontales", "Pájaros (Posterior)", "Face Pull (Polea)",
            "Encogimientos (Trapecio)"
        ),
        "BÍCEPS" to listOf(
            "Curl con Barra (Z o Recta)", "Curl con Mancuernas", "Curl Martillo",
            "Curl Predicador (Banco Scott)", "Curl Concentrado", "Curl en Polea Baja",
            "Curl Araña (Spider)", "Dominadas Supinas (Bíceps)"
        ),
        "TRÍCEPS" to listOf(
            "Extensiones en Polea (Cuerda)", "Extensiones en Polea (Barra)", "Press Francés",
            "Rompecráneos (Skullcrushers)", "Patada de Tríceps", "Fondos entre Bancos",
            "Press de Banca Agarre Estrecho"
        ),
        "ABDOMINALES / CORE" to listOf(
            "Crunches (Encogimientos)", "Elevación de Piernas (Colgado)", "Plancha Abdominal (Plank)",
            "Rueda Abdominal", "Russian Twist", "Leñador (Woodchopper)", "Vacío Abdominal"
        ),
        "CARDIO / OTROS" to listOf(
            "Cinta de Correr", "Elíptica", "Bicicleta Estática", "Remo (Ergómetro)",
            "Salto a la Comba", "Burpees", "Box Jump"
        )
    )

    // Lista plana para búsquedas rápidas
    val allExercisesList = groupedExercises.values.flatten().sorted()
}