package ar.edu.unahur.obj2.minions

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe



class TestLaboratorio : DescribeSpec ({

    // instanciando de empleados
    val dave = Empleado (Obrero(),Biclopes,6)
    val stuart = Empleado (Obrero(),Ciclopes,4)
    val tim = Empleado (Soldado(),Biclopes,5)
    val mark = Empleado (Soldado(),Ciclopes,3)
    val kevin = Empleado (Limpiador(),Biclopes,3)
    val phil = Empleado (Limpiador(),Ciclopes,7)
    val albert = Empleado(Obrero(),Biclopes,6)
    val julit = Empleado(Limpiador(),Ciclopes,9)

    // sectores
    val deposito = Sector (true,false,0)
    val cocina = Sector (true,false,0)
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



    describe ("test 6.- Poder determinar si está en orden el laboratorio ") {

        // agregando sectores
        Laboratorio.sectores.add(deposito)
        Laboratorio.sectores.add(cocina)
        Laboratorio.sectores.add(armeria)
        Laboratorio.sectores.add(cochera)
        Laboratorio.sectores.add(laboratorioExperimentos)

        // sumando empleados
        Laboratorio.empleados.add(dave)
        Laboratorio.empleados.add(stuart)
        Laboratorio.empleados.add(tim)
        Laboratorio.empleados.add(mark)
        Laboratorio.empleados.add(kevin)
        Laboratorio.empleados.add(phil)
        Laboratorio.empleados.add(albert)
        Laboratorio.empleados.add(julit)

        it ("si todos sus sectores están limpios, sin amenazas " +
                "y todos sus minions están contentos") {

            Laboratorio.estaEnOrden().shouldBeFalse() // las cosas estan bastante descontroladas en el laboratorio

            // empecemos a resolver las cosas:
            //limpiando
            phil.realizar(limpiarChiquero)      // limpiando sectores
            cocina.estaLimpio.shouldBeTrue()
            kevin.realizar(trapearDepo)         // limpiando sectores
            deposito.estaLimpio.shouldBeTrue()

            // defendiendo
            tim.comer(Manzana)      // alimentando a los soldados
            mark.comer(Banana)     // alimentando a los soldados
            tim.realizar(desbaratarArmeria)        // soldado haciendo su trabajo
            mark.realizar(liberarCochera)          // soldado haciendo su trabajo
            dave.realizar(resolverBardo)           // este no es soldado, pero se la re-banca

            // vamos a limpiar de nuevo, ya que quedo todo hecho un desastre
            phil.realizar(limpiarSector(armeria))      // limpiando sectores
            armeria.estaLimpio.shouldBeTrue()
            kevin.realizar(limpiarSector(cochera))         // limpiando sectores
            cochera.estaLimpio.shouldBeTrue()
            phil.realizar(limpiarSector(laboratorioExperimentos)) // limpiando sectores
            laboratorioExperimentos.estaLimpio.shouldBeTrue()

            // ahora solo los que tienen baja estamina, van a comer para estar mas contentos.
            dave.comer(Banana)
            stuart.comer(Banana)
            tim.comer(Banana)
            mark.comer(Banana)
            kevin.comer(Banana)
            phil.comer(Banana)
            albert.comer(Banana)

            // ahora si. el laboratorio esta en condiciones
            Laboratorio.estaEnOrden().shouldBeTrue()
        }
    }
    describe("test 7.- Ejecutar una jornada laboral ") {

        // haremos que los minions realicen todas las tareas pendientes
        // otras vez tenemos de capataz a bob
        val bob = Empleado(Capataz(), Biclopes, 5)

        // le agregamos a bob dos empleados a cargo
        bob.agregarEmpleadoACargo(dave)
        bob.agregarEmpleadoACargo(stuart)

        // agregando capataz al laboratorio
        Laboratorio.empleados.add(bob)

        // le agregamos herramientas a los obreros para que puedan trabajar
        dave.agregarHerramienta("Llave fuerza")
        dave.agregarHerramienta("Llave francesa")
        dave.agregarHerramienta("Llaves de tuercas")
        stuart.agregarHerramienta("Llave fuerza")
        stuart.agregarHerramienta("Llaves de fusion")
        stuart.agregarHerramienta("Llaves de tuercas")
        albert.agregarHerramienta("Llave fuerza")
        albert.agregarHerramienta("Llave francesa")
        albert.agregarHerramienta("Llaves de tuercas")
        bob.agregarHerramienta("Llave fuerza")
        bob.agregarHerramienta("Llave francesa")
        bob.agregarHerramienta("Llaves de tuercas")

        // agregando tareas pendientes
        Laboratorio.tareasPendientes.add(arreglarMotorFusion)
        Laboratorio.tareasPendientes.add(desbaratarArmeria)
        Laboratorio.tareasPendientes.add(limpiarChiquero)
        Laboratorio.tareasPendientes.add(hacerAndarMotorDiesel)
        Laboratorio.tareasPendientes.add(liberarCochera)
        Laboratorio.tareasPendientes.add(trapearDepo)
        Laboratorio.tareasPendientes.add(repararBomba)
        Laboratorio.tareasPendientes.add(resolverBardo)

        it("cualquiera de los minions que pueda realizar y completada se la saca de la lista") {

            // aranca la jornada con el estado de situacion
            Laboratorio.hayTareasPendientes().shouldBeTrue()  // y si hay mucho por hacer

            // preparemos al plantel =  todos comeran su racion de Banana para poder empezar el dia con todo
            dave.comer(Banana)
            stuart.comer(Banana)
            tim.comer(Banana)
            mark.comer(Banana)
            kevin.comer(Banana)
            phil.comer(Banana)
            albert.comer(Banana)
            julit.comer(Banana)
            bob.comer(Banana)

            // ejecupando jornada laboral
            Laboratorio.jornadaLaboral()

            Laboratorio.hayTareasPendientes().shouldBeFalse()    // ya esta todo hecho
        }

        it ("algunos de los minions NO pueden realizar las tareas") {

            // aranca otra jornada con el estado de situacion de nuevo
            Laboratorio.hayTareasPendientes().shouldBeTrue()  // y si hay mucho por hacer
            Laboratorio.tareasPendientes.size.shouldBe(8)  // para demostrar la cantidad de pendientes

            // ejecupando jornada laboral, pero esta vez no hay bananas para nadie
            shouldThrowAny {Laboratorio.jornadaLaboral()}

            Laboratorio.hayTareasPendientes().shouldBeTrue()  // quedan pendientes
            Laboratorio.tareasPendientes.size.shouldBe(2)  // para demostrar la cantidad de pendientes
        }

    }

})