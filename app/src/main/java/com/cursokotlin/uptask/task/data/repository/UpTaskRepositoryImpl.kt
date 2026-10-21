package com.cursokotlin.uptask.task.data.repository

import com.cursokotlin.uptask.task.data.datasource.UpTaskLocalDataSource
import com.cursokotlin.uptask.task.data.model.Materia
import com.cursokotlin.uptask.task.data.model.Tarea
import kotlinx.coroutines.flow.Flow

class UpTaskRepositoryImpl(
    private val dataSource: UpTaskLocalDataSource
) : UpTaskRepository {

    override fun obtenerMaterias(): Flow<List<Materia>> {
        return dataSource.obtenerMaterias()
    }

    override fun obtenerTareas(): Flow<List<Tarea>> {
        return dataSource.obtenerTareas()
    }

    // Si la materia ya existe se actualiza. Si no, se crea con un id nuevo.
    override suspend fun guardarMateria(materia: Materia) {
        dataSource.actualizarMaterias { lista ->
            val existe = lista.any { it.id == materia.id }
            if (existe) {
                lista.map { actual ->
                    if (actual.id == materia.id) materia.copy(esInicial = actual.esInicial) else actual
                }
            } else {
                val nuevoId = (lista.maxOfOrNull { it.id } ?: 0) + 1
                lista + materia.copy(id = nuevoId)
            }
        }
    }

    override suspend fun alternarArchivada(id: Int) {
        dataSource.actualizarMaterias { lista ->
            lista.map { materia ->
                if (materia.id == id) materia.copy(archivada = !materia.archivada) else materia
            }
        }
    }

    // Si la tarea ya existe se actualiza (conservando si estaba completada). Si no, se crea.
    override suspend fun guardarTarea(tarea: Tarea) {
        val ahora = System.currentTimeMillis()

        dataSource.actualizarTareas { lista ->
            val existe = lista.any { it.id == tarea.id }
            if (existe) {
                lista.map { actual ->
                    if (actual.id == tarea.id) {
                        actual.copy(
                            titulo = tarea.titulo,
                            descripcion = tarea.descripcion,
                            materiaId = tarea.materiaId,
                            fechaEntrega = tarea.fechaEntrega,
                            prioridad = tarea.prioridad,
                            fechaActu = ahora
                        )
                    } else {
                        actual
                    }
                }
            } else {
                val nuevoId = (lista.maxOfOrNull { it.id } ?: 0) + 1
                lista + tarea.copy(id = nuevoId, fechaCreada = ahora, fechaActu = ahora)
            }
        }
    }

    override suspend fun cambiarEstadoTarea(id: Int, completada: Boolean) {
        val ahora = System.currentTimeMillis()

        dataSource.actualizarTareas { lista ->
            lista.map { tarea ->
                if (tarea.id == id) tarea.copy(completada = completada, fechaActu = ahora) else tarea
            }
        }
    }

    override suspend fun eliminarTarea(id: Int) {
        dataSource.actualizarTareas { lista ->
            lista.filter { tarea -> tarea.id != id }
        }
    }
}