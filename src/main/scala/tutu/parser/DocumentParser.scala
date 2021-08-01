package tutu.parser
import org.javafp.parsecj.Combinators.attempt
import org.javafp.parsecj.Text._
import tutu.parser.Ast.Prefix

import java.util.Optional

object DocumentParser {
  private def prefixOf(name: String): Prefix = name match {
    case "narou" | "なろう" | "小説家になろう" => Prefix.Narou
    case "kakuyomu" | "カクヨム" => Prefix.Kakuyomu
    case _ => Prefix.UseDefinedPrefix(name)
  }
  lazy val document: TutuParser[String] = for {
    _ <- wspaces
  } yield ""

  lazy val prefix: TutuParser[Ast.Prefix] = for {
    name: String <- regex("[^:]+")
    _ <- string(":")
  } yield prefixOf(name)

  lazy val tag: TutuParser[(Option[Ast.Prefix], String)] =  for {
    _ <- string("[")
    p <- optional(prefix)
    n <- regex("[^]]+")
    _ <- string("]")
  } yield (p, n)

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
