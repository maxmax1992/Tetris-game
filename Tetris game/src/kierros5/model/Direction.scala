package kierros5.model

/**
 * @authors Maxim Afteniy 539762, Sami Kalkas 77241H
 */

object Direction extends Enumeration {
  
  // Creates four different types of Values.
  val Up, Down, Right, Left = Value
  
  /*
   * Returns an Int value for a given Direction-value.
   */
  def getXChange(x: Direction.Value) : Int = x match {
    case Up => 0
    case Down => 0
    case Left => -1
    case Right => 1
  }
  
  /*
   * Returns an Int value for a given Direction-value.
   */
  def getYChange(y: Direction.Value) : Int = y match {
    case Up => -1
    case Down => 1
    case Left => 0
    case Right => 0 
  }


}