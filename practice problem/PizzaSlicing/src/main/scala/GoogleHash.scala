import scala.io.Source

object GoogleHash {

  def main(args: Array[String]): Unit = {

  }

  case class Task(pizza: Seq[Seq[Char]], min: Int, max: Int)

  case class Slice(r1: Int, c1: Int, r2: Int, c2: Int) {
    override def toString = s"$r1 $c1 $r2 $c2"
  }

  def slicePizza(filename: String): String = {
    val Task(pizza, min, max) = readPizza(filename)

    val slices = ""

    slices
  }

  def readPizza(filename: String): Task = {
    val header :: rows = Source.fromFile(filename).getLines().toList
    val rc :: cc :: min :: max :: Nil = header.split(' ').map(_.toInt).toList
    val data = rows.map(_.toCharArray.toSeq)
    assert(rc == rows.length)
    assert(data.forall(_.length == cc))
    Task(data, min, max)
  }

  def saveSlices(slices: Seq[Slice]): String =
    s"""${slices.size}
       |${slices.mkString("\n")}
       |""".stripMargin
}