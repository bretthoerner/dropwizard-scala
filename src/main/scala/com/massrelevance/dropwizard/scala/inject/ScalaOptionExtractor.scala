package com.massrelevance.dropwizard.scala.inject

import javax.ws.rs.core.MultivaluedMap
import com.sun.jersey.server.impl.model.parameter.multivalued.MultivaluedParameterExtractor

/**
 * Given a parameter name and a possibly-null default value, attempts to extract
 * the first parameter values and return a Some instance, returning the default
 * value if no parameter exists. If defaultValue is null and no parameter
 * exists, returns None.
 */
class ScalaOptionExtractor(extractor: MultivaluedParameterExtractor) extends MultivaluedParameterExtractor {
  def getName = extractor.getName

  def getDefaultStringValue = extractor.getDefaultStringValue

  def extract(parameters: MultivaluedMap[String, String]) = Option(extractor.extract(parameters))
}
