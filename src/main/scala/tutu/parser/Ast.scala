package tutu.parser

object Ast {
  case class Element(prefix: Option[String], name: String, body: String)
}
