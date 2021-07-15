package tutu
import org.javafp.parsecj._
import org.javafp.parsecj.Combinators._
import org.javafp.parsecj.input.Input
import org.javafp.parsecj.Text

import java.util.Optional

package object parser {
  type TutuParser[A] = Parser[Character, A]
  case class ~[A, B](_1: A, _2: B)
  implicit class RichParser[A >: Null](self: TutuParser[A]) {
    def flatMap[B](function: A => TutuParser[B]): TutuParser[B] = {
      self.bind(a => function(a))
    }
    def map[B](function: A => B): TutuParser[B] = {
      self.map(a => function(a))
    }
    def |(that: TutuParser[A]): TutuParser[A] = {
      self.or(that)
    }
    def ~[B](that: TutuParser[B]): TutuParser[A ~ B] = {
      self.bind(a =>
        that.bind(b =>
          retn(_root_.tutu.parser.~(a, b))
        )
      )
    }
    def withFilter(predicate: A => Boolean): TutuParser[A] = {
      self.flatMap(a => if(predicate(a)) self else fail("condition doesn't satisfy"))
    }
  }
  def literal(value: String): TutuParser[String] = Text.string(value)
  def optional[A >: Null](value: TutuParser[A]): TutuParser[Option[A]] = {
    value.optionalOpt().map[Option[A]]((v: Optional[A]) => Option(v.orElse(null)))
  }
  def success[A](value: A): TutuParser[A] = retn(value)
}
