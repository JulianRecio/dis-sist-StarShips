package edu.austral.ingsis.starships

import GameState
import Model.Entity
import Model.Enums.ButtonKey
import Model.Enums.EntityType
import Model.Enums.HitBoxType
import edu.austral.ingsis.starships.Starships.Companion.ASTEROID_IMG
import edu.austral.ingsis.starships.Starships.Companion.SHOT_IMG
import edu.austral.ingsis.starships.Starships.Companion.STARSHIP_IMAGE_REF
import edu.austral.ingsis.starships.ui.*
import javafx.application.Application
import javafx.application.Application.launch
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.Scene
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.stage.Stage

fun main() {
    launch(Starships::class.java)
}

class Starships() : Application() {
    private val imageResolver = CachedImageResolver(DefaultImageResolver())
    private val facade = ElementsViewFacade(imageResolver)
    private val keyTracker = KeyTracker()

    companion object {
        val STARSHIP_IMAGE_REF = ImageRef("starship", 70.0, 70.0)
        val SHOT_IMG = ImageRef("shot",70.0,70.0)
        val ASTEROID_IMG = ImageRef("asteroid", 70.0,70.0)
        val gameState = GameState()
    }

    override fun start(primaryStage: Stage) {
        
        val pane = gameScene()
        val menu = menuScene(primaryStage, pane)

        facade.timeListenable.addEventListener(TimeListener(facade.elements, gameState, facade, this))
        facade.collisionsListenable.addEventListener(CollisionListener(gameState))
        keyTracker.keyPressedListenable.addEventListener(KeyPressedListener(gameState, pane, primaryStage, this))

        keyTracker.scene = menu

        primaryStage.scene = menu
        primaryStage.height = 800.0
        primaryStage.width = 800.0

        facade.start()
        keyTracker.start()
        primaryStage.show()
    }

    private fun menuScene(primaryStage: Stage, pane: StackPane): Scene {
        val title = Label("Starships")

        val newGame = Label("New Game")
        newGame.setOnMouseClicked {
            primaryStage.scene.root = pane
            gameState.start()
        }

        val hLayout = HBox(70.0)
        hLayout.alignment = Pos.CENTER
        hLayout.children.addAll(newGame)

        val vLayout = VBox(50.0)
        vLayout.id = "menu"
        vLayout.alignment = Pos.CENTER
        vLayout.children.addAll(title, newGame)

        return Scene(vLayout)

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
    private val gameState: GameState,
    private val facade: ElementsViewFacade,
    private val starship: Starships
) : EventListener<TimePassed> {

    override fun handle(event: TimePassed) {
        val entities = gameState.entities ?: return;
        for (entity in entities){
            val element = elements[entity.id]
            if (element != null){
                element.x.set(entity.entityPosition.x)
                element.y.set(entity.entityPosition.y)
                element.rotationInDegrees.set(entity.rotation)
                element.height.set(5.0)
                element.width.set(5.0)
            }
            else{
                facade.elements[entity.id] = ElementModel(entity.id, entity.entityPosition.x, entity.entityPosition.y, 5.0, 5.0,entity.rotation,starship.readHitBox(entity.hitBoxType), getImage(entity))
            }
        }
    }

    private fun getImage(entity: Entity): ImageRef?{
        return when(entity.type){
            EntityType.SHIP -> STARSHIP_IMAGE_REF
            EntityType.SHOT -> SHOT_IMG
            EntityType.ASTEROID -> ASTEROID_IMG
        }
    }

}

class CollisionListener(
    private val gameState: GameState
) : EventListener<Collision> {
    override fun handle(event: Collision) {
        println("${event.element1Id} ${event.element2Id}")
    }

}

class KeyPressedListener(
    private val gameState: GameState,
    private val pane: StackPane,
    private val primaryStage: Stage,
    private val starship: Starships
): EventListener<KeyPressed> {

    override fun handle(event: KeyPressed) {
        val map = gameState.config.keyBoardConfig
        when(event.key){
            map[ButtonKey.UP] -> gameState.controlShip("ship1", ButtonKey.UP)
            map[ButtonKey.DOWN] -> gameState.controlShip("ship1", ButtonKey.DOWN)
            map[ButtonKey.LEFT] -> gameState.controlShip("ship1", ButtonKey.LEFT)
            map[ButtonKey.RIGHT] -> gameState.controlShip("ship1", ButtonKey.RIGHT)
            map[ButtonKey.SHOOT] -> gameState.controlShip("ship1", ButtonKey.SHOOT)
            else -> {}
        }
    }

}