import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

import scala.annotation.tailrec
import scala.io.Source

object GoogleHash {

  case class Video(index: Int, size: Int)

  case class CacheConnection(index: Int, latency: Int)

  case class Endpoint(index: Int, latency: Int, cacheConnections: Map[Int, CacheConnection])

  case class RequestDescription(videoIndex: Int, endpointIndex: Int, count: Int)

  case class Task(videos: List[Video],
                  endpoints: List[Endpoint],
                  requestDescriptions: List[RequestDescription],
                  cacheSize: Int)

  case class CacheServer(index: Int, videos: List[Int] = List.empty)

  case class Result(caches: List[CacheServer])

  def readTask(filename: String): Task = {
    val tmp = Source.fromFile(filename).getLines().toSeq
    val videos :: endpoints :: requests :: caches :: cacheSize :: Nil = tmp.head.split(' ').map(_.toInt).toList
    val videosList = tmp.tail.head.split(' ').zipWithIndex.map { case (size, index) => Video(index, size.toInt) }

    @tailrec
    def readEndpoints(rest: List[String], endpointsAcc: List[Endpoint]): List[Endpoint] = {
      val latency :: caches :: Nil = rest.head.split(' ').toList.map(_.toInt)
      val cacheConnections = rest.tail.take(caches).map(_.split(' ').toList).map {
        case toCache :: cacheLatency :: Nil => CacheConnection(toCache.toInt, cacheLatency.toInt)
        case _ => throw new Exception("Tro-lo-lo")
      }
      val list = Endpoint(endpointsAcc.length, latency, cacheConnections.map(cc => (cc.index, cc)).toMap) :: endpointsAcc
      if (list.length < endpoints) readEndpoints(rest.drop(caches + 1), list)
      else list
    }

    val endpointsList = readEndpoints(tmp.drop(2).toList, List.empty)
    val rdList = tmp.drop(2 + endpointsList.size + endpointsList.map(_.cacheConnections.size).sum)
      .map(reqStr => {
        val videoIndex :: endpointIndex :: count :: Nil = reqStr.split(' ').map(_.toInt).toList
        RequestDescription(videoIndex, endpointIndex, count)
      }).toList

    Task(videosList.toList, endpointsList, rdList, cacheSize)
  }


  def saveResult(result: Result): String = {
    s"""${result.caches.size}
       |${result.caches.sortBy(_.index).map(c => s"${c.index} ${c.videos.mkString(" ")}").mkString("\n")}
       |""".stripMargin
  }

  def solveTask(task: Task): Result = {
    var caches = task.endpoints.flatMap(_.cacheConnections.keys).distinct.map(ind => CacheServer(ind))
    var cacheSizesMap = task.endpoints.flatMap(_.cacheConnections.keys).distinct.map(_ -> 0).toMap
    task.requestDescriptions.sortBy(_.count).reverse.foreach { case RequestDescription(videoIndex, endpointIndex, requestsCount) =>
      val Some(Video(_, videoSize)) = task.videos.find(_.index == videoIndex)
      val Some(Endpoint(_, endpintLatency, cacheConnections)) = task.endpoints.find(_.index == endpointIndex)
      if (cacheConnections.nonEmpty) {
        val nonFullConnectedCaches = cacheSizesMap.filter {
          case (cacheIndex, used) =>
            cacheConnections.keySet.contains(cacheIndex) &&
              used + videoSize <= task.cacheSize &&
              !caches.exists(_.videos.contains(videoIndex))
        }
//        println(nonFullConnectedCaches)
        nonFullConnectedCaches.foreach { case (index, size) =>
          caches = caches.map {
            case CacheServer(currInd, videos) if currInd == index => CacheServer(currInd, videoIndex :: videos)
            case other => other
          }
          cacheSizesMap = cacheSizesMap.updated(index, cacheSizesMap(index) + videoSize)
        }
      }
    }
    Result(caches)
  }


  def main(args: Array[String]): Unit = {
    val filenames =
      "me_at_the_zoo.in" ::
        "example.in" ::
        "trending_today.in" ::
        "kittens.in" ::
        Nil
    filenames.foreach { filename =>
      val res = saveResult(solveTask(readTask(filename)))
      Files.write(Paths.get(filename.split('.').head + ".out"), res.getBytes(StandardCharsets.UTF_8))
    }
  }
}