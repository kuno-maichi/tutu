package tutu.parser
import org.javafp.parsecj.Combinators.attempt
import org.javafp.parsecj.Text._

import java.util.Optional

object DocumentParser {
  lazy val document: TutuParser[String] = for {
    _ <- wspaces
  } yield ""


  lazy val tag: TutuParser[(Option[String], String)] =  for {
    _ <- string("[")
    prefix <- optional(for {
      p: String <- regex("[0-9a-zA-Z_]+")
      _ <- string(":")
    } yield p)
    name <- regex("[0-9a-zA-Z_]+")
    _ <- string("]")
  } yield (prefix, name)

  lazy val LBrace: TutuParser[String] = string("{")
  lazy val RBrace: TutuParser[String] = string("}")

  lazy val element: TutuParser[Ast.Element] = for {
    (prefix, name) <- tag
    _ <- LBrace
    _ <- RBrace
  } yield Ast.Element(prefix, name, "")
}
