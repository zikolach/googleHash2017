import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

import scala.annotation.tailrec
import scala.io.Source

object GoogleHash {


  def main(args: Array[String]): Unit = {
    //        val filename = "logo"
    val filename = "me_at_the_zoo.in"
    val tmp = Source.fromFile(Paths.get(s"$filename").toFile).getLines().toList

    val data = tmp.head.split(' ')
    val videos = Integer.parseInt(data(0))
    val endpoints = Integer.parseInt(data(1))
    val requests = Integer.parseInt(data(2))
    val caches = Integer.parseInt(data(3))
    val csize = Integer.parseInt(data(4))

    println("Videos:  " + videos + " Endpoints: " + endpoints)
    println("Requests:  " + requests + " Caches: " + caches + " " + csize + " MB")



//    var i = 0
//    val commands = tmp.tail.zipWithIndex.flatMap {
//      case (line, index) => p1(index, line)
//    }
//    commands.foreach(println)
//
//    Files.write(Paths.get(s"$filename.out"), (commands.length.toString :: commands).mkString("\n").getBytes(StandardCharsets.UTF_8))
  }
}