package tutu.model
import scala.collection.immutable.Map

object Ast {
  sealed trait Novel {
    def title: String
    def synopsis: String
    def episodes: Seq[Episode]
    def attributes: Map[String, Any]
  }
  case class NarouNovel(title: String, synopsis: String, episodes: Seq[Episode], attributes: Map[String,Any] = Map.empty) extends Novel
  case class KakuyomuNovel(title: String, synopsis: String, episodes: Seq[Episode], catchphrase: String, attributes: Map[String, Any] = Map.empty) extends Novel
  case class Episode(name: String, body: String, forward: String, afterward: String)
}
