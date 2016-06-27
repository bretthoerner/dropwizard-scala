package com.massrelevance.dropwizard.scala.inject

import javax.ws.rs.core.MultivaluedMap

import org.glassfish.jersey.server.internal.inject.MultivaluedParameterExtractor

/**
 * Given a parameter name and a possibly-null default value, attempts to extract
 * the first parameter values and return a Some instance, returning the default
 * value if no parameter exists. If defaultValue is null and no parameter
 * exists, returns None.
 */
class ScalaOptionStringExtractor(parameter: String, defaultValue: String)
  extends MultivaluedParameterExtractor[Option[String]] {
  private val default = Option(defaultValue)

  def getName = parameter

  def getDefaultValueString = defaultValue

  def extract(parameters: MultivaluedMap[String, String]) =
    Option(parameters.getFirst(parameter)).orElse(default)
}
