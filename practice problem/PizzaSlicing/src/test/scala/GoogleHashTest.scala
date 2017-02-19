import org.scalatest.{FlatSpec, Matchers}

class GoogleHashTest extends FlatSpec with Matchers {

  import GoogleHash._

  "Hashcode" should "save slices to file" in {
    val expected =
      """3
        |0 0 2 1
        |0 2 2 2
        |0 3 2 4
        |""".stripMargin
    val result = saveSlices(Seq(Slice(0, 0, 2, 1), Slice(0, 2, 2, 2), Slice(0, 3, 2, 4)))
    result should be(expected)
  }

  "Hashcode" should "read a task" in {
    val expected = Task(Seq(
      Seq('T', 'T', 'T', 'T', 'T'),
      Seq('T', 'M', 'M', 'M', 'T'),
      Seq('T', 'T', 'T', 'T', 'T')), 1, 6)
    val task = readPizza("example.in")
    task should be(expected)
  }

  "Hashcode" should "slice example pizza" in {
    val result = slicePizza("example.in")
    val expected =
      """3 5 1 6
        |TTTTT
        |TMMMT
        |TTTTT
        |""".stripMargin
    result should be(expected)
  }

}
