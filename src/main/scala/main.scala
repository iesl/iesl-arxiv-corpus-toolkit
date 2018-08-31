
import scala.collection.mutable
import ammonite.{ops => fs}  , fs._

object Main extends App {
  import Helpers._

  val argMap = argsToMap(args)
  val categoryInputFile = argMap.get("category-file").flatMap(_.headOption)
    .getOrElse(sys.error("missing --category-file option"))

  val pdfsOutputFile = argMap.get("pdf-output").flatMap(_.headOption)
    .getOrElse(sys.error(""))

  val statsFile = argMap.get("stats-output").flatMap(_.headOption)
    .getOrElse(sys.error(""))

  val  pdfsByDiscipline = mutable.HashMap[String, mutable.ArrayBuffer[String]]()

  io.Source.fromFile(categoryInputFile).getLines.foreach{ inputLine =>
    if (inputLine.contains(".pdf:") && inputLine.contains("[") && inputLine.contains("]")) {
      try {
        val Array(pre, post)  = inputLine.split("\\[")
        val Array(catstr, x) = post.split("\\]")
        val disciplineCode = catstr.toLowerCase().replaceAll("\\W", " ")
        val filename = inputLine.split(":")(0)
        val buf = pdfsByDiscipline.getOrElseUpdate(disciplineCode, mutable.ArrayBuffer[String]())
        buf.append(filename)

      } catch {
        case t: Throwable =>
      }
    }
  }

  println(s"Total disciplines: ${pdfsByDiscipline.keySet.size}")

  val filenameBuffer = mutable.ArrayBuffer[String]()

  val allEntries = pdfsByDiscipline.map { case (disciplineCode, filenames) =>
    val entry = s"${filenames.length} PDFs for discipline: ${disciplineCode}" + filenames.mkString("\n  ", "\n  ", "\n")
    val topNames = filenames.take(51)
    filenameBuffer.appendAll(topNames)
    entry
  }

  println(s"Total files = ${filenameBuffer.length}")
  val includePdfs = filenameBuffer.mkString("\n")

  val pdfOutput = fs.pwd / pdfsOutputFile
  val statsOutput = fs.pwd / statsFile
  if (fs.exists(pdfOutput)) fs.rm(pdfOutput)
  if (fs.exists(statsOutput)) fs.rm(statsOutput)

  fs.write(pdfOutput, includePdfs)
  fs.write(statsOutput, allEntries.mkString("\n\n"))

}


object Helpers {

  def argsToMap(args: Array[String]): Map[String, List[String]] = {
    import scala.collection.mutable.{ListMap => LMap}
    val argmap = LMap[String, List[String]]()
    args.foldLeft(argmap)({(m, k:String) => {
      val ss:Seq[Char] = k
      ss match {
        case Seq('-', '-', opt @ _*) => m.put(opt.toString, List[String]())
        case Seq('-', opt @ _*) => m.put(opt.toString, List[String]())
        case opt @ _ => m.put(m.head._1, m.head._2 ++ List[String](opt.toString))
      }
      m
    }})
    Map[String, List[String]](argmap.toList.reverse: _*)
  }


}
