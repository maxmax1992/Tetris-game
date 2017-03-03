package kierros5.model
import java.awt.Color
import kierros5.model.Direction._
import sun.audio._

/**
 * @authors Maxim Afteniy 539762, Sami Kalkas 77241H
 */

class Piece(var coordY: Int, var coordX: Int, var shape: Shape, val grid: GameGrid) {
  
  var newShape = this.shape
  
  /*
   * Tries to add a new shape to the game and returns a 
   * Boolean value of the success of the operation.
   */
  def addtoGameGrid(shape: Shape): Boolean = this.grid.setShape(coordY, coordX, newShape)
  
  
  /*
   * Returns a new shape that has been rotated 90 degrees to the right
   */
  def getSpinned() = {
    var theShape = this.newShape.getShape().transpose.map(_.reverse)
    val color = this.shape.getColor()
    this.newShape = new Shape() {
      override def getColor() : Color = color
      override def getShape() : List[List[Boolean]] = theShape
    }
    this.newShape
  }
  
  /*
   * Moves the falling piece to a Direction.Value direction and returns a Boolean
   * value of the success of the operation.
   */
  def move(direction: Direction.Value) : Boolean = direction match {
   
    case Direction.Up => {
      if(this.shape != O) {
        if(this.grid.removeShape(coordY, coordX, this.shape)) {
          if(this.addtoGameGrid(this.getSpinned())) {
            grid.fallingPiece = new Piece(coordY, coordX, this.newShape, this.grid)
            true
          } 
          else {
            for(i <- 1 to 3) {
              this.getSpinned()
            }
            this.grid.setShape(coordY, coordX, this.shape)
            false
          }
        } else false
      } 
      else false
    }
    
    case Direction.Left => {
      if (this.grid.removeShape(coordY, coordX, this.shape)) {
        if (this.grid.setShape(coordY + Direction.getYChange(Left), coordX + Direction.getXChange(Left), this.newShape))  {
          this.grid.fallingPiece = new Piece(coordY + Direction.getYChange(Left), coordX + Direction.getXChange(Left), this.newShape, this.grid)
          true
        }  
        else {
         this.grid.setShape(coordY, coordX, this.shape)
         false
        }
      }
      else false
    }
    
    case Direction.Right => {
      if (this.grid.removeShape(coordY, coordX, this.shape)) {
        if (this.grid.setShape(coordY + Direction.getYChange(Right), coordX + Direction.getXChange(Right), this.newShape))  {
          this.grid.fallingPiece = new Piece(coordY + Direction.getYChange(Right), coordX + Direction.getXChange(Right), this.newShape, this.grid)
          true
        }  
        else {
         this.grid.setShape(coordY, coordX, this.shape)
         false
         }
      }
      else false
    }
    
    case Direction.Down => {
      if (this.grid.removeShape(coordY, coordX, this.shape)) {
        if (this.grid.setShape(coordY + Direction.getYChange(Down), coordX + Direction.getXChange(Down), this.newShape))  {
          this.grid.fallingPiece = new Piece(coordY + Direction.getYChange(Down), coordX + Direction.getXChange(Down), this.newShape, this.grid)
          this.coordY = this.coordY+ 1 
          true
        }  
        else {
         this.grid.setShape(coordY, coordX, this.shape)
         this.grid.dropNextPiece()
         false
       }
      } else false
    }
  }

  // Drops the falling piece as far down as possible.
  def drop() : Boolean = {
    var stepper = 0
    while(this.move(Down)) {
      stepper += 1
    }
    stepper != 0
  }
  
  
}