package fix

import scalafix.v1._

import scala.meta._

/**
 * Actual Scalafix Semantic Rule to Check for pattern match on non-Lists.
 */
class CheckPatternMatchWithConsCell extends SemanticRule("CheckPatternMatchWithConsCell") {

  // Symbols that compile with cons cell, but fail at runtime.
  private val blackListedSymbols = Set(
    "TraversableOnce",
    "Traversable",
    "Iterable",
    "Seq")
    .map(t => s"scala/package.$t#")
    .map(Symbol(_))

  override def fix(implicit doc: SemanticDocument): Patch = {
    //    The following helps a lot while developing.
    //    println("Tree.syntax: " + doc.tree.syntax)
    //    println("Tree.structure: " + doc.tree.structure)
    //    println("Tree.structureLabeled: " + doc.tree.structureLabeled)

    doc.tree.collect {
      case Term.Match(term@Term.Name(_), cases) =>

        val matchOnGenericTyp = term.symbol.info.map(_.signature).collect {
          case ValueSignature(TypeRef(_, typ, _)) =>
            typ
        }.exists(blackListedSymbols.contains)

        val hasConsCellInMatch = cases.exists {
          case Case(Pat.ExtractInfix(_, Term.Name(op), _), cond, body) =>
            op == "::"
          case _ => false
        }

        if (matchOnGenericTyp && hasConsCellInMatch) {
          Patch.lint(new Diagnostic {
            override def message: String =
              """
                |Possible pattern match with :: on a collection whose runtime type may not be a List.
                |:: should only be used to pattern match on List[A].
              """.stripMargin

            override def position: Position = term.pos
          })
        }
        else {
          Patch.empty
        }
    }.asPatch
  }
}
