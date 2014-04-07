package com.massrelevance.dropwizard.bundles

import io.dropwizard.Bundle
import io.dropwizard.setup.{Bootstrap, Environment}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.massrelevance.dropwizard.scala.inject.ScalaCollectionsQueryParamInjectableProvider

class ScalaBundle extends Bundle {
  def initialize(bootstrap: Bootstrap[_]) {
    bootstrap.getObjectMapper.registerModule(new DefaultScalaModule())
  }

  def run(environment: Environment) {
    environment.jersey.getResourceConfig.getClasses.add(classOf[ScalaCollectionsQueryParamInjectableProvider])
  }
}
