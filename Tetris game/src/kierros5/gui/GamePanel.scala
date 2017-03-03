package kierros5.gui
import kierros5.model._
import kierros5._
import scala.swing._
import java.awt.Graphics2D
import java.awt.Color

/**
 * @authors Maxim Afteniy 539762, Sami Kalkas 77241H
 */

class GamePanel(val gameGrid: GameGrid) extends GridPanel(gameGrid.height, gameGrid.width) {
  
  
  /*
   * Sets the color of every GameGrids coordinate.
   */
  override def paintComponent(g:Graphics2D) : Unit = {
    val shadowCoords = this.gameGrid.testSet()
    val arrayToEdit = gameGrid.array
    
    for(i <- 0 until arrayToEdit.size; j <- 0 until arrayToEdit.head.size) {
     g.setColor(Color.BLACK)
     g.fillRect(j*30, i*30, 30, 30)
     g.setColor(arrayToEdit(i)(j).getColor())
     g.fillRoundRect(j*30, i*30, 29, 29,2,2)
     
    
    }
    
    for(i <- shadowCoords) {
      if(arrayToEdit(i._1)(i._2).getColor() == Color.DARK_GRAY) {
      g.setColor(Color.GRAY)
      g.fillRect(30*i._2,30*i._1,30,30)
      }
    }
    
  }
  
}