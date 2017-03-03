package kierros5.gui
import kierros5.model._
import kierros5._
import scala.swing._

/**
 * @authors Maxim Afteniy 539762, Sami Kalkas 77241H
 */


class PiecePanel(var piece: Piece) extends GridPanel(piece.grid.height, piece.grid.width) {
  
  
  val piecePanel = new GameGrid(5, 5)
  val pieceArray = piecePanel.array
  
  
  piecePanel.setShape(2, 2, this.piece.shape)
  
  
  /*
   * Updates the view of the PiecePanel to show the next piece to be 
   * dropped in the game.
   */
  def updatePiece(p: Piece) : Unit = {
    this.piece = p
    this.piecePanel.removeShape(2, 2, this.piecePanel.getShape(2, 2))
    this.piecePanel.setShape(2, 2, this.piece.shape)
    this.repaint()
  }
  
  
  /*
   * Sets the color of every PaintPanel coordinate.
   */
  override def paintComponent(g:Graphics2D) : Unit = {
    val arrayToEdit = piecePanel.array
    for(i <- 0 until arrayToEdit.size; j <- 0 until arrayToEdit.head.size) {
     g.setColor(arrayToEdit(i)(j).getColor())
     g.fillRect(j*15, i*15, 15, 15)
    }
  }
  

}