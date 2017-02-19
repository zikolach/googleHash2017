import org.scalatest.{Matchers, WordSpec}

class GoogleHashTest extends WordSpec with Matchers {

  import GoogleHash._

  private val examplePizza = Pizza(Seq(
    Seq('T', 'T', 'T', 'T', 'T'),
    Seq('T', 'M', 'M', 'M', 'T'),
    Seq('T', 'T', 'T', 'T', 'T')))


  "Hashcode" should {
    "save slices to file" in {
      val expected =
        """3
          |0 0 2 1
          |0 2 2 2
          |0 3 2 4
          |"""
          .stripMargin
      val result = Task(examplePizza, 0, 0, Set(
        Slice(examplePizza, 0, 0, 2, 1),
        Slice(examplePizza, 0, 2, 2, 2),
        Slice(examplePizza, 0, 3, 2, 4))).toString
      result should be(
        expected)
    }

    "read a task" in {
      val expected = Task(examplePizza, 1, 6)
      val task = Task("example.in")
      task should be(expected)
    }

    "slice example pizza" in {
      val result = Task("example.in").slice().toString
      val expected =
        """3 5 1 6
          |TTTTT
          |TMMMT
          |TTTTT
          |"""
          .stripMargin
      result should be(
        expected)
    }
  }

  "Pizza" should {
    "cut a slice" in {

    }
  }
}
