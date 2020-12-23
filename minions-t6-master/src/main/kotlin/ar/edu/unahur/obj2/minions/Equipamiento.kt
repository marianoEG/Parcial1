package ar.edu.unahur.obj2.minions

abstract class Equipamiento() { var herramientas = mutableListOf<String>() }

class Arma() : Equipamiento()

class Cinturon() : Equipamiento()

class ManosLimpias() : Equipamiento()

