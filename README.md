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
and add `ScalaBundle`:

```scala

    object ExampleService extends ScalaApplication[ExampleConfiguration]) {
      override def getName = "example"

      def initialize(bootstrap: Bootstrap[ExampleConfiguration]) {
        bootstrap.addBundle(new ScalaBundle)
      }

      def run(configuration: ExampleConfiguration, environment: Environment) {
        env.jersey().register(new ExampleResource)
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
