organization := "com.evojam"

name := "nlp-trainer"

scalaVersion := "2.11.7"

crossScalaVersions := Seq("2.11.7")

scalacOptions ++= Seq(
  "-target:jvm-1.8",
  "-encoding", "UTF-8",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Xlint",
  "-Ywarn-adapted-args",
  "-Ywarn-value-discard",
  "-Ywarn-inaccessible",
  "-Ywarn-dead-code"
)

licenses +=("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html"))

libraryDependencies ++= Seq(
  "org.scalanlp" %% "epic" % "0.3.1"
)
