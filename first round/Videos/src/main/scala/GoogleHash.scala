import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

import scala.annotation.tailrec
import scala.io.Source

object GoogleHash {


  def main(args: Array[String]): Unit = {
    //        val filename = "logo"
//    val filename = "me_at_the_zoo.in"
    val filename = "example.in"
    val tmp = Source.fromFile(Paths.get(s"$filename").toFile).getLines().toList

    val data = tmp.head.split(' ')
    val nr_vids = Integer.parseInt(data(0))
    val nr_ends = Integer.parseInt(data(1))
    val nr_reqs = Integer.parseInt(data(2))
    val nr_caches = Integer.parseInt(data(3))
    val csize = Integer.parseInt(data(4))

    println("Videos:  " + nr_vids + " Endpoints: " + nr_ends)
    println("Requests:  " + nr_reqs + " Caches: " + nr_caches + " " + csize + " MB")

    val next = tmp.tail

    val videos = next.head
    val vids: Array[Int] = videos.split(" ").filterNot(_.isEmpty).map(a => a.toInt)

    println( vids.mkString(" MB "))


//    var i = 0
//    val commands = tmp.tail.zipWithIndex.flatMap {
//      case (line, index) => p1(index, line)
//    }
//    commands.foreach(println)
//
//    Files.write(Paths.get(s"$filename.out"), (commands.length.toString :: commands).mkString("\n").getBytes(StandardCharsets.UTF_8))
  }
}