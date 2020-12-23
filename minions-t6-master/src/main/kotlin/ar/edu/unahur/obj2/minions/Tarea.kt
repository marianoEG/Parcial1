package ar.edu.unahur.obj2.minions

abstract class Tarea () {

    abstract fun puedeSerEjecutadaPor(empleado: Empleado) : Boolean
    abstract fun dificultad(empleado: Empleado): Int
    abstract fun efectoAlRealizarTarea(empleado: Empleado)

}

class ArreglarMaquina(val maquina : Maquina): Tarea() {

    override fun puedeSerEjecutadaPor(empleado: Empleado) : Boolean {
        if (!maquina.necesitaHerramientas()){
            return empleado.estamina >= maquina.complejidad
        }
        else {
            return (empleado.estamina >= maquina.complejidad) &&
                    empleado.rol.equipamiento.herramientas.containsAll(maquina.herramientasNecesarias)
        }
    }

    override fun efectoAlRealizarTarea(empleado: Empleado) {
        this.arreglarMaquina(empleado)
    }

    fun arreglarMaquina(empleado: Empleado) {
        empleado.estamina = empleado.estamina - maquina.complejidad
    }

    override fun dificultad(empleado: Empleado) = maquina.complejidad * 2
}

class defenderSector(val sector : Sector) : Tarea(){
    val guardarValorAmenaza = sector.gradoAmenaza
    override fun puedeSerEjecutadaPor(empleado: Empleado) =
        empleado.rol.defiendeSector() and (empleado.fuerza() >= sector.gradoAmenaza)

    override fun dificultad(empleado: Empleado): Int {
        val dificultad = when{
            empleado.raza == Biclopes -> guardarValorAmenaza
            else                      -> guardarValorAmenaza * 2
        }
        return dificultad
    }

    override fun efectoAlRealizarTarea(empleado: Empleado){
        this.defenderSector(empleado)
    }

    fun defenderSector(empleado: Empleado) {
        if (empleado.rol is Soldado){
            empleado.rol.efectoAlDefender()
        }
        empleado.estamina = empleado.estamina / 2
        sector.gradoAmenaza = 0
        sector.estaLimpio = false
    }
}

class limpiarSector(val sector: Sector) : Tarea() {

    fun puedeLimpiarSector(empleado: Empleado) =
        if (sector.esGrande) empleado.estamina >= 4 else empleado.estamina >= 1

    override fun puedeSerEjecutadaPor(empleado: Empleado) =
        !empleado.rol.defiendeSector() || this.puedeLimpiarSector(empleado)

    override fun dificultad(empleado: Empleado) = dificultaDeLimpiarLab.nuevoValor.toInt()

    override fun efectoAlRealizarTarea(empleado: Empleado) {
        val estaminaPerdida = when {
            !empleado.rol.defiendeSector() -> 0
            empleado.estamina >= 4 -> 4
            else -> 1 }
        empleado.estamina = maxOf(empleado.estamina - estaminaPerdida,0)
        this.sector.estaLimpio = true
    }

}

object dificultaDeLimpiarLab {
    var nuevoValor = 10
}




