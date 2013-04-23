package com.yammer.dropwizard.scala.inject

import javax.ws.rs.QueryParam
import javax.ws.rs.ext.Provider
import com.sun.jersey.api.model.Parameter
import com.sun.jersey.core.spi.component.{ComponentScope, ComponentContext}
import com.sun.jersey.spi.inject.{Injectable, InjectableProvider}
import com.sun.jersey.server.impl.model.parameter.multivalued.MultivaluedParameterExtractor
import java.lang.reflect.{ParameterizedType, Type}

@Provider
class ScalaCollectionsQueryParamInjectableProvider extends InjectableProvider[QueryParam, Parameter] {
  def getScope = ComponentScope.PerRequest

  def getInjectable(ic: ComponentContext, a: QueryParam, c: Parameter): Injectable[_] = {
    val parameterName = c.getSourceName
    if (parameterName != null && !parameterName.isEmpty) {
      buildInjectable(parameterName, c.getDefaultValue, !c.isEncoded, c.getParameterType)
    } else null
  }

  private def buildExtractor(name: String, default: String, typ: Type): MultivaluedParameterExtractor = {
    val klass = typ match {
      case c: Class[_] => c
      case pt: ParameterizedType => pt.getRawType.asInstanceOf[Class[_]]
    }

    if (klass == classOf[Seq[String]]) {
      new ScalaCollectionStringReaderExtractor(name, default, Seq)
    } else if (klass == classOf[List[String]]) {
      new ScalaCollectionStringReaderExtractor(name, default, List)
    } else if (klass == classOf[Vector[String]]) {
      new ScalaCollectionStringReaderExtractor(name, default, Vector)
    } else if (klass == classOf[IndexedSeq[String]]) {
      new ScalaCollectionStringReaderExtractor(name, default, IndexedSeq)
    } else if (klass == classOf[Set[String]]) {
      new ScalaCollectionStringReaderExtractor(name, default, Set)
    } else if (klass == classOf[Option[String]]) {
      new ScalaOptionStringExtractor(name, default)
    } else null
  }

  private def buildInjectable(name: String, default: String, decode: Boolean, typ: Type): Injectable[_ <: Object] = {
    val extractor = buildExtractor(name, default, typ)
    if (extractor != null) {
      new ScalaCollectionQueryParamInjectable(extractor, decode)
    } else null
  }
}
