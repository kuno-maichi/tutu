package tutu.parser

import org.javafp.parsecj.input.Input
import org.scalatest.diagrams.Diagrams
import org.scalatest.funsuite.AnyFunSuite

class DocumentParserTest extends AnyFunSuite with Diagrams {
  test("one element ") {
    val elementParser: TutuParser[Ast.Element] = DocumentParser.element
    val element = elementParser.parse(Input.of("""
[なろう:あらすじ]{
これはあらすじです。
}""")).getResult
    assert(element.prefix == Some("なろう"))
    assert(element.name == "あらすじ")
    assert(element.body == "これはあらすじです。")
  }
}
