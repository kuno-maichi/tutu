package tutu.parser

object Ast {
  sealed abstract class Prefix(name: String)
  object Prefix {
    case object Narou extends Prefix("narou")
    case object Kakuyomu extends Prefix("kakuyomu")
    case class UseDefinedPrefix(name: String) extends Prefix(name)
  }
  case class Element(prefix: Option[Prefix], name: String, body: String)
}
