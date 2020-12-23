package ar.edu.unahur.obj2.minions

class Maquina(val complejidad : Int, val herramientasNecesarias : List<String>){

    fun necesitaHerramientas() = herramientasNecesarias.size > 0
}