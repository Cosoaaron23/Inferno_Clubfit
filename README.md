Inferno Clubfit 🔥

Inferno Clubfit es una aplicación nativa de Android desarrollada con Kotlin y Jetpack Compose que combina el seguimiento de entrenamientos de fuerza con una experiencia social gamificada.

Diseñada con una estética "Hardcore" (Dark Mode puro con acentos rojos y naranjas), permite a los usuarios registrar sus sesiones, visualizar su progreso y compartir sus logros con otros "guerreros".

🚧 Estado del Proyecto (Versión 1.0)

⚠️ Nota Importante: Esta es la primera versión (MVP) de la aplicación, desarrollada como proyecto final para la asignatura de Desarrollo de Interfaces.

Actualmente, la aplicación funciona con persistencia de datos en memoria. Esto significa que:

No hay una base de datos real conectada (como Firebase o SQL).

La funcionalidad de "Red Social" (Feed, Likes, Amigos) es simulada para demostrar la interfaz de usuario y la experiencia de navegación.

Los datos se mantienen mientras la aplicación está abierta en memoria, pero no se sincronizan en la nube entre diferentes dispositivos.

El objetivo de esta versión es demostrar la capacidad de diseño de interfaces complejas, navegación y gestión de estado local en Jetpack Compose.

📱 Funcionalidades Principales

1. The Pit (Social Feed) 🏠

Un muro social donde puedes ver la actividad de otros usuarios.

Historias de Guerreros: Barra superior con usuarios activos.

Feed de Actividad: Publicaciones automáticas cuando un usuario termina una rutina.

Interacción: Sistema de "Likes" funcional (a nivel de interfaz).

2. Inferno Arena (Tracker de Entrenamiento) 💪

El núcleo de la aplicación. Un sistema completo para registrar tus sesiones.

Rutinas Personalizables: Crea tus propias rutinas o usa las predefinidas (Pierna, Espalda/Bíceps, Pecho Completo).

Sesión Activa: Interfaz optimizada para el gimnasio con cronómetro de descanso integrado.

Registro Detallado: Tabla para apuntar Kilos y Repeticiones serie a serie.

Buscador de Ejercicios: Base de datos interna con decenas de ejercicios filtrables por nombre.

3. Perfil del Condenado 👤

Tu centro de estadísticas y progreso.

Gráficas: Visualización del volumen de carga semanal.

Mapa de Calor Muscular: Un "muñeco" interactivo que se ilumina según los músculos que has trabajado (Semana/Mes/Año).

Calendario: Heatmap estilo GitHub que marca los días que has entrenado.

Medidas Corporales: Historial para registrar tu peso y medidas musculares.

🛠️ Stack Tecnológico

Lenguaje: Kotlin

UI Toolkit: Jetpack Compose (Material Design 3)

Navegación: Jetpack Navigation Compose

Arquitectura: MVVM (Model-View-ViewModel) simplificado con State Management.

Iconos: Material Icons Extended.

🚀 Roadmap (Próximas Funciones)

El desarrollo de Inferno Clubfit no termina aquí. Estas son las funciones planificadas para la Versión 2.0:

[ ] Base de Datos Real: Implementación de Firebase (Firestore & Auth) para guardar datos en la nube y tener usuarios reales.

[ ] Rangos de Fuerza: Algoritmo para calcular tu nivel (Principiante, Intermedio, Élite) basado en tu peso corporal y levantamientos.

[ ] Modo Competición: Rankings semanales entre amigos.

[ ] Exportación de Datos: Posibilidad de descargar tu historial en CSV.
