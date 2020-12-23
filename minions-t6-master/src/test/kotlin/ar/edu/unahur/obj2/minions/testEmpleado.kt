package ar.edu.unahur.obj2.minions
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe


class TestEmpleado : DescribeSpec ({

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

    describe ("antes de arrancar") {
        // vamos a instanciar un minions de control
        // un Bíclope Soldado con 10 de estamina y 4 de daño extra tendrá una fuerza de 11 (10 / 2 + 2 + 4)
        val vic = Empleado (Soldado(),Biclopes,10)
        // un Cíclope Soldado con los mismos valores tendrá una fuerza de 5 (redondeamos para abajo)
        val mirc = Empleado (Soldado(),Ciclopes,10)
        // un Cíclope Obrero con 20 de estamina tendrá una fuerza de 6 ((20 / 2 + 2) / 2)
        val clac = Empleado (Obrero(),Ciclopes,20)

        it ("test de algunos ejemplos: Biclope Soldado") {
            // simulamos el aumento en el daño a 4 de vic
            vic.rol.efectoAlDefender()
            vic.rol.efectoAlDefender()

            // un Bíclope Soldado con 10 de estamina y 4 de daño extra tendrá una fuerza de 11 (10 / 2 + 2 + 4)
            vic.fuerza().shouldBe(11)
        }

        it ("test de algunos ejemplos: Cíclope Soldado") {
            // simulamos el aumento en el daño a 4 de mirc
            mirc.rol.efectoAlDefender()
            mirc.rol.efectoAlDefender()

            // un Cíclope Soldado con los mismos valores tendrá una fuerza de 5 (redondeamos para abajo)
            mirc.fuerza().shouldBe(5)
        }

        it ("test de algunos ejemplos: Cíclope Obrero") {
            // un Cíclope Obrero con 20 de estamina tendrá una fuerza de 6 ((20 / 2 + 2) / 2)
            clac.fuerza().shouldBe(6)
        }
    }

    describe("test 1.-") {

        it("Que un empleado pueda comer una fruta para recuperar estamina") {
            dave.estamina.shouldBe(6)
            stuart.estamina.shouldBe(4)
            // comer ambos una fruta: comiendo una manzana recupera 5 de estamina
            dave.comer(Manzana) // dave es biclope
            stuart.comer(Manzana)  // stuart es ciclope
            // vemos su estamina
            dave.estamina.shouldBe(10) // aqui si bien deberia dar 11 de estamina, solo recupera hasta 10
            stuart.estamina.shouldBe(9)
        }
        it("En el caso de los Bíclopes, la estamina no puede superar los 10 puntos, sin importar cuántas " +
                "frutas se ingieran.") {
            // comen otra fruta: comiendo una bamana recupera 10 de estamina
            dave.comer(Banana) // dave es biclope
            stuart.comer(Banana)  // stuart es ciclope
            // vemos su estamina
            dave.estamina.shouldBe(10) // aqui si bien deberia dar 16 de estamina, solo recupera hasta 10
            stuart.estamina.shouldBe(14)
        }
    }

    describe( "test 2.-") {

        it ("Conocer la experiencia de un empleado, que se obtiene a partir de la cantidad de tareas " +
                "realizadas multiplicada por la sumatoria de sus dificultades") {
            // stuart es un obrero
            stuart.tareasRealizadas.add(arreglarMotorFusion)  // complejidad 9
            stuart.tareasRealizadas.add(hacerAndarMotorDiesel)  //complejidad 6
            stuart.experiencia().shouldBe(60)

            // kevin es un limpiador
            kevin.tareasRealizadas.add(limpiarChiquero)  // dificultad de 10 general
            kevin.tareasRealizadas.add(trapearDepo)   // dificultad de 10 general
            kevin.experiencia().shouldBe(40)

            // cambiamos la dificultad general para limpiar
            dificultaDeLimpiarLab.nuevoValor = 7

            //cambiar la experiencia de kevin
            kevin.experiencia().shouldBe(28)

            // tim es un soldado Biclope
            tim.tareasRealizadas.add(desbaratarArmeria)
            tim.tareasRealizadas.add(liberarCochera)
            // mark es un soldado Ciclope
            mark.tareasRealizadas.add(desbaratarArmeria)
            mark.tareasRealizadas.add(liberarCochera)
            tim.experiencia().shouldBe(22)
            mark.experiencia().shouldBe(44)  // el ciclope gana el doble de experiencia
        }
    }

    describe("test 3.-") {
        it ("Saber si un empleado puede realizar una tarea o no: obreros y soldados") {
            // dave es un obrero
            dave.puedeRealizar(repararBomba)
                .shouldBeFalse()  // alcanza con la estamina que tiene, pero le faltan herramientas
            // le agregamos herramientas necesarias a dave
            dave.agregarHerramienta("Llave fuerza")
            dave.agregarHerramienta("Llaves de tuercas")
            dave.puedeRealizar(repararBomba).shouldBeTrue()

            //tim es una soldado Biclope
            tim.puedeRealizar(resolverBardo).shouldBeFalse() // no le alcanza la fuerza

            // hacemos que tim gane estamina
            tim.comer(Manzana)
            tim.puedeRealizar(resolverBardo).shouldBeTrue() // ahora si alcanza la fuerza

            // mark es una soldado Ciclope
            mark.puedeRealizar(resolverBardo).shouldBeFalse()  // no le alcanza la fuerza
            mark.fuerza().shouldBe(1)  // al ser un ciclope si fuerza es la mitad para defender.

            // hacemos que coma como loco
            mark.comer(Banana)
            mark.comer(Manzana)
            mark.estamina.shouldBe(18)
            mark.fuerza().shouldBe(5)
            mark.puedeRealizar(resolverBardo).shouldBeTrue()  // ahora si alcanza la fuerza
        }

        it("Saber si un empleado puede realizar una tarea o no: limpiadores") {
            // phil es un limpiador y siempre limpia
            kevin.puedeRealizar(trapearDepo).shouldBeTrue()   // porque es un limpiador
            // mark es soldado con poca estamina
            mark.puedeRealizar(trapearDepo).shouldBeFalse()    // no le alcanza la estamina

            // phil es limpiador y por eso no defiende
            phil.puedeRealizar(liberarCochera).shouldBeFalse()  // no defiende
        }
    }

    describe("test 4.-") {
        it( "hacer que un empleado realice una tarea, teniendo en cuenta las restricciones descriptas " +
                "anteriormente. Si no puede hacerla, debe lanzarse un error.") {
            // dave es un obrero
            dave.agregarHerramienta("Llave fuerza")
            dave.agregarHerramienta("Llaves de tuercas")
            dave.estamina.shouldBe(6)
            dave.experiencia().shouldBe(0)
            dave.realizar(repararBomba)
            dave.estamina.shouldBe(0)
            dave.experiencia().shouldBe(12)
            // stuart es un obrero
            shouldThrowAny { stuart.realizar(repararBomba) }

            // tim es un soldado
            tim.comer(Manzana)
            tim.estamina.shouldBe(10)
            tim.fuerza().shouldBe(7)
            resolverBardo.sector.gradoAmenaza.shouldBe(5)
            tim.realizar(resolverBardo)
            tim.estamina.shouldBe(5)
            tim.fuerza().shouldBe(6)
            resolverBardo.sector.gradoAmenaza.shouldBe(0)

            // kevin es un limpiador
            kevin.estamina.shouldBe(3)
            cocina.estaLimpio.shouldBeFalse()
            kevin.realizar(limpiarChiquero)
            kevin.estamina.shouldBe(3)
            cocina.estaLimpio.shouldBeTrue()

            // tim ahora va a limpiar
            tim.estamina.shouldBe(5)
            deposito.estaLimpio.shouldBeFalse()
            tim.realizar(trapearDepo)
            tim.estamina.shouldBe(1)
            deposito.estaLimpio.shouldBeTrue()
        }
    }

    describe("test 5.- Agregar un nuevo rol: el Capataz") {

        // tenemos a un nuevo empleado, que es un capataz
        val bob = Empleado(Capataz(), Biclopes, 5)

        // le agregamos dos empleados a cargo
        bob.agregarEmpleadoACargo(dave)
        bob.agregarEmpleadoACargo(stuart)

        it(
            "Cuando se le pide que haga algo, se lo delega a su subordinado con mayor nivel de experiencia " +
                    "de los que puedan realizar la tarea. Su experiencia es la suma de la experiencia de sus subordinados " +
                    "más la propia, que adquiere cuando a él le toca ejecutar alguna tarea. Para saber si puede realizar " +
                    "una tarea, hay que mirar si alguno de sus subordinados puede hacerla o si él mismo puede hacerla."
        ) {

            // para la tarea es necesario que los obreros tengan: "Llave fuerza","Llave francesa","Llaves de tuercas"
            bob.agregarHerramienta("Llave fuerza")
            dave.agregarHerramienta("Llave fuerza")
            dave.agregarHerramienta("Llave francesa")
            stuart.agregarHerramienta("Llave fuerza")
            stuart.agregarHerramienta("Llave francesa")

            // vamos por las tareas. Primero veremos quien puede hacer segun la tarea
            // la complejidad del motor diesel es de 6 y necesita: "Llave fuerza","Llave francesa","Llaves de tuercas"
            bob.puedeRealizar(hacerAndarMotorDiesel)
                .shouldBeFalse() // a todo el grupo de obreros le falta una herramienta

            // le enviamos la tares y vemos que da error.
            shouldThrowAny { bob.realizar(hacerAndarMotorDiesel) }

            // dave tiene la estmina suficiente para la tarea, le agregamos la herramienta que le falta.
            dave.agregarHerramienta("Llaves de tuercas")

            // vamos por la tarea de nuevo
            bob.puedeRealizar(hacerAndarMotorDiesel).shouldBeTrue()

            // hace la tarea y medimos consecuencias
            bob.estamina.shouldBe(5)
            bob.experiencia().shouldBe(0)
            dave.estamina.shouldBe(6)
            dave.experiencia().shouldBe(0)
            stuart.estamina.shouldBe(4)
            stuart.experiencia().shouldBe(0)

            bob.realizar(hacerAndarMotorDiesel)         // ejecucion de tarea

            bob.estamina.shouldBe(5)
            bob.experiencia().shouldBe(12)
            dave.estamina.shouldBe(0)           // efectos de hacer hecho la tarea
            dave.experiencia().shouldBe(12)     // efectos de hacer hecho la tarea
            stuart.estamina.shouldBe(4)
            stuart.experiencia().shouldBe(0)

            // ahora vamos a hacer trabajar a Bob, el capataz
            bob.comer(Manzana)  // agrega estamina
            bob.agregarHerramienta("Llaves de tuercas")
            bob.agregarHerramienta("Llave francesa")

            // ejecutamos la tarea para que la haga Bob y medimos consecuencias
            bob.estamina.shouldBe(10)
            bob.experiencia().shouldBe(12)
            dave.estamina.shouldBe(0)
            dave.experiencia().shouldBe(12)
            stuart.estamina.shouldBe(4)
            stuart.experiencia().shouldBe(0)

            bob.realizar(hacerAndarMotorDiesel)

            bob.estamina.shouldBe(4)     // efectos de hacer hecho la tarea
            bob.experiencia().shouldBe(24)   // efectos de hacer hecho la tarea
            dave.estamina.shouldBe(0)
            dave.experiencia().shouldBe(12)
            stuart.estamina.shouldBe(4)
            stuart.experiencia().shouldBe(0)
        }
    }

    describe("test 5.- Agregar un nuevo rol: el Capataz, otro caso!!! "){

            // otra banda de obreros arreglando cosas
            val Tardis = Maquina(15, listOf("Destornillador sonico"))
            val maquinaIrreparable = Maquina(100,listOf("Llave Yugoslava"))

            val arreglarTardis = ArreglarMaquina(Tardis)
            val tareaIrrealizable = ArreglarMaquina(maquinaIrreparable)
            val Nestor = Empleado(Capataz(),Ciclopes,15)

            Nestor.agregarHerramienta("Llave fuerza")
            Nestor.agregarHerramienta("Destornillador sonico")

            val Guillermo = Empleado(Soldado(),Ciclopes,25)
            val Alberto = Empleado(Obrero(),Biclopes,10)
            val Julio = Empleado(Obrero(),Ciclopes,12)

            Nestor.agregarEmpleadoACargo(Alberto)
            Nestor.agregarEmpleadoACargo(Guillermo)
            Nestor.agregarEmpleadoACargo(Julio)

            it("Solo Nestor puede realizar esta tarea"){
                Nestor.realizar(arreglarTardis)
                Nestor.tareasRealizadas.shouldContain(arreglarTardis)
            }

            it("Nadie puede realizar esta tarea"){
                shouldThrowAny { Nestor.realizar(tareaIrrealizable) }
            }

            it("Guille defiende tiene mas exp"){
                //le ponemos mas experiencia a Guillermo
                Guillermo.tareasRealizadas.add(arreglarTardis)
                Nestor.realizar(resolverBardo)

                Nestor.tareasRealizadas.shouldNotContain(resolverBardo)
                Julio.tareasRealizadas.shouldNotContain(resolverBardo)
                Alberto.tareasRealizadas.shouldNotContain(resolverBardo)
                Guillermo.tareasRealizadas.shouldContain(resolverBardo)
            }

            it("Experiencia del capataz Nestor"){
                //Agregamos exp a los empleados
                Nestor.tareasRealizadas.add(resolverBardo) //10
                Alberto.tareasRealizadas.add(repararBomba)  //12
                Guillermo.tareasRealizadas.add(desbaratarArmeria) //14
                Julio.tareasRealizadas.add(liberarCochera) //8

                Nestor.experiencia().shouldBe(44)
            }
    }

})
