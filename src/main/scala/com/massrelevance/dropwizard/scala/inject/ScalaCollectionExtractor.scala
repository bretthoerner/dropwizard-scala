package com.massrelevance.dropwizard.scala.inject

import scala.collection.JavaConversions._
import javax.ws.rs.core.MultivaluedMap
import com.sun.jersey.server.impl.model.parameter.multivalued.MultivaluedParameterExtractor
import scala.collection.generic.GenericCompanion
import com.sun.jersey.core.util.MultivaluedMapImpl

/**
 * Given a parameter name, a possibly-null default value, and a collection
 * companion object, attempts to extract all the parameter values and return a
 * collection instance. If defaultValue is null and no parameter exists, returns
 * an empty collection.
 */
class ScalaCollectionExtractor[+CC[X] <: Traversable[X]](companion: GenericCompanion[CC],
                                                                     extractor: MultivaluedParameterExtractor)
  extends MultivaluedParameterExtractor {

  def getName = extractor.getName

  def getDefaultStringValue = extractor.getDefaultStringValue

  def extract(parameters: MultivaluedMap[String, String]) = {
    val builder = companion.newBuilder[Object]
    val params = parameters.get(getName)
    if (params != null) {
      builder.sizeHint(params.size)
      for (param <- params) {
        val subMap = new MultivaluedMapImpl()
        subMap.putSingle(getName, param)
        builder += extractor.extract(subMap)
      }
    } else if (getDefaultStringValue != null) {
      builder += getDefaultStringValue
    }
    builder.result()
  }
}
