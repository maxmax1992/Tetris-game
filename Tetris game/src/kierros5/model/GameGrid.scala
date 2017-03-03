package kierros5.model
import scala.collection.mutable.Buffer
import scala.collection.mutable.ArrayBuffer

/**
 * @authors Maxim Afteniy 539762, Sami Kalkas 77241H
 */

class GameGrid(val height: Int, val width: Int) {
 

  // Boolean value to determin if the game has been lost.
  var isLost = false
  
  // New ArrayBuffer containing empty shapes at the beginning of the game.
  var array : ArrayBuffer[ArrayBuffer[Shape]] = ArrayBuffer.fill(height, width)(` `)
  
  // Creates the falling piece and adds it to the game.
  var fallingPiece: Piece = new Piece(2, this.width/2 , Shape.getRandom(), this)
  this.fallingPiece.addtoGameGrid(this.fallingPiece.shape)
  
  // Keeps count of the players points in the game.
  var playerPoints = 0
  
  // Sets a new piece to be the next one to be added to the game.
  var nextPiece: Piece = new Piece(2, this.width/2 , Shape.getRandom(), this)
  
  // How many rows hace been completed at once.
  var countOfRows = 0
  
  def getFallingPiece() : Piece = this.fallingPiece
  
  def getNextPiece() : Piece = this.nextPiece
  
  def getShape(y: Int, x: Int) : Shape = array(y)(x)
  

  def setNewGame() = {
    isLost = false
    array = ArrayBuffer.fill(height, width)(` `)
    fallingPiece = new Piece(2, this.width/2 , Shape.getRandom(), this)
    playerPoints = 0
    nextPiece = new Piece(2, this.width/2 , Shape.getRandom(), this)
    fallingPiece.addtoGameGrid(this.fallingPiece.shape)
  }
  
  /*
   * Sets the given 'shape' to a given set of coordinates in the gamegrid if possible.
   * Returns a Boolean value of the success of this operation.
   */
  def setShape(y: Int, x: Int, shape: Shape) : Boolean = {
    val shapeList = shape.getShape()
    val middlecoords = (shapeList.size / 2, shapeList.head.size / 2)
    val shapeBlocks = Buffer[(Int,Int)]()
    
    // Saves the coordinates of the true values of the 5x5 List.
    for(i <- 0 until shapeList.size; j <- 0 until shapeList.head.size) {
      if(shapeList(i)(j)) shapeBlocks += ((i, j))
    }
    // Helping method which returns according GameGrid coordinates
    // from given shape boolean list coordinates from shapeBlocks Buffer.
    def getAccording(coords: (Int,Int)): (Int,Int)= {
      (y + coords._1 - middlecoords._1, x + coords._2 - middlecoords._2)
    }
    val shapeBlocksModified = shapeBlocks.map(getAccording(_))
    var canbeSet = shapeBlocksModified.forall(x => x._1 >= 0 && 
                                              x._2 >= 0 && 
                                              x._2 < array.head.size &&
                                              x._1 < array.size && 
                                              array(x._1)(x._2).getShape().size == 1)
    if(canbeSet) shapeBlocksModified.foreach(x => array(x._1)(x._2) = shape)
    canbeSet
    
  }
  
  
  
 
  def testSet(): Buffer[(Int,Int)] = {
    
    var returnBuffer = Buffer[(Int,Int)]()
    
    def getShapeCoords(coordy:Int,coordx: Int, shape: Shape):Buffer[(Int,Int)] = {
    var theBuffer = Buffer[(Int,Int)]()
   
    var theList = shape.getShape()
    for(y <- 0 until theList.size; x <- 0 until theList.head.size) {
      if(theList(y)(x) ) {
        theBuffer += ((y,x))
      }
    }
      theBuffer = theBuffer.map(x => (coordy+ x._1 - 2 , coordx + x._2 -2) )
        
      theBuffer
    }
    
   var isTrue = true
   var stePper = 1
   
   while(isTrue) {
     if(isTrue) {
       this.removeShape(this.fallingPiece.coordY, this.fallingPiece.coordX, this.fallingPiece.newShape)
        
       if(this.setShape(this.fallingPiece.coordY + stePper, this.fallingPiece.coordX, this.fallingPiece.newShape)) {
         this.removeShape(this.fallingPiece.coordY + stePper, this.fallingPiece.coordX, this.fallingPiece.newShape)
         returnBuffer = getShapeCoords(this.fallingPiece.coordY + stePper, this.fallingPiece.coordX, this.fallingPiece.newShape)
         this.setShape(this.fallingPiece.coordY, this.fallingPiece.coordX, this.fallingPiece.newShape)
         stePper += 1
       } else {
         this.setShape(this.fallingPiece.coordY, this.fallingPiece.coordX, this.fallingPiece.newShape)
         isTrue = false
       }
     }
   }

    
    
    
     returnBuffer

  }
  
  /*
   * Removes a given 'shape' from the given coordintaes, if it contains a 
   * shape other than ` `. Returns a Boolean value of the success of the operation.
   */
  def removeShape(y: Int, x: Int, shape: Shape) : Boolean = {
    val shapeList = shape.getShape()
    val middlecoords = (shapeList.size / 2, shapeList.head.size / 2)
    val shapeBlocks = Buffer[(Int,Int)]()
    for(i <- 0 until shapeList.size; j <- 0 until shapeList.head.size) {
      if(shapeList(i)(j)) shapeBlocks += ((i, j))
    }
   
    // helping method which returns the according GameGrid coordinates
    // from a given shape of Boolean list coordinates.
    def getAccording(coords: (Int,Int)): (Int,Int)= {
      (y + coords._1 - middlecoords._1, x + coords._2 - middlecoords._2)
    }
    val shapeBlocksModified = shapeBlocks.map(getAccording(_))
    var canbeSet = shapeBlocksModified.forall(x => x._1 >= 0 && 
                                                   x._2 >= 0 && 
                                                   x._2 < array.head.size &&
                                                   x._1 < array.size && 
                                                   array(x._1)(x._2).getShape().size == 5)
    
    if(canbeSet) shapeBlocksModified.foreach(x => array(x._1)(x._2) = ` `)
    canbeSet
  }
  
  
  /*
   * Removes a full line from the game. A full line is a line that does
   * not contain any empty shape values (` `).
   * If lines are removed, adds points to the score of the player.
   */
  def removeLine(y: Int): Unit = {
    val emptyArray: ArrayBuffer[Shape] = ArrayBuffer.fill(this.width)(` `)
    if(this.array(y).forall { x => x != ` `}) {
      this.array -= array(y)
      this.array.insert(0, emptyArray)
      countOfRows += 1
    }
  }
 
  
  /*
   * Calculates the amount of points to be added to the score.
   */
  def pointCounter() = {
    if (countOfRows == 4) playerPoints += 600
    else {
      playerPoints += countOfRows * 100
    }
    countOfRows = 0
  }
    
  /*
   * Drops the next piece into the game after clearing all full rows.
   * If the next piece can be added succesfully, the falling and next pieces are
   * updated and the game continues. Otherwise the game ends,
   * since there was no space to add the next piece to the game.
   */
  def dropNextPiece() : Unit = {
    for(i <- 0 until this.array.size) {
     this.removeLine(i)
    }
    this.pointCounter()
    if(this.nextPiece.addtoGameGrid(nextPiece.shape)) {
     this.fallingPiece = this.nextPiece
     this.fallingPiece.addtoGameGrid(this.fallingPiece.shape)
     this.nextPiece = new Piece(2, this.width/2 , Shape.getRandom(), this)
    } 
    else this.isLost = true
  }

}