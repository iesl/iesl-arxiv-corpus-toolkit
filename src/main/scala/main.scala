


object Main extends App {


  import scala.collection.mutable

  // val catToBuffer = io.Source.fromFile("categories.txt").getLines.map{ line =>
  //   val normalCat = line.replaceAll("\\W", " ")
  //   ( normalCat, mutable.ArrayBuffer[String]() )
  // }

  val entriesByDiscipline = mutable.HashMap[String, mutable.ArrayBuffer[String]]()

  // entriesByDiscipline ++= catToBuffer

  io.Source.fromFile("pdf-cats.txt").getLines.foreach{ arxivCat =>
    if (arxivCat.contains("tarball") && arxivCat.contains("[") && arxivCat.contains("]")) {
      // println(s"trying: ${arxivCat}")
      try {
        val Array(pre, post)  = arxivCat.split("\\[")
        // println(s"pre/post: ${pre} / $post")
        val Array(catstr, x) = post.split("\\]")
        // println(s"cat/_: ${cat} / $x")
        val cat = catstr.toLowerCase().replaceAll("\\W", " ")
        // println(s"cat: ${cat}")
        val filename = arxivCat.split(":")(0)
        val buf = entriesByDiscipline.getOrElseUpdate(cat, mutable.ArrayBuffer[String]())
        buf.append(filename)

      } catch {
        case t: Throwable =>
      }
    }
  }


  println(s"Total disciplines: ${entriesByDiscipline.keySet.size}")

  val filenameBuffer = mutable.ArrayBuffer[String]()


  entriesByDiscipline.foreach { case (cat, filenames) =>
    val entry = cat + "\n" + filenames.mkString("\n  ", "\n  ", "\n")
    val topNames = filenames.take(51)

    // val topNameStr = topNames.mkString("\n  ", "\n  ", "\n")
    val topNameStr = topNames.mkString("\n", "\n", "")

    filenameBuffer.appendAll(topNames)
    // println(s"${cat} ${topNameStr}")
    // println(topNameStr)
  }

  println(s"Total files = ${filenameBuffer.length}")
  println(filenameBuffer.mkString("\n"))


}
