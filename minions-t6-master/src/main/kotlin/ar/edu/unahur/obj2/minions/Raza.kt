package ar.edu.unahur.obj2.minions

abstract class Raza () {

    abstract fun alimentarse(fruta: Fruta, empleado: Empleado)
    abstract fun ojos() : Int
    open fun calculoFuerza(fuerza : Int): Int = fuerza
}

object Biclopes : Raza() {

    override fun ojos() = 2
    override fun alimentarse(fruta: Fruta, empleado: Empleado) {
        empleado.estamina = minOf(empleado.estamina + fruta.energia,10)
    }

}

object Ciclopes : Raza() {

    override fun ojos() = 1
    override fun alimentarse(fruta: Fruta, empleado: Empleado) {
        empleado.estamina = empleado.estamina + fruta.energia
    }
    override fun calculoFuerza(fuerza: Int) = fuerza / 2

}