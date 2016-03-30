name := """akka-http-todo"""

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies += "org.scalactic" %% "scalactic" % "2.2.6"
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test"
libraryDependencies += "com.typesafe.akka" % "akka-actor_2.11" % "2.4.2"
libraryDependencies += "com.typesafe.akka" % "akka-http-experimental_2.11" % "2.4.2"
libraryDependencies += "com.typesafe.akka" % "akka-http-spray-json-experimental_2.11" % "2.4.2"
libraryDependencies += "com.typesafe.akka" % "akka-testkit_2.11" % "2.4.2"
libraryDependencies += "com.typesafe.akka" % "akka-http-xml-experimental_2.11" % "2.4.2"
libraryDependencies += "com.typesafe.akka" % "akka-http-testkit_2.11" % "2.4.2"