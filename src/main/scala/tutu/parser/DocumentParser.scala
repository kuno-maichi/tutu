package tutu.parser
import org.javafp.parsecj.Combinators._
import org.javafp.parsecj.Text._

import java.util.Optional

object DocumentParser {
  case class Element(prefix: Option[String], name: String, body: String)
  lazy val document: TutuParser[String] = for {
    _ <- wspaces
  } yield ""

  lazy val tag: TutuParser[(Option[String], String)] =  for {
    _ <- string("[")
    prefix: Option[String] <- optional(for {
      p <- regex("[0-9a-zA-Z_]*")
      _ <- string(":")
    } yield p)
    _ <- string(":")
    name <- regex("[0-9a-zA-Z]+")
    _ <- string("]")
  } yield (prefix, name)

  lazy val LBrace: TutuParser[String] = string("{")
  lazy val RBrace: TutuParser[String] = string("}")

  lazy val element: TutuParser[Element] = for {
    (prefix, name) <- tag
  } yield Element(prefix, name, "")
}
