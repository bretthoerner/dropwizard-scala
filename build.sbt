// Basic project information
name          := "dropwizard-scala"

version       := "0.6.1"

organization  := "com.yammer.dropwizard"

scalaVersion  := "2.10.0"

scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-unchecked", "-target:jvm-1.6", "-feature", "-Ywarn-adapted-args", "-language:higherKinds")

libraryDependencies ++= Seq(
    "com.yammer.dropwizard" % "dropwizard-core" % "0.6.1",
    "com.fasterxml.jackson.module" % "jackson-module-scala" % "2.1.2",
    "com.timgroup" % "java-statsd-client" % "2.0.0",
    "com.yammer.metrics" %% "metrics-scala" % "2.2.0"
)
