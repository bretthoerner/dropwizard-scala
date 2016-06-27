package com.massrelevance.dropwizard

import io.dropwizard.{Application, Configuration}

abstract class ScalaApplication[T <: Configuration] extends Application[T] {
  final def main(args: Array[String]) {
    run(args.toArray: _*)
  }
}
