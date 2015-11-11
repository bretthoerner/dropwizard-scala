Dropwizard-Scala
================

A fork of the original (now unmaintained) dropwizard-scala subproject of [Dropwizard](https://github.com/codahale/dropwizard).

[![Build Status](https://travis-ci.org/bretthoerner/dropwizard-scala.png)](https://travis-ci.org/bretthoerner/dropwizard-scala)

***

SBT information:

```scala
libraryDependencies += "com.massrelevance" %% "dropwizard-scala" % "0.7.1"
```

***

Dropwizard services should extend `ScalaApplication` instead of `Application`
and add `ScalaBundle` (See ExampleService below).

The following code snippet is all the elements required to create a Dropwizard resource and get it up and running

```scala

import javax.ws.rs.{GET, Path, Produces, QueryParam}

import com.codahale.metrics.annotation.Timed
import com.fasterxml.jackson.annotation.JsonProperty
import com.massrelevance.dropwizard.ScalaApplication
import com.massrelevance.dropwizard.bundles.ScalaBundle
import io.dropwizard.Configuration
import io.dropwizard.setup.{Bootstrap, Environment}
import org.hibernate.validator.constraints.NotEmpty

object ExampleService extends ScalaApplication[ExampleConfiguration] {

  override def getName = "example"

  def initialize(bootstrap: Bootstrap[ExampleConfiguration]) {
    bootstrap.addBundle(new ScalaBundle)
  }

  def run(configuration: ExampleConfiguration, environment: Environment) {
    environment.jersey().register(new ExampleResource(configuration.defaultName, configuration.template))
  }
}

class ExampleConfiguration extends Configuration {

  @NotEmpty
  @JsonProperty
  val defaultName: String = "world!"

  @NotEmpty
  @JsonProperty
  val template: String = "Hello %s"
}

@Path("/greeting")
@Produces(Array("application/json"))
class ExampleResource(val defaultName: String, val template: String) {

  @GET
  @Timed
  def sayHello(@QueryParam("name") name: Option[String]): String = {
    String.format(template, name.getOrElse(defaultName))
  }
}


```

Run it using:

`java -jar project.jar server`

Test it by going to:

`http://localhost:8080/greeting`


Features
========

`dropwizard-scala` provides the following:

* `QueryParam`-annotated parameters of type `Seq[String]`, `List[String]`, `Vector[String]`,
  `IndexedSeq[String]`, `Set[String]`, and `Option[String]`.
* Case class (i.e., `Product` instances) JSON request and response entities.
* `Array[A]` request and response entities. (Due to the JVM's type erasure and mismatches between
  Scala and Java type signatures, this is the only "generic" class supported since `Array` type
  parameters are reified.)
