// Basic project information
name          := "dropwizard-scala"

version       := "0.6.2"

organization  := "com.massrelevance"

scalaVersion  := "2.10.1"

scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-unchecked", "-target:jvm-1.6", "-feature", "-Ywarn-adapted-args", "-language:higherKinds")

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

publishTo <<= version { (v: String) =>
  val maven = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at maven + "content/repositories/snapshots/")
  else
    Some("releases"  at maven + "service/local/staging/deploy/maven2/")
}

libraryDependencies ++= Seq(
    "com.yammer.dropwizard" % "dropwizard-core" % "0.6.2",
    "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.1.3",
    "com.timgroup" % "java-statsd-client" % "2.0.0",
    "com.massrelevance" %% "metrics-scala" % "2.2.0"
)
