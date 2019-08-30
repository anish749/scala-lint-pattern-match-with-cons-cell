# scala-lint-pattern-match-with-cons-cell
Scalafix Linting for Pattern Matching with cons cells (Scala 2.11, 2.12)

## The problem
Pattern Matching using cons cell (`::`) compiles with `Seq[T]` or `Iterable[T]`. `::` is however
the `List[T]` implementation of `Seq` / `Iterable`. A `Seq` can be backed by a `WrappedArray[T]` or any
other implementation that is not a `List[T]`. This leads to mistakes from developers when the code
compiles but fails at runtime. If there is a `case _ =>`, it often leads to unexpected results.

Consider the following example:
```scala
val s: Seq[Int] = Array(1, 2, 3).toSeq // Seq backed by a WrappedArray[Int]
s match {
  case head :: tail => println(head)
  case _ => println("Fail")
}
```
We often make the assumption that this code will print `1`, but it doesn't.

This is a linting rule, which points out these usages.
A sample fix would be:
```
s.toList match {
  case head :: tail => println(head)
  case _ => println("Fail")
}
```

Since a `toList` might not be desired in some cases, we only point out this problem,
but not actively fix it. Also if we have an `Iterable[T]` or a `Seq[T]`, it might
be backed by a `List[T]` at runtime, and we wouldn't know that at compile time.

## Usage
Add the following plugin to `plugins.sbt`
```scala
addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.9.0")
```

Start `sbt` shell.
```sh
$ sbt
```

Enable Scalafix and run the rule as follows:

```sbt
sbt> scalafixEnable
sbt> scalafix --rules=github:anish749/scala-lint-pattern-match-with-cons-cell/CheckPatternMatchWithConsCell
```
