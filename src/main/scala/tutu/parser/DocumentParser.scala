package tutu.parser
import tutu.combinator._
import tutu.parser.Ast.{Prefix, SynopsisTag, UserTag}
import tutu.parser.DocumentParser.prefix

import java.util.Optional

object DocumentParser extends SCombinator {
  private def prefixOf(name: String): Prefix = name match {
    case "narou" | "なろう" | "小説家になろう" => Prefix.Narou
    case "kakuyomu" | "カクヨム" => Prefix.Kakuyomu
    case _ => Prefix.UseDefinedPrefix(name)
  }

  lazy val prefix: Parser[Ast.Prefix] = for {
    name <- (not(string(":")) ~> any).+.map(_.mkString)
    _ <- string(":")
  } yield prefixOf(name)

  lazy val userTag: Parser[UserTag] =  for {
    _ <- string("[")
    p <- prefix.?
    n <- (not(string("]")) ~> any).+.map(_.mkString)
    _ <- string("]")
  } yield UserTag(p, n)

  lazy val synopsisTag: Parser[SynopsisTag] =  for {
    _ <- string("[")
    p <- prefix.?
    _ <- string("synopsis") | string("あらすじ")
    _ <- string("]")
  } yield SynopsisTag(p)

  lazy val tag: Parser[Ast.Tag] = synopsisTag | userTag

  def except(elements: Char*): Parser[String] = {
    val joined = elements.toList.map(ch => string("" +ch)).reduceLeft((a, b) => a | b)
    not(joined) ~> any map {_.toString}
  }

  def all(elements: Char*): Parser[String] = {
    val joined = elements.toList.map(ch => string("" +ch)).reduceLeft((a, b) => a | b)
    joined
  }

  lazy val LBrace: Parser[String] = string("{")
  lazy val RBrace: Parser[String] = string("}")

  lazy val line: Parser[String] = except('\r', '\n').* ~ all('\r', '\n').+ ^^ { case x ~ y => x.mkString + y.mkString}

  lazy val element: Parser[Ast.Element] = for {
    _ <- DefaultSpaces
    tag <- tag
    _ <- LBrace ~ DefaultSpaces
    body <- line.+
    _ <- RBrace
  } yield {
    tag match {
      case SynopsisTag(prefix) =>
        Ast.Synopsis(prefix, body.foldLeft(""){(a: String, b: String) => a + b.trim()})
      case UserTag(prefix, name) =>
        Ast.UserElement(prefix, name, body.foldLeft(""){(a: String, b: String) => a + b.trim()})
    }
  }

  def parseElement(input: String): Ast.Element = {
    parse(element , input) match {
      case Result.Success(result) => result
      case Result.Failure(location, message) => throw new RuntimeException(s"${location}:${message}")
    }
  }
}
