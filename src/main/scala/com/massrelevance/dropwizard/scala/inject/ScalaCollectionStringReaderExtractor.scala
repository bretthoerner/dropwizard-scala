package com.massrelevance.dropwizard.scala.inject

import javax.ws.rs.core.MultivaluedMap

import org.glassfish.jersey.server.internal.inject.MultivaluedParameterExtractor

import scala.collection.JavaConversions._
import scala.collection.generic.GenericCompanion

/**
 * Given a parameter name, a possibly-null default value, and a collection
 * companion object, attempts to extract all the parameter values and return a
 * collection instance. If defaultValue is null and no parameter exists, returns
 * an empty collection.
 */
class ScalaCollectionStringReaderExtractor[CC[X] <: Traversable[X]](parameter: String,
                                                                    defaultValue: String,
                                                                    companion: GenericCompanion[CC])
  extends MultivaluedParameterExtractor[CC[String]] {

  def getName = parameter

  def getDefaultValueString = defaultValue

  def extract(parameters: MultivaluedMap[String, String]) = {
    val builder = companion.newBuilder[String]
    val params = parameters.get(parameter)
    if (params != null) {
      builder.sizeHint(params.size)
      for (param <- params) {
        builder += param
      }
    } else if (defaultValue != null) {
      builder += defaultValue
    }
    builder.result()
  }
}
