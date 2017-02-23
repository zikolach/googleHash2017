import scala.io.Source

object GoogleHash {

  case class Video(index: Int, size: Int)

  case class CacheConnection(index: Int, latency: Int)

  case class Endpoint(index: Int, latency: Int, cacheConnections: Map[Int, CacheConnection])

  case class RequestDescription(videoIndex: Int, endpointIndex: Int, count: Int)

  case class Task(videos: Video, endpoints: Endpoint, requestDescriptions: RequestDescription, cacheSize: Int)

  def readTask(filename: String): Task = {
    val tmp = Source.fromFile(filename).getLines().toSeq
    val videos :: endpoints :: requests :: caches :: cacheSize :: Nil = tmp.head.split(' ').map(_.toInt).toList
    val videosList = tmp.tail.head.split(' ').zipWithIndex.map { case (size, index) => Video(index, size.toInt) }

//    val endpointsList = tmp.drop(2).foldLeft(List.empty[Endpoint])
//    Task(videosList, )
    ???
  }


  def main(args: Array[String]): Unit = {
    //        val filename = "logo"
    readTask("example.txt")

  }
}