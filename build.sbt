scalaVersion := "2.13.3"
name := "stellar"
organization := "com.altaworks"
version := "0.1"

resolvers += "jitpack" at "https://jitpack.io"
resolvers += Resolver.jcenterRepo
libraryDependencies += "com.github.synesso" %% "scala-stellar-sdk" % "0.16.0"
libraryDependencies += "ch.qos.logback"     % "logback-core"       % "1.2.3"

(compile in Compile) := ((compile in Compile) dependsOn (scalafmtCheck in Compile, scalafmtSbtCheck in Compile)).value
(compile in Test) := ((compile in Test) dependsOn (scalafmtCheck in Test)).value

fork := true
javaOptions ++= Seq(
  "-Dlogback.configurationFile=./logback.xml"
)
