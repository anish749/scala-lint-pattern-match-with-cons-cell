package fix

object PatternMatchWithConsCell {

  def main(args: Array[String]): Unit = {
    {
      val i: Iterable[Int] = Array(1, 2, 3).toIterable

      val hi = i match {
        case head :: tail => head
        case _ => println("Fail")
      }

      println(s"hi = ${hi}")
    }

    {
      val s: Seq[Int] = Array(1, 2, 3).toSeq

      val hs = s match {
        case head :: tail => head
        case _ => println("Fail")
      }

      println(s"hs = ${hs}")
    }

    {
      val t: Traversable[Int] = Array(1, 2, 3).toSeq

      val ht = t match {
        case head :: tail => head
        case _ => println("Fail")
      }

      println(s"ht = ${ht}")
    }

    {
      val t: TraversableOnce[Int] = Array(1, 2, 3).toSeq

      val ht = t match {
        case head :: tail => head
        case _ => println("Fail")
      }

      println(s"ht = ${ht}")
    }

    {
      val t: Seq[Int] = Array(1, 2, 3).toList

      val ht = t match {
        case head :: tail =>
          println("Success")
          head
        case _ => println("Fail")
      }

      println(s"ht = ${ht}")
    }

    {
      val s: Array[Int] = Array(1, 2, 3)

      val hs = s.toList match {
        case head :: tail =>
          println("Success")
          head
        case _ => println("Fail")
      }

      println(s"hs = ${hs}")
    }

    {
      val s: Seq[Int] = Array(1, 2, 3).toSeq

      val hs = s match {
        case _ => println("NoConsCell")
          s
      }

      println(s"hs = ${hs}")
    }
  }

}
