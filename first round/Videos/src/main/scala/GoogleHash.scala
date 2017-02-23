import scala.io.Source

object GoogleHash {

  case class Video(index: Int, size: Int)


  case class CacheConnection(index: Int, latency: Int)

  case class Endpoint(index: Int, latency: Int, cacheConnections: Map[Int, CacheConnection])

  case class RequestDescription(videoIndex: Int, endpointIndex: Int, count: Int)

  case class Task(videos: Video, endpoints: Endpoint, requestDescriptions: RequestDescription, cacheSize: Int)
  
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