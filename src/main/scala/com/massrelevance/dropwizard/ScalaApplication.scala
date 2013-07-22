package com.massrelevance.dropwizard

import com.codahale.dropwizard.Configuration
import com.codahale.dropwizard.Application

abstract class ScalaApplication[T <: Configuration] extends Application[T] {
  final def main(args: Array[String]) {
    run(args)
  }
}
