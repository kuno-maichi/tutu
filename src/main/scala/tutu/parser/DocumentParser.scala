package tutu.parser
import org.javafp.parsecj.Combinators.attempt
import org.javafp.parsecj.Text._
import tutu.parser.Ast.{Prefix, SynopsisTag, UserTag}
import tutu.parser.DocumentParser.prefix

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

  lazy val userTag: TutuParser[UserTag] =  for {
    _ <- string("[")
    p <- optional(prefix)
    n <- regex("[^]]+")
    _ <- string("]")
  } yield UserTag(p, n)

  lazy val synopsisTag: TutuParser[SynopsisTag] =  for {
    _ <- string("[")
    p <- optional(prefix)
    _ <- regex("synopsis|あらすじ")
    _ <- string("]")
  } yield SynopsisTag(p)

  lazy val tag: TutuParser[Ast.Tag] = synopsisTag.map(t => t:Ast.Tag) | userTag.map(t => t: Ast.Tag)

  lazy val LBrace: TutuParser[String] = string("{")
  lazy val RBrace: TutuParser[String] = string("}")

  lazy val line: TutuParser[String] = regex("[^\r\n]*[\r\n]+")

  lazy val element: TutuParser[Ast.Element] = for {
    _ <- wspaces
    tag <- tag
    _ <- LBrace ~ wspaces
    body <- line.many1()
    _ <- RBrace
  } yield {
    tag match {
      case SynopsisTag(prefix) =>
        Ast.Synopsis(prefix, body.foldl((a: String, b: String) => a + b.trim(), ""))
      case UserTag(prefix, name) =>
        Ast.UserElement(prefix, name, body.foldl((a: String, b: String) => a + b.trim(), ""))
    }
  }
}
