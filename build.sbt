name := "mongo-test"

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  "org.mongodb.scala"           %% "mongo-scala-driver" % "2.2.0",
  "com.typesafe"                %  "config"             % "1.3.1",
  "com.softwaremill.macwire"    %% "macros"             % "2.3.0" % Provided,
  "org.scalatest"               %% "scalatest"          % "3.0.5" % Test
)

scalacOptions ++= Seq(
  "-feature",
  "-language:postfixOps"
)

parallelExecution in Test := false
fork in Test := true
logBuffered in Test := false