package tutu.parser

object Ast {
  sealed abstract class Prefix(name: String)
  object Prefix {
    case object Narou extends Prefix("narou")
    case object Kakuyomu extends Prefix("kakuyomu")
    case class UseDefinedPrefix(name: String) extends Prefix(name)
  }
  sealed abstract class Tag(val prefix: Option[Prefix], val name: String)
  case class SynopsisTag(override val prefix: Option[Prefix]) extends Tag(prefix, "synopsis")
  case class UserTag(override val prefix: Option[Prefix], override val name: String) extends Tag(prefix, name)
  sealed abstract class Element(val name: String) {
    def prefix: Option[Prefix]
  }
  case class UserElement(prefix: Option[Prefix], override val name: String, body: String) extends Element(name)
  case class Synopsis(prefix: Option[Prefix], body: String) extends Element("synopsis")
}
