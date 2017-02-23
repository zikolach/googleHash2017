
import org.scalatest.{Matchers, WordSpec}


class GoogleHashSpec extends WordSpec with Matchers {

  import GoogleHash._

  "GoogleHash" should {
    "read a task" in {
      val task = readTask("example.in")
      task.endpoints.length shouldBe 2
      task.requestDescriptions.length shouldBe 4
      task.cacheSize shouldBe 100
      task.videos.length shouldBe 5
      task.videos.map(_.size).sum shouldBe 320
      task.endpoints.map(_.cacheConnections.values.map(_.latency).sum).sum shouldBe 600
      task.requestDescriptions.map(_.count).sum shouldBe 4000
    }

    "save result" in {
      saveResult(Result(CacheServer(0, 1 :: 2 :: Nil) :: Nil)) shouldBe
        """1
          |0 1 2
          |""".stripMargin
      saveResult(Result(
        CacheServer(0, 2 :: Nil) ::
        CacheServer(1, 3 :: 1 :: Nil) ::
        CacheServer(2, 0 :: 1 :: Nil) ::
          Nil)) shouldBe
        """3
          |0 2
          |1 3 1
          |2 0 1
          |""".stripMargin
    }
  }
}