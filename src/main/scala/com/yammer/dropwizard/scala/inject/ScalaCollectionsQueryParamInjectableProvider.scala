package com.yammer.dropwizard.scala.inject

import javax.ws.rs.QueryParam
import javax.ws.rs.ext.Provider
import com.sun.jersey.api.model.Parameter
import com.sun.jersey.core.spi.component.{ComponentScope, ComponentContext}
import com.sun.jersey.spi.inject.{Injectable, InjectableProvider}
import com.sun.jersey.server.impl.model.parameter.multivalued.MultivaluedParameterExtractor
import java.lang.reflect.{ParameterizedType, Type}
import com.fasterxml.jackson.module.scala.util.CompanionSorter
import collection.{immutable, mutable}
import collection.generic.GenericCompanion

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

    if (classOf[Option[String]].isAssignableFrom(klass)) {
      new ScalaOptionStringExtractor(name, default)
    } else collectionCompanionFor(klass) match {
      case Some(companion) => new ScalaCollectionStringReaderExtractor(name, default, companion)
      case None => null
    }
  }

  private def buildInjectable(name: String, default: String, decode: Boolean, typ: Type): Injectable[_ <: Object] = {
    val extractor = buildExtractor(name, default, typ)
    if (extractor != null) {
      new ScalaCollectionQueryParamInjectable(extractor, decode)
    } else null
  }

  /* Companion sorting guarantees that the first match is the most specific if multiple classes could match. */
  private[this] val COLLECTION_COMPANIONS = new CompanionSorter[Iterable]()
    .add(immutable.HashSet)
    .add(immutable.List)
    .add(immutable.ListSet)
    .add(immutable.Queue)
    .add(immutable.Set)
    .add(immutable.Vector)
    .add(IndexedSeq)
    .add(mutable.ArraySeq)
    .add(mutable.Buffer)
    .add(mutable.HashSet)
    .add(mutable.IndexedSeq)
    .add(mutable.LinearSeq)
    .add(mutable.LinkedHashSet)
    .add(mutable.ListBuffer)
    .add(mutable.MutableList)
    .add(mutable.Queue)
    .add(mutable.ResizableArray)
    .add(mutable.Set)
    .add(Seq)
    .add(Stream)
    .toList

  def collectionCompanionFor(klass: Class[_]): Option[GenericCompanion[Iterable]] =
    COLLECTION_COMPANIONS find { _._1.isAssignableFrom(klass) } map { _._2 }
}
