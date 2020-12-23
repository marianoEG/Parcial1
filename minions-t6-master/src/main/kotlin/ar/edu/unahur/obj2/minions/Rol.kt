package ar.edu.unahur.obj2.minions

abstract class Rol () {
    abstract val equipamiento : Equipamiento
    open fun agregarHerramienta(herramienta : String) {throw Exception("No se puede equipar herramienta")}
    open fun defiendeSector() = true
    open fun calculoFuerza(empleado: Empleado) = (empleado.estamina / 2) + 2
    open fun efectoAlDefender() {}
    open fun experiencia(empleado: Empleado):Int = empleado.cantidadTareasRealizadas() * empleado.dicultadTotalTareasRealizadas()
    open fun puedeRealizarTarea(tarea: Tarea, empleado: Empleado) = tarea.puedeSerEjecutadaPor(empleado)
    open fun realizar(tarea: Tarea, empleado: Empleado) {
        empleado.tareasRealizadas.add(tarea)
        tarea.efectoAlRealizarTarea(empleado)
    }
}

class Soldado() : Rol() {
    var nivelDanio = 0
    override val equipamiento = Arma()
    override fun calculoFuerza(empleado: Empleado) = (empleado.estamina / 2) + 2 + nivelDanio
    override fun efectoAlDefender() { nivelDanio += 2 }
}

open class Obrero()  : Rol() {
    override val equipamiento = Cinturon()
    override fun agregarHerramienta(herramienta: String) { equipamiento.herramientas.add(herramienta) }
}

class Limpiador()  : Rol() {
    override val equipamiento = ManosLimpias()
    override fun defiendeSector() = false
}

class Capataz() : Obrero() {
    override fun experiencia(empleado: Empleado) = super.experiencia(empleado) + empleado.experienciaDeSubordinados()

    override fun puedeRealizarTarea(tarea: Tarea, empleado: Empleado): Boolean =
        tarea.puedeSerEjecutadaPor(empleado) || empleado.empleadosACargo.any { tarea.puedeSerEjecutadaPor(it) }

    override fun realizar(tarea: Tarea, empleado: Empleado) {
        var realizador : Empleado
        if(empleado.empleadosACargo.any(){ it.puedeRealizar(tarea) })
            realizador = this.subordinadoParaTarea(tarea,empleado)
        else{
            realizador = empleado
        }
        realizador.tareasRealizadas.add(tarea)
        tarea.efectoAlRealizarTarea(realizador)
    }
    fun subordinadoParaTarea(tarea: Tarea, empleado: Empleado): Empleado
            = empleado.empleadosACargo.filter { it.puedeRealizar(tarea) }.maxBy { it.experiencia() }!!
}




