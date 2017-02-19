import scala.io.Source
import scala.language.postfixOps

object GoogleHash {

  def main(args: Array[String]): Unit = {
  }

  case class Pos(r: Int, c: Int) {
    def inside(r1: Int, c1: Int, r2: Int, c2: Int): Boolean = r >= r1 && r <= r2 && c >= c1 && c <= c2
  }

  case class Pizza(cells: Seq[Seq[Char]]) {
    val ingredients: Map[Char, Set[Pos]] = cells.indices
      .flatMap(row => cells(row).indices
        .map(col => (cells(row)(col), Pos(row, col))))
      .groupBy(_._1)
      .map({ case (key, value) => key -> value.map(_._2).toSet })


  }

  case class Task(pizza: Pizza, min: Int, max: Int, slices: Set[Slice] = Set.empty) {
    def slice(): Task = ???

    override def toString: String =
      s"""${slices.size}
         |${slices.map(s => s"${s.r1} ${s.c1} ${s.r2} ${s.c2}").mkString("\n")}
         |""".stripMargin
  }

  object Task {
    def apply(filename: String): Task = {
      val header :: rows = Source.fromFile(filename).getLines().toList
      val rc :: cc :: min :: max :: Nil = header.split(' ').map(_.toInt).toList
      val data = rows.map(_.toCharArray.toSeq)
      assert(rc == rows.length)
      assert(data.forall(_.length == cc))
      Task(Pizza(data), min, max)
    }
  }

  case class Slice(pizza: Pizza, r1: Int, c1: Int, r2: Int, c2: Int) {
    val ingredients: Map[Char, Set[Pos]] = pizza.ingredients.map { case (ing, positions) =>
      ing -> positions.filter(_.inside(r1, c1, r2, c2))
    }
  }

}