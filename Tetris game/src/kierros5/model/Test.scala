package kierros5.model
import kierros5.model.Direction._

/**
 * @authors Maxim Afteniy 539762, Sami Kalkas 77241H
 */

object Test extends App {
 
  var gameGrid = new GameGrid(20, 10)
  
  // Removes the falling piece for testing purposes
  this.gameGrid.removeShape(2, gameGrid.width/2, gameGrid.fallingPiece.shape)
  
  // Tries to use the setShape method to all of the gameGrid coordinates, only succeeding at some of them.
  gameGrid.setShape(0,0 , testShape)
  for(y <- 0 until this.gameGrid.height; x <- 0 until this.gameGrid.width) {
    this.gameGrid.setShape(y, x, testShape)
  }
  println(gameGrid)
  
  
}
  