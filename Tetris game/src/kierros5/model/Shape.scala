package kierros5.model
import java.awt.Color
import scala.collection.immutable.List
import scala.util.Random


/**
 * The only task of Shape companion object is to provide a method for selecting
 * the next piece to drop to the game. 
 * 
 * @author ankkukku & Maxim Afteniy 539762 & Sami Kalkas 77241H
 */

object Shape {
  def getRandom() : Shape = {
    val options = Array(I, J, L, O, S, Z, T)
    options(Random.nextInt(options.length))
  }
}

/**
 * An abstract class for representing the general properties of a shape for the
 * piece.
 * 
 * The subclasses can override the methods to fit their properties. The default
 * implementation represents properties of an empty shape. 
 * 
 * @author ankkukku
 */
abstract class Shape {
  
  /**
   * @return The color that represents the shape in GamePanel.
   */
  def getColor() : Color = Color.DARK_GRAY
  def coloR(): Color = Color.ORANGE
  /**
   * @return The shape as two-dimensional list containing true in places where
   *         the shape is present and false where there is no shape. 
   */
  def getShape() : List[List[Boolean]] = List(List(false))
  
  override def toString = " # "

}

/**
 * Represents the shape I or a line, that is one block wide and four blocks
 * tall.
 * 
 * _ _ * _ _
 * _ _ * _ _
 * _ _ * _ _
 * _ _ * _ _
 * _ _ _ _ _
 * 
 * @author ankkukku
 */
object I extends Shape {
  override def getColor() : Color = Color.ORANGE

  override def getShape() : List[List[Boolean]] = {
    List(List(false, false, true, false, false),
         List(false, false, true, false, false),
         List(false, false, true, false, false),
         List(false, false, true, false, false),
         List(false, false, false, false, false))
  }
  
  override def toString = " I "

}

/**
 * Represents the shape J.
 * 
 * _ _ _ _ _
 * _ _ * _ _
 * _ _ * _ _
 * _ * * _ _
 * _ _ _ _ _
 * 
 * @author ankkukku
 */
object J extends Shape {
  override def getColor() : Color = Color.BLUE

  override def getShape() : List[List[Boolean]] = {
    List(List(false, false, false, false, false),
         List(false, false, true, false, false),
         List(false, false, true, false, false),
         List(false, true, true, false, false),
         List(false, false, false, false, false))
  }
  
  override def toString = " J " 
}

/**
 * Represents the shape L.
 * 
 * _ _ _ _ _
 * _ _ * _ _
 * _ _ * _ _
 * _ _ * * _
 * _ _ _ _ _
 * 
 * @author ankkukku
 */
object L extends Shape {
  override def getColor() : Color = Color.MAGENTA

  override def getShape() : List[List[Boolean]] = {
    List(List(false, false, false, false, false),
         List(false, false, true, false, false),
         List(false, false, true, false, false),
         List(false, false, true, true, false),
         List(false, false, false, false, false))
  }
  
  override def toString = " L "

}

/**
 * Represents the shape O or a square, that is two block wide and tall.
 * 
 * The block is the only piece that is not affected by turn.
 * 
 * _ _ _ _ _
 * _ * * _ _
 * _ * * _ _
 * _ _ _ _ _
 * _ _ _ _ _
 * 
 * @author ankkukku
 */
object O extends Shape {
  override def getColor() : Color = Color.RED

  override def getShape() : List[List[Boolean]] = {
    List(List(false, false, false, false, false),
         List(false, true, true, false, false),
         List(false, true, true, false, false),
         List(false, false, false, false, false),
         List(false, false, false, false, false))
  }
  
  override def toString = " O "

}

/**
 * Represents the shape S.
 * 
 * _ _ _ _ _
 * _ _ * * _
 * _ * * _ _
 * _ _ _ _ _
 * _ _ _ _ _
 * 
 * @author ankkukku
 */
object S extends Shape {
  override def getColor() : Color = Color.CYAN

  override def getShape() : List[List[Boolean]] = {
    List(List(false, false, false, false, false),
         List(false, false, true, true, false),
         List(false, true, true, false, false),
         List(false, false, false, false, false),
         List(false, false, false, false, false))
  }
  
  override def toString = " S "

}

/**
 * Represents the shape Z.
 * 
 * _ _ _ _ _
 * _ * * _ _
 * _ _ * * _
 * _ _ _ _ _
 * _ _ _ _ _
 * 
 * @author ankkukku
 */
object Z extends Shape {
  override def getColor() : Color = Color.GREEN

  override def getShape() : List[List[Boolean]] = {
    List(List(false, false, false, false, false),
         List(false, true, true, false, false),
         List(false, false, true, true, false),
         List(false, false, false, false, false),
         List(false, false, false, false, false))
  }
  
  override def toString = " Z "

}

/**
 * Represents the shape T.
 * 
 * _ _ _ _ _
 * _ _ * _ _
 * _ * * * _
 * _ _ _ _ _
 * _ _ _ _ _
 * 
 * @author ankkukku
 */
object T extends Shape {
  override def getColor() : Color = Color.YELLOW

  override def getShape() : List[List[Boolean]] = {
    List(List(false, false, false, false, false),
         List(false, false, true, false, false),
         List(false, true, true, true, false),
         List(false, false, false, false, false),
         List(false, false, false, false, false))
  }
  
  override def toString = " T "

}

/**
 * Represents an empty shape.
 * 
 * @author ankkukku
 */
object ` ` extends Shape {
  override def toString = {
    " _ "
  }
}
object testShape extends Shape {
    override def toString = " █ "
    override def getShape() : List[List[Boolean]] = {
    List(List(true, true, true, true, true),
         List(true, false, false, false, true),
         List(true, false, false, false, true),
         List(true, false, false, false, true),
         List(true, true, true, true, true))
    }
  }
