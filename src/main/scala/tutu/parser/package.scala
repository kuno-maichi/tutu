package tutu
import org.javafp.parsecj._
import org.javafp.parsecj.Combinators._
import org.javafp.parsecj.input.Input

package object parser {
  type TutuParser[A] = Parser[Input[Char], A]
  case class ~[A, B](_1: A, _2: B)
  implicit class RichParser[A](self: TutuParser[A]) {
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
  }
  def success[A](value: A): TutuParser[A] = retn(value)
}
