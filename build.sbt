// Basic project information
name := "dropwizard-scala"

version := "0.8.5"

organization  := "com.massrelevance"

crossScalaVersions := Seq("2.10.4", "2.11.7")

scalacOptions <<= scalaVersion map { sv: String =>
  sv match {
    case s: String if s.startsWith("2.9") =>
      Seq("-encoding", "UTF-8", "-deprecation", "-unchecked", "-target:jvm-1.5")
    case _ =>
      Seq("-encoding", "UTF-8", "-deprecation", "-unchecked", "-target:jvm-1.6", "-feature", "-Ywarn-adapted-args")
  }
}

resolvers += "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository"

libraryDependencies ++= Seq(
  "io.dropwizard" % "dropwizard-core" % "0.8.5",
  "nl.grons" %% "metrics-scala" % "3.3.0",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.4.3",
  "org.scalatest" %% "scalatest" % "2.2.2" % "test",
  "org.mockito" % "mockito-core" % "1.10.8" % "test",
  "org.glassfish.jersey.test-framework" % "jersey-test-framework-core" % "2.6" % "test",
  "org.glassfish.jersey.core" % "jersey-client" % "2.16" % "test"
)

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

publishTo <<= version { (v: String) =>
  val maven = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at maven + "content/repositories/snapshots/")
  else
    Some("releases" at maven + "service/local/staging/deploy/maven2/")
}

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { x => false }

pomExtra := (
  <url>https://github.com/bretthoerner/dropwizard-scala</url>
  <licenses>
    <license>
      <name>Apache License 2.0</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.html</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:bretthoerner/dropwizard-scala.git</url>
    <connection>scm:git:git@github.com:bretthoerner/dropwizard-scala.git</connection>
  </scm>
  <developers>
    <developer>
      <id>bretthoerner</id>
      <name>Brett Hoerner</name>
      <url>http://bretthoerner.com</url>
      <timezone>-6</timezone>
    </developer>
    <developer>
      <id>clearstorydata</id>
      <name>ClearStory Data</name>
      <url>http://clearstorydata.com</url>
      <timezone>-8</timezone>
    </developer>
  </developers>
)
