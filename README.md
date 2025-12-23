# 🧩 Pictorutinas

Pictorutinas es una aplicación Android nativa desarrollada en Java que permite la creación y ejecución de rutinas visuales mediante pictogramas. El proyecto está enfocado a mejorar la accesibilidad cognitiva y la autonomía de personas con Necesidades Específicas de Apoyo Educativo (NEAE), utilizando apoyos visuales como principal medio de interacción.

## 🎯 Finalidad
Facilitar la anticipación, comprensión y organización de tareas diarias mediante rutinas visuales secuenciales, reduciendo la carga cognitiva y la ansiedad asociada a cambios o instrucciones complejas.

## 🛠️ Stack tecnológico
- Lenguaje: Java
- UI: XML (Views tradicionales)
- Persistencia: SQLite
- Arquitectura: Separación por capas (UI / Modelo / Datos)
- Target SDK: Android 14 (API 34)
- Internacionalización: Español e Inglés

## 📱 Arquitectura del proyecto
El proyecto sigue una organización modular orientada a la separación de responsabilidades:

- `data/`: Gestión de la base de datos SQLite y repositorios de acceso a datos.
- `model/`: Definición de entidades de dominio (`Routine`, `Step`).
- `ui/`: Activities y adaptadores organizados por funcionalidad.
- `res/`: Recursos XML, pictogramas y cadenas multi-idioma.

## ⚙️ Funcionalidades implementadas
- CRUD de rutinas mediante base de datos SQLite.
- Asociación ordenada de pasos a cada rutina.
- Ejecución secuencial de rutinas paso a paso.
- Uso de notificaciones (Toast, Snackbar) y diálogos de confirmación.
- Interfaz accesible, visual y adaptada a usuarios con necesidades cognitivas.

## 📦 Requisitos
- Android Studio
- Dispositivo o emulador con Android 14 (API 34)

