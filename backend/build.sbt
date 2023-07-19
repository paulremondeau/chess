val scala3Version = "3.3.0"

val http4sVersion = "0.23.16"

lazy val root = project
  .in(file("."))
  .settings(
    name := "backend",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test,
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-ember-server" % http4sVersion,
      "org.http4s" %% "http4s-dsl" % http4sVersion
    ),
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-circe" % http4sVersion,
      // Optional for auto-derivation of JSON codecs
      "io.circe" %% "circe-generic" % "0.14.5",
      // Optional for string interpolation to JSON model
      "io.circe" %% "circe-literal" % "0.14.5"
    ),
    libraryDependencies += "org.slf4j" % "slf4j-api" % "2.0.7",
    libraryDependencies += "org.slf4j" % "slf4j-simple" % "2.0.7",
    libraryDependencies += "org.typelevel" %% "log4cats-slf4j" % "2.6.0"
  )

addCompilerPlugin(
  "org.scalamacros" % "paradise_2.13.0-M1" % "2.1.0"
)
