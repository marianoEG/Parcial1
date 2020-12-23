package ar.edu.unahur.obj2.minions

// instanciando de empleados
val dave = Empleado (Obrero(),Biclopes,6)
val stuart = Empleado (Obrero(),Ciclopes,4)
val tim = Empleado (Soldado(),Biclopes,5)
val mark = Empleado (Soldado(),Ciclopes,3)
val kevin = Empleado (Limpiador(),Biclopes,3)
val phil = Empleado (Limpiador(),Ciclopes,7)

// sectores
val deposito = Sector (true,false,2)
val cocina = Sector (true,false,1)
val armeria = Sector (false,true,7)
val cochera = Sector (false,false,4)
val laboratorioExperimentos = Sector (true,false,5)

// maquinas
val motorDiesel = Maquina (6, listOf("Llave fuerza","Llave francesa","Llaves de tuercas"))
val motorFusion = Maquina (9, listOf("Llave fuerza","Llaves de fusion"))
val bombaElectrones = Maquina (6, listOf("Llave fuerza","Llaves de tuercas"))

// tareas
val arreglarMotorFusion = ArreglarMaquina(motorFusion)
val hacerAndarMotorDiesel = ArreglarMaquina(motorDiesel)
val repararBomba = ArreglarMaquina(bombaElectrones)

val desbaratarArmeria = defenderSector(armeria)
val liberarCochera = defenderSector(cochera)
val resolverBardo = defenderSector(laboratorioExperimentos)

val limpiarChiquero = limpiarSector(cocina)
val trapearDepo = limpiarSector(deposito)