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
        """3
          |0 0 2 1
          |0 2 2 2
          |0 3 2 4
          |"""
          .stripMargin
      result shouldBe expected
    }
  }

  "Pizza" should {
    "cut a slice" in {

    }
  }

  "Slice" should {
    "calc dist to point" in {
      val slice = Slice(examplePizza, 1, 1, 3, 3)
      slice.dist(Pos(4,3)) - 1.0 should be < 0.01
      slice.dist(Pos(0,1)) - 1.0 should be <  0.01
      slice.dist(Pos(0,0)) - 1.41 should be < 0.01
      slice.dist(Pos(5,4)) - Math.sqrt(5) should be < 0.01
    }

    "calc size" in {
      val slice = Slice(examplePizza, 1, 1, 3, 3)
      slice.size should be (9)
    }
  }
}
