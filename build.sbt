// Basic project information
name          := "dropwizard-scala"

version       := "0.6.2-2-SNAPSHOT"

organization  := "com.massrelevance"

crossScalaVersions := Seq("2.9.1", "2.9.2", "2.10.0")

scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-unchecked", "-target:jvm-1.5")

libraryDependencies ++= Seq(
    "com.yammer.dropwizard" % "dropwizard-core" % "0.6.2",
    "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.1.3",
    "com.timgroup" % "java-statsd-client" % "2.0.0",
    "org.scalatest" %% "scalatest" % "2.0.M5b" % "test",
    "org.mockito" % "mockito-core" % "1.9.5" % "test"
)

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

publishTo <<= version { (v: String) =>
  val maven = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at maven + "content/repositories/snapshots/")
  else
    Some("releases"  at maven + "service/local/staging/deploy/maven2/")
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
  </developers>
)
