package ar.edu.unahur.obj2.minions

import java.lang.Exception

object Laboratorio {
    val sectores = mutableListOf<Sector>()
    val empleados = mutableListOf<Empleado>()
    val tareasPendientes = mutableListOf<Tarea>()

    //6.-
    fun estaEnOrden() = this.sectoresLimpios() && this.sectoresSinAmenazas() && this.empleadosContentos()

    fun sectoresLimpios() = sectores.all { it.estaLimpio }
    fun sectoresSinAmenazas() = sectores.all { it.gradoAmenaza == 0 }
    fun empleadosContentos() = empleados.all { it.estaContento() }

    //7.-
    fun jornadaLaboral() {
        val tareasARealizar = mutableListOf<Tarea>()
        tareasPendientes.forEach{ tareasARealizar.add(it) }
        for (tarea in tareasARealizar) {
            if (this.quienPuedeHacerLaTarea(tarea))   {
                this.estePuedeHacerLaTarea(tarea).realizar(tarea)
                tareasPendientes.remove(tarea)
            }
        }
        if (this.hayTareasPendientes()) { throw Exception ("Quedaron tareas sin resolver") }
    }


    fun hayTareasPendientes(): Boolean = this.tareasPendientes.isNotEmpty()

    fun quienPuedeHacerLaTarea(tarea:Tarea):Boolean = this.empleados.any{ it.puedeRealizar(tarea) }

    fun estePuedeHacerLaTarea(tarea:Tarea):Empleado = empleados.find{ it.puedeRealizar(tarea) }!!

}