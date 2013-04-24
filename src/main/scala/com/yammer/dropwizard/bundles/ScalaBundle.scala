package com.yammer.dropwizard.bundles

import com.yammer.dropwizard.Bundle
import com.yammer.dropwizard.config.{Bootstrap, Environment}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.yammer.dropwizard.scala.inject.ScalaCollectionsQueryParamInjectableProvider

class ScalaBundle extends Bundle {
  def initialize(bootstrap: Bootstrap[_]) {
    bootstrap.getObjectMapperFactory.registerModule(new DefaultScalaModule())
  }

  def run(environment: Environment) {
    environment.getJerseyResourceConfig.getClasses.add(classOf[ScalaCollectionsQueryParamInjectableProvider])
  }
}
