package edu.austral.ingsis.starships

import starShips.model.Entities.Entity
import starShips.model.Enums.EntityType
import starShips.model.Enums.HitBoxType
import edu.austral.ingsis.starships.ui.*
import javafx.application.Application
import javafx.application.Application.launch
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.Scene
import javafx.scene.input.KeyCode
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import starShips.Game
import kotlin.system.exitProcess

fun main() {
    launch(Starships::class.java)
}

class Starships() : Application() {
    private val imageResolver = CachedImageResolver(DefaultImageResolver())
    private val facade = ElementsViewFacade(imageResolver)
    private val keyTracker = KeyTracker()

    companion object {
        val STARSHIP_P1 = ImageRef("starship1", 70.0, 70.0)
        val STARSHIP_P2 = ImageRef("starship2", 70.0, 70.0)
        val SHOT_IMG = ImageRef("shot",70.0,70.0)
        val ASTEROID_IMG = ImageRef("asteroid", 70.0,70.0)
        val game = Game()
    }

    override fun start(primaryStage: Stage) {
        
        val pane = gameScene()
        val menu = menuScene(primaryStage, pane)
        facade.showGrid.value = false
        facade.showCollider.value = false

        facade.timeListenable.addEventListener(TimeListener(facade.elements, game, facade, this))
        facade.collisionsListenable.addEventListener(CollisionListener(game))
        keyTracker.keyPressedListenable.addEventListener(KeyPressedListener(game, pane, primaryStage, this, menu))

        keyTracker.scene = menu
        primaryStage.scene = menu
        primaryStage.height = 800.0
        primaryStage.width = 800.0

        facade.start()
        keyTracker.start()
        primaryStage.show()
    }

    fun getImage(entity: Entity): ImageRef?{
        if (entity.type == EntityType.SHIP){
            return if (entity.id.equals("starship-1")) STARSHIP_P1
            else STARSHIP_P2
        }
        return if (entity.type == EntityType.SHOT) SHOT_IMG
        else ASTEROID_IMG
    }

    private fun menuScene(primaryStage: Stage, pane: StackPane): Scene {
        val title = Label("Navecitas")

        val newGame = Label("New Game")
        newGame.setOnMouseClicked {
            primaryStage.scene.root = pane
            game.start(false)
            addEntities()
        }

        val hLayout = HBox(70.0)
        hLayout.alignment = Pos.CENTER
        hLayout.children.addAll(newGame)

        val vLayout = VBox(50.0)
        vLayout.id = "menu"
        vLayout.alignment = Pos.CENTER
        vLayout.children.addAll(title, newGame)

        val menu = Scene(vLayout)
        menu.stylesheets.add(this::class.java.classLoader.getResource("styles.css")?.toString())
        return menu

    }

    fun pauseScene(primaryStage: Stage, pane: StackPane, menu: Scene): Scene {
        val unpause = Label("Continue Game")

        unpause.setOnMouseClicked {
            primaryStage.scene = menu
            primaryStage.scene.root = pane
            game.pauseOrUnPause()
        }

        val exitGame = Label("Exit game")
        exitGame.setOnMouseClicked {
            stop()
        }
        val vLayout = VBox(50.0)
        vLayout.id = "pause"
        vLayout.alignment = Pos.CENTER
        vLayout.children.addAll(unpause, exitGame)
        return Scene(vLayout)
    }



    private fun addEntities() {
        val entities = game.entities
        for (entity in entities){
            facade.elements[entity.id] = ElementModel(entity.id, entity.entityPosition.x,entity.entityPosition.y,entity.height, entity.width, entity.rotation, readHitBox(entity.hitBoxType), getImage(entity))
        }
    }

    private fun gameScene(): StackPane {
        val pane = StackPane()
        val root = facade.view
        pane.children.addAll(root)
        root.id = "pane"
        return pane
    }


    override fun stop() {
        facade.stop()
        keyTracker.stop()
        exitProcess(0)
    }

    fun readHitBox(hitBoxType: HitBoxType): ElementColliderType {
        return when(hitBoxType){
            HitBoxType.RECTANGULAR -> ElementColliderType.Rectangular
            HitBoxType.ELLIPTICAL -> ElementColliderType.Elliptical
            HitBoxType.TRIANGULAR -> ElementColliderType.Triangular
        }
    }
}

class TimeListener(
    private val elements: Map<String, ElementModel>,
    private val game: Game,
    private val facade: ElementsViewFacade,
    private val starship: Starships
) : EventListener<TimePassed> {
    override fun handle(event: TimePassed) {
        if (game.isOver){
            starship.stop()
        }
        game.update()
        val entities = game.entities ?: return;
        for (entity in entities){
            val element = elements[entity.id]
            if (element != null){
                element.x.set(entity.entityPosition.x)
                element.y.set(entity.entityPosition.y)
                element.rotationInDegrees.set(entity.rotation)
                element.height.set(entity.height)
                element.width.set(entity.width)
            }
            else{
                facade.elements[entity.id] = ElementModel(entity.id, entity.entityPosition.x, entity.entityPosition.y, 5.0, 5.0,entity.rotation,starship.readHitBox(entity.hitBoxType), starship.getImage(entity))
            }
        }
        val eliminatedEntities = game.eliminated

        for (eliminated in eliminatedEntities){
            if (elements.containsKey(eliminated)){
                facade.elements[eliminated] = null
            }
        }
    }
}


class CollisionListener(
    private val game: Game
) : EventListener<Collision> {
    override fun handle(event: Collision) {
        println("${event.element1Id} ${event.element2Id}")
    }

}

class KeyPressedListener(
    private val game: Game,
    private val pane: StackPane,
    private val primaryStage: Stage,
    private val starships: Starships,
    private val menu: Scene
): EventListener<KeyPressed> {

    override fun handle(event: KeyPressed) {
        val map = game.keyBoardConfig
        when(event.key){
            map["accelerate-1"] ->game.accelerateShip("starship-1", true)
            map["stop-1"] -> game.accelerateShip("starship-1", false)
            map["left-1"] -> game.turnShip("starship-1", -5.0)
            map["right-1"] -> game.turnShip("starship-1", 5.0)
            map["shoot-1"] -> game.shoot("starship-1")
            map["accelerate-2"] -> game.accelerateShip("starship-2", true)
            map["stop-2"] -> game.accelerateShip("starship-2", false)
            map["left-2"] -> game.turnShip("starship-2", -5.0)
            map["right-2"] -> game.turnShip("starship-2", 5.0)
            map["shoot-2"] -> game.shoot("starship-2")
            KeyCode.P ->{
                game.pauseOrUnPause()
               if (game.isPaused){
                   primaryStage.scene = starships.pauseScene(primaryStage, pane, menu)
               }
            }
            else ->{}
        }


    }

}