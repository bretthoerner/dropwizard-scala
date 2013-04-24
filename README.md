Dropwizard-Scala
================

A fork of the original (now unmaintained) dropwizard-scala subproject of [Dropwizard](https://github.com/codahale/dropwizard).

[![Build Status](https://travis-ci.org/bretthoerner/dropwizard-scala.png)](https://travis-ci.org/nbauernfeind/dropwizard-scala)

***

SBT information:

```scala
libraryDependencies += "com.massrelevance" % "dropwizard-scala" % "0.6.2"
```

***

Dropwizard services should extend `ScalaService` instead of `Service`
and add `ScalaBundle`:

```scala

    object ExampleService extends ScalaService[ExampleConfiguration]) {
      def initialize(bootstrap: Bootstrap[ExampleConfiguration]) {
        bootstrap.setName("example")
        bootstrap.addBundle(new ScalaBundle)
      }

      def run(configuration: ExampleConfiguration, environment: Environment) {
        environment.addResource(new ExampleResource)
      }
    }
```

Features
========

`dropwizard-scala` provides the following:

* `QueryParam`-annotated parameters of type `Seq[String]`, `List[String]`, `Vector[String]`,
  `IndexedSeq[String]`, `Set[String]`, and `Option[String]`.
* Case class (i.e., `Product` instances) JSON request and response entities.
* `Array[A]` request and response entities. (Due to the JVM's type erasure and mismatches between
  Scala and Java type signatures, this is the only "generic" class supported since `Array` type
  parameters are reified.)
