import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

import scala.annotation.tailrec
import scala.io.Source
import scala.language.postfixOps

object GoogleHash {

  val debug = true

  def log(str: Any): Unit = if (debug) println(str)

  def main(args: Array[String]): Unit = {
    val res = Task(args.head).slice().toString
    Files.write(Paths.get(args.head.split('.').head + ".out"), res.getBytes(StandardCharsets.UTF_8))
  }

  case class Pos(r: Int, c: Int) {
    def inside(r1: Int, c1: Int, r2: Int, c2: Int): Boolean = r >= r1 && r <= r2 && c >= c1 && c <= c2

    def dist(p: Pos): Double = Math.sqrt(Math.pow(p.r - r, 2) + Math.pow(p.c - c, 2))
  }

  case class Pizza(cells: Seq[Seq[Char]]) {
    val ingredients: Map[Char, Set[Pos]] = {
      val typeToPos = for {
        r <- cells.indices.toSet[Int]
        c <- cells(r).indices.toSet[Int]
      } yield Pos(r, c)
      typeToPos.groupBy(p => cells(p.r)(p.c))
    }
  }

  case class Task(pizza: Pizza, min: Int, max: Int, slices: Set[Slice] = Set.empty) {
    def slice(): Task = {
      val minSlices = Math.ceil((pizza.cells.size * pizza.cells.head.size).toDouble / max).toInt
      log(s"min slices: $minSlices")
//      findNext(minSlices)
            findNext(2)
    }

    def cut(taken: Seq[Pos]): Slice = {
      val (rr, cc) = taken.map(p => p.r -> p.c).unzip
      //      log(s"xx: $rr, yy: $cc")
      Slice(pizza, rr.min, cc.min, rr.max, cc.max)
    }

    @tailrec
    final def findNext(left: Int): Task = {
      val rest = pizza.ingredients.map { case (i, pos) => i -> pos.filter(p => !slices.exists(_.include(p))) }
      val used = pizza.ingredients.values.toSet.flatten.filter(p => slices.exists(_.include(p)))
      println(s"left: $left, used: ${used.size}")
      val minIng = rest.minBy(_._2.size)._1
      log(s"min ing - $minIng")
      val startPos = rest(minIng).head
      log(s"start from $startPos")
      // TODO: filter only close enough
      val sortedIngs = rest(minIng).tail.toSeq
        .filter(p => startPos.dist(p) < max)
        .filter(p => {
          val probSlice = cut(Seq(startPos, p))
          val probPS = probSlice.ingredients.values.flatten.toSet
          probSlice.size <= max && probPS.intersect(used).isEmpty
        })
        .sortBy(startPos.dist)
      log(s"sortedIngs.size ${sortedIngs.size}")
      val taken = startPos :: sortedIngs.take(min - 1).toList
      log(taken)
      val preSlice = cut(taken)

      val otherIng = rest.filterNot(_._1 == minIng).head._1
      log(s"other ing - $otherIng")

      val slice = if (preSlice.ingredients.forall(_._2.size >= min)) {
        log("preslice taken")
        preSlice
      } else {
        val otherSorted = rest(otherIng).toSeq.sortBy(startPos.dist)
        log(s"other sorted $otherSorted")
        val otherTaken = otherSorted.take(min)
        cut(taken ++ otherTaken)
      }
      val task = this.copy(slices = this.slices + slice)
      log(s"result: $slice (${slice.size})")
      if (left == 1) task.copy(slices = task.slices.filter(_.ingredients.forall(_._2.size >= min)))
      else task.findNext(left - 1)
    }

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
    lazy val ingredients: Map[Char, Set[Pos]] = pizza.ingredients.map { case (ing, positions) =>
      ing -> positions.filter(include)
    }

    def include(pos: Pos): Boolean = pos.inside(r1, c1, r2, c2)

    def dist(pos: Pos): Double = {
      if (include(pos)) 0
      else {
        Math.sqrt(
          Math.pow(Math.max(pos.r - r2, r1 - pos.r), 2) +
            Math.pow(Math.max(pos.c - c2, c1 - pos.c), 2))
      }
    }

    lazy val size: Int = (r2 - r1 + 1) * (c2 - c1 + 1)

    override def toString: String = s"Slice($r1,$c1,$r2,$c2)"
  }

}