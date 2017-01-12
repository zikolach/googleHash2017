import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

import scala.annotation.tailrec
import scala.io.Source

object GoogleHash {

  def main(args: Array[String]): Unit = {
    //        val filename = "logo"
    val filename = "example"
    //    val filename = "learn_and_teach"
    val tmp = Source.fromFile(Paths.get(s"$filename.in").toFile).getLines().toList

    val data = tmp.head.split(' ')
    val rows = Integer.parseInt(data(0))
    val cols = Integer.parseInt(data(1))

    println("Rows " + rows + " Cols: " + cols)
//    var i = 0
//    val commands = tmp.tail.zipWithIndex.flatMap {
//      case (line, index) => p1(index, line)
//    }
//    commands.foreach(println)
//
//    Files.write(Paths.get(s"$filename.out"), (commands.length.toString :: commands).mkString("\n").getBytes(StandardCharsets.UTF_8))
  }
}