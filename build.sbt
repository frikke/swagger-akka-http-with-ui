import sbt.Keys.libraryDependencies
import sbtghactions.JavaSpec.Distribution.Zulu

organization := "com.github.swagger-akka-http"

name := "swagger-akka-http-with-ui"

val akkaVersion = "2.6.16"
val akkaHttpVersion = "10.2.8"
val jacksonVersion = "2.13.1"
val swaggerVersion = "2.1.12"

val scala213 = "2.13.8"
val slf4jVersion = "1.7.36"

ThisBuild / scalaVersion := scala213
ThisBuild / crossScalaVersions := Seq(scala213, "2.12.15")

update / checksums := Nil

lazy val root = (project in file("."))
  .settings(
    libraryDependencies ++= Seq("com.github.swagger-akka-http" %% "swagger-akka-http" % "2.6.0",
      "org.webjars" % "webjars-locator" % "0.42",
      "org.webjars" % "swagger-ui" % "4.1.3",
      "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
      "org.scalatest" %% "scalatest" % "3.2.10" % Test,
      "org.json4s" %% "json4s-native" % "4.0.3" % Test,
      "org.jsoup" % "jsoup" % "1.14.3" % Test,
      "jakarta.ws.rs" % "jakarta.ws.rs-api" % "3.0.0" % Test,
      "joda-time" % "joda-time" % "2.10.13" % Test,
      "org.joda" % "joda-convert" % "2.2.2" % Test,
      "org.slf4j" % "slf4j-simple" % slf4jVersion % Test

    ),
  )


Test / testOptions += Tests.Argument("-oD")

Test / publishArtifact := false

Test / parallelExecution := false

pomIncludeRepository := { _ => false }

homepage := Some(url("https://github.com/swagger-akka-http/swagger-akka-http-with-ui"))

licenses := Seq("The Apache Software License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

pomExtra := (
  <developers>
    <developer>
      <id>gerbrand</id>
      <name>Gerbrand van Dieijen</name>
      <url>https://software-creation.nl</url>
    </developer>
    <developer>
      <id>pjfanning</id>
      <name>PJ Fanning</name>
      <url>https://github.com/pjfanning</url>
    </developer>
  </developers>)

ThisBuild / githubWorkflowJavaVersions := Seq(JavaSpec(Zulu, "8"), JavaSpec(Zulu, "11"))
ThisBuild / githubWorkflowTargetTags ++= Seq("v*")
ThisBuild / githubWorkflowPublishTargetBranches := Seq(
  RefPredicate.Equals(Ref.Branch("main")),
  RefPredicate.StartsWith(Ref.Tag("v"))
)

ThisBuild / githubWorkflowPublish := Seq(
  WorkflowStep.Sbt(
    List("ci-release"),
    env = Map(
      "PGP_PASSPHRASE" -> "${{ secrets.PGP_PASSPHRASE }}",
      "PGP_SECRET" -> "${{ secrets.PGP_SECRET }}",
      "SONATYPE_PASSWORD" -> "${{ secrets.SONATYPE_PASSWORD }}",
      "SONATYPE_USERNAME" -> "${{ secrets.SONATYPE_USERNAME }}",
      "CI_SNAPSHOT_RELEASE" -> "+publishSigned"
    )
  )
)
