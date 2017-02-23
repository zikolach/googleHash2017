
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
    }

    "save result" in {
      saveResult(Result(CacheServer(0, 1 :: 2 :: Nil) :: Nil)) shouldBe
        """1
          |0 1 2
          |""".stripMargin
    }
  }
}