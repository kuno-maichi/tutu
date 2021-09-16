package tutu.parser

import tutu.combinator._
import org.scalatest.diagrams.Diagrams
import org.scalatest.funsuite.AnyFunSuite

class DocumentParserTest extends AnyFunSuite with Diagrams {
  test("[なろう:あらすじ] が解析可能") {
    val element = DocumentParser.parseElement("""
[なろう:あらすじ]{
これはあらすじです。
}""")
    assert(element.prefix == Some(Ast.Prefix.Narou))
    assert(element.name == "synopsis")
    assert(element.asInstanceOf[Ast.Synopsis].body == "これはあらすじです。")
  }

  test("[カクヨム:あらすじ] が解析可能") {
    val element = DocumentParser.parseElement("""
[カクヨム:あらすじ]{
これはあらすじです。
}""")
    assert(element.prefix == Some(Ast.Prefix.Kakuyomu))
    assert(element.name == "synopsis")
    assert(element.asInstanceOf[Ast.Synopsis].body == "これはあらすじです。")
  }
}
