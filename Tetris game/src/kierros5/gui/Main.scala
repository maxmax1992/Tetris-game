package kierros5.gui

import kierros5.model._
import kierros5.model.Direction
import java.awt.Dimension
import kierros5._
import scala.swing._
import javax.swing.Timer
import java.awt.Graphics2D
import javax.swing.AbstractAction
import java.awt.event.ActionEvent
import scala.swing.event.ButtonClicked
import javax.swing.JOptionPane
import javax.swing.Icon
import javax.swing.ImageIcon
import scala.swing.event.KeyPressed
import scala.swing.event.Key
import java.awt.Font
import scala.io.Source
import javax.sound.sampled._
import java.io.File
import scala.swing.event.ButtonClicked
import scala.collection.mutable.Buffer
import java.awt.Color
import java.io._

/**
 * @authors Maxim Afteniy 539762, Sami Kalkas 77241H
 */

object Main extends SimpleSwingApplication {
  
  /*
   * Sets the music for the gameplay.
   */
  val file = new File("_-_Tetris_Blitz_soundtrackGame_.wav")
  val audioIn =  AudioSystem.getAudioInputStream(file)
  val clip = AudioSystem.getClip
  clip.open(audioIn)
  clip.loop(Int.MaxValue)
  

  /*
   * Some variables to set the games dimensions, game speed and
   * and some events that happen in the game.
   */
  val GAME_HEIGHT = 21
  val GAME_WIDTH = 10
  val MAIN_HEIGHT = 900
  val MAIN_WIDTH = 900
  var timerSpeed = 900
  var pauseCounter = 0
  var highsCoresVector: Vector[(String,String)] = this.getHighScores()
 
  
  // Creates a new gameGrid.
  var gameGrid = new GameGrid(GAME_HEIGHT, GAME_WIDTH)
  
  /*
   * Creation of the buttons and labels used in the game.
   */
  val quitGame = new Button("Quit Game")
  val gamePause = new Button("Pause") {
    this.focusable = false
  }
  val restartButton = new Button("Restart") {
    this.focusable = false
  }
  val buttonGroup = new ButtonGroup(gamePause, restartButton, quitGame)
  
  val level = new Label("Level: " + gameGrid.playerPoints/1000 + 1)
  val spaceLabel = new Label("\n")
  val titlePanel = new Label("   TOP 3")
  val hiScores = new Label("1: " + this.highsCoresVector.head._1 + " - " + this.highsCoresVector.head._2)
  val hiScores1 = new Label("2: " + this.highsCoresVector(1)._1 + " - " + this.highsCoresVector(1)._2)
  val hiScores2 = new Label("3: " + this.highsCoresVector(2)._1 + " - " + this.highsCoresVector(2)._2)
  val playerpoints = new Label("Points:")
  val points = new Label(this.gameGrid.playerPoints.toString)
 
  playerpoints.font = new Font("Arial", 0, 30)
  points.font = new Font("Arial", 0, 30)
  
  
  /*
   * Creation of a new GamePanel and PiecePanel.
   */
  val gamePanel = new GamePanel(gameGrid) 
  
  val piecePanel = new PiecePanel(this.gameGrid.nextPiece)
  piecePanel.repaint()
  piecePanel.updatePiece(gameGrid.nextPiece)

   
  
  /*
   * Creation of a new BoxPanel and adding all the buttons and labels to it.
   */
  val subPanel = new BoxPanel(Orientation.Vertical) {
    maximumSize = new Dimension(200, 450)
  }
  subPanel.contents += piecePanel
  subPanel.contents += level
  subPanel.contents += playerpoints
  subPanel.contents += points
  subPanel.contents += spaceLabel
  subPanel.contents += titlePanel
  subPanel.contents += hiScores
  subPanel.contents += hiScores1
  subPanel.contents += hiScores2
  subPanel.contents += spaceLabel
  subPanel.contents += restartButton
  subPanel.contents += gamePause
  subPanel.contents += quitGame
  
  /*
   * Creation of a new BoxPanel that holds the GamePanel and the subPanel.
   */
  val allThings  = new BoxPanel(Orientation.Horizontal) {
    focusable = true
    requestFocus()
    
    contents += gamePanel
    contents += subPanel
    
    
    // Listens to a number of keys or buttons pressed.
    listenTo(restartButton)
    listenTo(gamePause)
    listenTo(quitGame)
    listenTo(keys)
    
    // A method to pause the game, if the 'Pause' button is pressed during the game.
    def pause() = {
      if(pauseCounter % 2  == 0) {
        this.deafTo(keys)
        timer.stop()
        pauseCounter += 1
      } 
      else {
        this.listenTo(keys)
        pauseCounter += 1
        timer.start()
      }
    }
    
    /*
     * Method to repaint the main window and to update changes.
     */
    def keysPressed() = {
      gamePanel.repaint()
      piecePanel.updatePiece(gameGrid.nextPiece)
      piecePanel.repaint()
      getPointsChange()
    }
    
    /*
     * All the reactions and events that take place for different keys or
     * buttons that are pressed. These reactions update the view of the game.
     */
    reactions += {
      
      case ButtonClicked(`restartButton`) =>  { 
        timer.stop()
        Dialog.showMessage(gamePanel, "Are yuo sure you want to restart?", "", Dialog.Message.Info) 
        gameGrid.setNewGame()
        timer.start()
      }
      case ButtonClicked(`gamePause`) => {
        pause()
      }
      case ButtonClicked(`quitGame`) => {
        timer.stop
        Dialog.showMessage(gamePanel, "Quitter!", title = "Notification", Dialog.Message.Info)
        closeGame()
      }
      case KeyPressed(_,Key.Up,_,_) => {
        gamePanel.gameGrid.fallingPiece.move(Direction.Up)
        keysPressed()
      }
      case KeyPressed(_,Key.Down,_,_) => {
        gamePanel.gameGrid.fallingPiece.move(Direction.Down)
        keysPressed()
      }
      case KeyPressed(_,Key.Left,_,_) => {
        gamePanel.gameGrid.fallingPiece.move(Direction.Left)
        keysPressed()
      }
      case KeyPressed(_,Key.Right,_,_) => {
        gamePanel.gameGrid.fallingPiece.move(Direction.Right)
        keysPressed()
      }
      case KeyPressed(_,Key.Space,_,_) => {
        gamePanel.gameGrid.fallingPiece.drop()
        keysPressed()
      }

    }
   
  }
  
  // Method to close the mainWindow.
  def closeGame() = mainWindow.closeOperation()
  
  // Updates the view of the players points and level.
  def getPointsChange() = {
    if(this.gameGrid.playerPoints.toString() != this.points.text) {
      this.points.text = this.gameGrid.playerPoints.toString()
      this.level.text = ("Level :" + this.gameGrid.playerPoints/1000 + 1).toString()
    }
  }
  
  /*
   * Updates the speed of the game depending on the progress of the player.
   * Checks also if the game has been lost and restarts a new game.
   */
  def updateState(): Unit = {
    this.level.text = ("Level " + (this.gameGrid.playerPoints/1000 + 1).toString())
    this.timer.setDelay(this.timerSpeed - 50 * this.gameGrid.playerPoints/1000)
    if (this.gameGrid.isLost) {
      timer.stop()
      Dialog.showMessage(new Label(""), "Game Over! \nYour total points were " + this.points.text + ".", title = "Notification!")
    this.hiScore()  
      
    timer.start()
    mainWindow.repaint()
    }
  }
  
  
  /*
   * If the player has scored more than 0 points, his name will be asked and 
   * if his score is high enough, it will be showed in the top 3.
   */
  def hiScore() = {
    if (this.gameGrid.playerPoints != 0) {
        val name = JOptionPane.showInputDialog("Please enter your name: ")
        val topList = new FileWriter("hiscores.txt", true)
        try {
          topList.write(name + " - " + this.gameGrid.playerPoints.toString() + "\n")
        }
        finally topList.close()
      }
    highsCoresVector = this.getHighScores()  
    hiScores.text = "1: " + this.highsCoresVector.head._1 + " - " + this.highsCoresVector.head._2
    hiScores1.text = "2: " + this.highsCoresVector(1)._1 + " - " + this.highsCoresVector(1)._2
    hiScores2.text = "3: " + this.highsCoresVector.last._1 + " - " + this.highsCoresVector.last._2
    Dialog.showMessage(new Label(""), "Restart?", title = "Notification")
    gameGrid.setNewGame()
  }
  
  /*
   * Gets the three highest scores of a list of players and scores to determin 
   * who are in the top 3. This information is shown in the subPanel of the game.
   */
  def getHighScores(): Vector[(String,String)] = {
    val scores = Source.fromFile("Hiscores.txt")
     try {
      var bufferOfNames = Buffer[String]()
      var bufferOfScores = Buffer[String]()
      var lines = scores.getLines.toVector
      
      bufferOfNames = lines.map(x => x.trim().takeWhile(_ != ' ')).toBuffer
      bufferOfScores = lines.map(x => x.reverse).map(x => x.takeWhile(_ != ' ')).map(_.reverse).toBuffer
      var theMap = bufferOfNames.zip(bufferOfScores).toVector
      theMap = theMap.filter(x => x._1 != "" || x._2 != "")
      theMap = theMap.sortBy(_._2.toInt).reverse
      theMap = theMap.filter(_._1 != "null")
      if(theMap.size == 0) {
        Vector(("",""),("",""),("",""))
      } else if(theMap.size == 1) {
        Vector(theMap.head,("",""),("",""))
      } else if(theMap.size == 2) {
        Vector(theMap(0), theMap(1), ("",""))
      } else theMap
     } finally {
      scores.close()
    }
  }
  
  
  /*
   * Creates the main window of the game, that holds all the different components in it.
   * Sets the dimensions, title of the main frame and centers it on the player's screen.
   */
  val mainWindow = new MainFrame
  mainWindow.title = "Tetris"
  mainWindow.contents = allThings
  mainWindow.resizable = false
  mainWindow.size = (new Dimension(500,670))
  mainWindow.centerOnScreen()
  
  /*
   * The timer performs automatically actions after a set 'timerSpeed' amount of milliseconds.
   * Makes the falling piece drop automatically and refreshes the state of the game.
   */
  val timer = new Timer(timerSpeed, new AbstractAction() {
      def actionPerformed(e: java.awt.event.ActionEvent) { 
        gameGrid.fallingPiece.move(Direction.Down)
        mainWindow.repaint()
        piecePanel.updatePiece(gameGrid.nextPiece)
        piecePanel.repaint()
        getPointsChange()
        updateState()
//        println(gameGrid.testSet())
//        println("coordX: " + gameGrid.fallingPiece.coordX + " coordY :" + gameGrid.fallingPiece.coordY )
//        println(gameGrid.getAccording2())
      }
    })
    timer.start

  def top = mainWindow
}
