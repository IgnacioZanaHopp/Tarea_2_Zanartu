Tarea 2 - Pruebas Bloom

Descripción

Esta aplicación en Java Swing permite cargar, ejecutar y analizar pruebas didácticas basadas en la Taxonomía de Bloom. Ofrece:

Carga de ítems desde un archivo CSV.

Interfaz gráfica para navegar pregunta a pregunta.

Resúmenes de rendimiento por nivel de Bloom y tipo de ítem.

Revisión detallada de cada respuesta (correcta/incorrecta).

Requisitos

- Java Development Kit (JDK) 11+
- Sistema operativo Windows/macOS/Linux.

Estructura del proyecto

mi_proyecto/
├─ src/
│  ├─ backend/
│  └─ frontend/
├─ plantilla_preguntas.csv

Cómo compilar y ejecutar
1. cd mi_proyecto/src
2. javac backend/*.java
3. javac -cp . frontend/*.java
4. java -cp . frontend.MainFrame

Formato de archivo de ítems (CSV)
[pregunta,tipo,bloomLevel,tiempo,opciones|...,respuestaCorrecta]
Ejemplo:
¿Capital de Chile?,MULTIPLE_CHOICE,Recordar,20,Santiago|Lima|Buenos Aires,Santiago
#   T a r e a _ 2 _ Z a n a r t u  
 