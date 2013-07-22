package com.massrelevance.dropwizard.scala.inject

import javax.ws.rs.QueryParam
import javax.ws.rs.ext.Provider
import com.sun.jersey.api.model.Parameter
import com.sun.jersey.core.spi.component.{ProviderServices, ComponentScope, ComponentContext}
import com.sun.jersey.spi.inject.{Injectable, InjectableProvider}
import com.sun.jersey.server.impl.model.parameter.multivalued.{StringReaderFactory, MultivaluedParameterExtractorFactory, MultivaluedParameterExtractor}
import java.lang.reflect.{ParameterizedType, Type}
import com.fasterxml.jackson.module.scala.util.CompanionSorter
import collection.{immutable, mutable}
import collection.generic.GenericCompanion
import javax.ws.rs.core.Context

@Provider
class ScalaCollectionsQueryParamInjectableProvider (@Context services: ProviderServices) extends InjectableProvider[QueryParam, Parameter] {
  def getScope = ComponentScope.PerRequest

  def getInjectable(ic: ComponentContext, a: QueryParam, c: Parameter): Injectable[_] = {
    val parameterName = c.getSourceName
    if (parameterName != null && !parameterName.isEmpty && c.getParameterType.isInstanceOf[ParameterizedType]) {
      buildInjectable(parameterName, c.getDefaultValue, !c.isEncoded, c, c.getParameterType.asInstanceOf[ParameterizedType])
    } else null
  }

  private[this] def buildExtractor(typ: ParameterizedType, extractor: MultivaluedParameterExtractor): MultivaluedParameterExtractor = {
    val klass = typ.getRawType.asInstanceOf[Class[_]]

    if (classOf[Option[String]].isAssignableFrom(klass)) {
      new ScalaOptionExtractor(extractor)
    } else collectionCompanionFor(klass) match {
      case Some(companion) => new ScalaCollectionExtractor(companion, extractor)
      case None => null
    }
  }

  private[this] def buildInjectable(name: String, default: String, decode: Boolean, c: Parameter, typ: ParameterizedType): Injectable[_ <: Object] = {
    val extractor = buildExtractor(typ, factory.get(unpack(c, typ)))
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

  private[this] def collectionCompanionFor(klass: Class[_]): Option[GenericCompanion[Iterable]] =
    COLLECTION_COMPANIONS find { _._1.isAssignableFrom(klass) } map { _._2 }

  private[this] def unpack(param: Parameter, typ: ParameterizedType): Parameter = {
    /**
     * TODO: This does not work as intended yet. Scala method parameters appear to lose their type parameters even
     * though they're still around for Java methods. I need to further look into how Jackson Scala Module gets this to
     * work. So for now assume it's String which is backwards-compatible.
     */
    val typeParameter: Type = {
      val t = typ.getActualTypeArguments.head
      if (t == classOf[Object]) classOf[String] else t
    }
    new Parameter(param.getAnnotations, param.getAnnotation, param.getSource, param.getSourceName, typeParameter, typeParameter.asInstanceOf[Class[_]], param.isEncoded, param.getDefaultValue)
  }

  private[this] lazy val factory: MultivaluedParameterExtractorFactory = {
    val stringReaderFactory: StringReaderFactory = new StringReaderFactory
    stringReaderFactory.init(services)
    new MultivaluedParameterExtractorFactory(stringReaderFactory)
  }
}
