package com.cursokotlin.uptask.task.data.datasource

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.cursokotlin.uptask.task.data.model.Materia
import com.cursokotlin.uptask.task.data.model.Tarea
import com.cursokotlin.uptask.task.data.model.Prioridad
import com.cursokotlin.uptask.task.presentations.utils.MILISEGUNDOS_POR_DIA
import com.cursokotlin.uptask.task.presentations.utils.hoyEnMillisUtc
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
private const val ETIQUETA = "UpTaskDataSource"

// DataStore de toda la app. Se declara una sola vez, fuera de la clase.
private val Context.dataStore by preferencesDataStore(name = "uptask_datos")

class UpTaskLocalDataSource(private val context: Context) {

    private val gson = Gson()

    // Las "llaves" con las que se guarda cada dato dentro de DataStore
    private val claveMaterias = stringPreferencesKey("materias_json")
    private val claveTareas = stringPreferencesKey("tareas_json")
    private val claveSemilla = booleanPreferencesKey("semilla_cargada")

    // Si DataStore falla al leer, se entregan datos vacíos en lugar de cerrar la app
    private val preferencias: Flow<Preferences> = context.dataStore.data
        .catch { error ->
            if (error is IOException) emit(emptyPreferences()) else throw error
        }

    // ---------- LECTURA: cada vez que algo cambia, el Flow emite la lista nueva ----------

    fun obtenerMaterias(): Flow<List<Materia>> {
        return preferencias.map { datos -> leerMaterias(datos[claveMaterias]) }
    }

    fun obtenerTareas(): Flow<List<Tarea>> {
        return preferencias.map { datos -> leerTareas(datos[claveTareas]) }
    }

    // ---------- ESCRITURA: recibe una función que transforma la lista actual ----------

    suspend fun actualizarMaterias(cambio: (List<Materia>) -> List<Materia>) {
        try {
            context.dataStore.edit { datos ->
                val actuales = leerMaterias(datos[claveMaterias])
                datos[claveMaterias] = gson.toJson(cambio(actuales))
            }
        } catch (e: IOException) {
            Log.e(ETIQUETA, "No se pudieron guardar las materias", e)
        }
    }

    suspend fun actualizarTareas(cambio: (List<Tarea>) -> List<Tarea>) {
        try {
            context.dataStore.edit { datos ->
                val actuales = leerTareas(datos[claveTareas])
                datos[claveTareas] = gson.toJson(cambio(actuales))
            }
        } catch (e: IOException) {
            Log.e(ETIQUETA, "No se pudieron guardar las tareas", e)
        }
    }

    // ---------- DATOS SEMILLA: solo se cargan la primera vez que se abre la app ----------

    suspend fun cargarSemillaSiEsNecesario() {
        try {
            context.dataStore.edit { datos ->
                if (datos[claveSemilla] != true) {
                    datos[claveMaterias] = gson.toJson(crearMateriasSemilla())
                    datos[claveTareas] = gson.toJson(crearTareasSemilla())
                    datos[claveSemilla] = true
                }
            }
        } catch (e: IOException) {
            Log.e(ETIQUETA, "No se pudo cargar la semilla", e)
        }
    }

    // ---------- IMÁGENES ----------

    // Copia la imagen elegida a la memoria interna de la app y devuelve su dirección.
    // Así la imagen sigue existiendo aunque el permiso de la galería caduque.
    suspend fun copiarImagen(uri: String): String? = withContext(Dispatchers.IO) {
        try {
            val entrada = context.contentResolver.openInputStream(uri.toUri())
                ?: return@withContext null

            val carpeta = File(context.filesDir, "materias")
            carpeta.mkdirs()
            val archivo = File(carpeta, "materia_${System.currentTimeMillis()}.jpg")

            entrada.use { origen ->
                archivo.outputStream().use { destino -> origen.copyTo(destino) }
            }
            archivo.toUri().toString()
        } catch (e: Exception) {
            Log.e(ETIQUETA, "No se pudo copiar la imagen", e)
            null
        }
    }

    // ---------- JSON <-> LISTAS ----------

    private fun leerMaterias(json: String?): List<Materia> {
        if (json.isNullOrBlank()) return emptyList()
        return try {
            val tipo = object : TypeToken<List<Materia>>() {}.type
            gson.fromJson<List<Materia>>(json, tipo) ?: emptyList()
        } catch (e: Exception) {
            Log.e(ETIQUETA, "No se pudieron leer las materias", e)
            emptyList()
        }
    }

    private fun leerTareas(json: String?): List<Tarea> {
        if (json.isNullOrBlank()) return emptyList()
        return try {
            val tipo = object : TypeToken<List<Tarea>>() {}.type
            gson.fromJson<List<Tarea>>(json, tipo) ?: emptyList()
        } catch (e: Exception) {
            Log.e(ETIQUETA, "No se pudieron leer las tareas", e)
            emptyList()
        }
    }

    // ---------- SEMILLAS ----------

    private fun crearMateriasSemilla(): List<Materia> {
        return listOf(
            Materia(1, "Aplicaciones Móviles", "#FF2E7D32", esInicial = true),
            Materia(2, "Aplicaciones Web", "#FF0288D1", esInicial = true),
            Materia(3, "Análisis y Diseño", "#FF7B1FA2", esInicial = true),
            Materia(4, "Inglés IV", "#FFEF6C00", esInicial = true),
            Materia(5, "Ética Profesional", "#FF1A237E", esInicial = true),
            Materia(6, "Estructura de Datos", "#FF455A64", esInicial = true),
            Materia(7, "Cálculo Varias Variables", "#FFC2185B", esInicial = true)
        )
    }

    private fun crearTareasSemilla(): List<Tarea> {
        val hoy = hoyEnMillisUtc()
        val ahora = System.currentTimeMillis()

        // Crea una tarea con la fecha de entrega calculada a partir de hoy
        fun nueva(
            id: Int,
            titulo: String,
            descripcion: String,
            materiaId: Int,
            dias: Int,
            prioridad: Prioridad,
            completada: Boolean = false
        ) = Tarea(
            id = id,
            titulo = titulo,
            descripcion = descripcion,
            materiaId = materiaId,
            fechaEntrega = hoy + dias * MILISEGUNDOS_POR_DIA,
            prioridad = prioridad,
            completada = completada,
            fechaCreada = ahora,
            fechaActu = ahora
        )

        return listOf(
            nueva(1, "Investigación Composables", "Investigar los Composables básicos de Material 3", 1, 0, Prioridad.ALTA),
            nueva(2, "Diapositivas de análisis", "Preparar las diapositivas del análisis del proyecto", 3, 1, Prioridad.MEDIA),
            nueva(3, "Contratos de API", "Definir los contratos de la API del proyecto", 2, 2, Prioridad.MEDIA),
            nueva(4, "Revisar rúbrica del proyecto", "Leer la rúbrica y revisar los puntos pendientes", 1, -1, Prioridad.ALTA),
            nueva(5, "Wireframes de la app", "Dibujar los wireframes de las pantallas", 3, 5, Prioridad.BAJA),
            nueva(6, "Mockup UI en Figma", "Diseñar el mockup de la interfaz", 2, -3, Prioridad.BAJA, completada = true),
            nueva(7, "Configuración de StateFlow", "Configurar el StateFlow del ViewModel", 1, -2, Prioridad.MEDIA, completada = true),
            nueva(8, "Past Simple activities", "Resolver los ejercicios de Past Simple", 4, 2, Prioridad.BAJA),
            nueva(9, "Resumen de ética", "Escribir el resumen de la unidad", 5, 3, Prioridad.MEDIA),
            nueva(10, "Árboles binarios", "Implementar los recorridos de un árbol binario", 6, 5, Prioridad.ALTA)
        )
    }
}