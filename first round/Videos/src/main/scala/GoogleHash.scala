import java.nio.file.Paths

import scala.annotation.tailrec
import scala.io.Source

object GoogleHash {

  case class Video(index: Int, size: Int)


  case class CacheConnection(index: Int, latency: Int)

  case class Endpoint(index: Int, latency: Int, cacheConnections: Map[Int, CacheConnection])

  case class RequestDescription(videoIndex: Int, endpointIndex: Int, count: Int)

  case class Task(videos: List[Video], endpoints: List[Endpoint], requestDescriptions: List[RequestDescription], cacheSize: Int)

  def readTask(filename: String): Task = {
    val tmp = Source.fromFile(filename).getLines().toSeq
    val videos :: endpoints :: requests :: caches :: cacheSize :: Nil = tmp.head.split(' ').map(_.toInt).toList
    val videosList = tmp.tail.head.split(' ').zipWithIndex.map { case (size, index) => Video(index, size.toInt) }

    @tailrec
    def readEndpoints(rest: List[String], endpointsAcc: List[Endpoint]): List[Endpoint] = {
      val latency :: caches :: Nil = rest.head.split(' ').toList.map(_.toInt)
      val cacheConnections = rest.tail.take(caches).map(_.split(' ').toList).map { case toCache :: cacheLatency :: Nil => CacheConnection(toCache.toInt, cacheLatency.toInt) }
      val list = Endpoint(endpointsAcc.length, latency, cacheConnections.map(cc => (cc.index, cc)).toMap) :: endpointsAcc
      if (list.length < endpoints) readEndpoints(rest.drop(caches + 1), list)
      else list
    }

    Task(videosList.toList, readEndpoints(tmp.drop(2).toList, List.empty), List.empty, cacheSize)
  }


  def main(args: Array[String]): Unit = {
    //        val filename = "logo"
    readTask("example.txt")

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
  }
}