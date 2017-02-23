
import org.scalatest.{Matchers, WordSpec}


class GoogleHashSpec extends WordSpec with Matchers {
  import GoogleHash._

  "GoogleHash" should {
    "read a task" in {
      val task = readTask("example.in")
      task.endpoints.length shouldBe 2
    }
  }
}