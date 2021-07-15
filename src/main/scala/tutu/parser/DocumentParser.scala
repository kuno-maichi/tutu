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
      p: String <- regex("[^:]+")
      _ <- string(":")
    } yield p)
    name <- regex("[^]]+")
    _ <- string("]")
  } yield (prefix, name)

  lazy val LBrace: TutuParser[String] = string("{")
  lazy val RBrace: TutuParser[String] = string("}")

  lazy val line: TutuParser[String] = regex("[^\r\n]*[\r\n]+")

  lazy val element: TutuParser[Ast.Element] = for {
    _ <- wspaces
    (prefix, name) <- tag
    _ <- LBrace ~ wspaces
    body <- line.many1()
    _ <- RBrace
  } yield Ast.Element(prefix, name, body.foldl((a: String, b: String) => a + b.stripTrailing(), ""))
}
