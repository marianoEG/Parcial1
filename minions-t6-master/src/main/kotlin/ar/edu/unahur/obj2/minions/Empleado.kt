package ar.edu.unahur.obj2.minions


class Empleado (var rol: Rol, val raza: Raza, var estamina: Int) {

    val empleadosACargo = mutableListOf<Empleado>()
    val tareasRealizadas = mutableListOf<Tarea>()

    fun estaContento() = estamina >= 9

    fun agregarHerramienta(herramienta: String) { rol.agregarHerramienta(herramienta) }

    //1
    fun comer(fruta: Fruta) { raza.alimentarse(fruta, this) }

    //2
    fun experiencia(): Int = rol.experiencia(this)

    fun cantidadTareasRealizadas(): Int = this.tareasRealizadas.size

    fun dicultadTotalTareasRealizadas(): Int = this.tareasRealizadas.sumBy { it.dificultad(this) }

    //3
    fun puedeRealizar(tarea: Tarea): Boolean = rol.puedeRealizarTarea(tarea,this)

    fun fuerza() = raza.calculoFuerza(rol.calculoFuerza(this))

    //4
    fun realizar(tarea: Tarea) {
        if (this.puedeRealizar(tarea)) {
            rol.realizar(tarea, this)
        } else { throw Error("No se puede ejecutar tarea") }
    }

    //5
    fun agregarEmpleadoACargo(empleado: Empleado)  { empleadosACargo.add(empleado) }

    fun experienciaDeSubordinados(): Int = empleadosACargo.sumBy { it.experiencia() }
}