val CirceVersion = "0.10.0-M1"

lazy val commonSettings = Seq(
  organization := "com.alaphi",
  name := "monix-kafka-producer-consumer",
  version := "0.0.1-SNAPSHOT",
  scalaVersion := "2.12.7",
  libraryDependencies ++= Seq(
    "io.monix" %% "monix-kafka-1x" % "1.0.0-RC1",
    "io.circe"        %% "circe-core"          % CirceVersion,
    "io.circe"        %% "circe-generic"       % CirceVersion,
    "io.circe"        %% "circe-parser"        % CirceVersion
  )
)

lazy val dockerSettings = Seq(
  dockerBaseImage := "openjdk:jre-alpine",
  dockerUpdateLatest := true
)

lazy val root = (project in file("."))
  .aggregate(common, producer, consumer)

lazy val common = (project in file("common"))
  .settings(commonSettings)

lazy val producer = (project in file("producer"))
  .enablePlugins(JavaAppPackaging, DockerPlugin, AshScriptPlugin)
  .dependsOn(common)
  .settings(dockerSettings)

lazy val consumer = (project in file("consumer"))
  .enablePlugins(JavaAppPackaging, DockerPlugin, AshScriptPlugin)
  .dependsOn(common)
  .settings(dockerSettings)