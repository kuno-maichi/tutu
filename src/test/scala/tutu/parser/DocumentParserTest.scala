package tutu.parser

import org.javafp.parsecj.input.Input
import org.scalatest.diagrams.Diagrams
import org.scalatest.funsuite.AnyFunSuite

class DocumentParserTest extends AnyFunSuite with Diagrams {
  test("[なろう:あらすじ] is parsed") {
    val elementParser: TutuParser[Ast.Element] = DocumentParser.element
    val element = elementParser.parse(Input.of("""
[なろう:あらすじ]{
これはあらすじです。
}""")).getResult
    assert(element.prefix == Some(Ast.Prefix.Narou))
    assert(element.name == "synopsis")
    assert(element.asInstanceOf[Ast.Synopsis].body == "これはあらすじです。")
  }

  test("[カクヨム:あらすじ] is parsed") {
    val elementParser: TutuParser[Ast.Element] = DocumentParser.element
    val element = elementParser.parse(Input.of("""
[カクヨム:あらすじ]{
これはあらすじです。
}""")).getResult
    assert(element.prefix == Some(Ast.Prefix.Kakuyomu))
    assert(element.name == "synopsis")
    assert(element.asInstanceOf[Ast.Synopsis].body == "これはあらすじです。")
  }
}
