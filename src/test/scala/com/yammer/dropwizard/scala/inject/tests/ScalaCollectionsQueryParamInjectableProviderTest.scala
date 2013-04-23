package com.yammer.dropwizard.scala.inject.tests

import javax.ws.rs.QueryParam
import com.sun.jersey.api.model.Parameter
import com.sun.jersey.core.spi.component.{ComponentContext, ComponentScope}
import com.sun.jersey.core.util.MultivaluedMapImpl
import com.sun.jersey.api.core.{HttpContext, ExtendedUriInfo}
import org.specs2.mutable._
import org.specs2.mock.Mockito
import com.yammer.dropwizard.scala.inject.{ScalaCollectionQueryParamInjectable, ScalaCollectionsQueryParamInjectableProvider}
import java.lang.reflect.{ParameterizedType, Type}

class ScalaCollectionsQueryParamInjectableProviderTest extends Specification with Mockito {

  "A Scala collections query param injectable provider" should {
    val httpContext = mock[HttpContext]
    val uriInfo = mock[ExtendedUriInfo]
    val params = new MultivaluedMapImpl()
    params.add("name", "one")
    params.add("name", "two")
    params.add("name", "three")

    httpContext.getUriInfo returns uriInfo
    uriInfo.getQueryParameters(any[Boolean]) returns params

    val context = mock[ComponentContext]
    val queryParam = mock[QueryParam]

    val provider = new ScalaCollectionsQueryParamInjectableProvider


    "has a per-request scope" in {
      provider.getScope.must(be(ComponentScope.PerRequest))
    }

    "returns an injectable for Seq instances" in {
      val param = parameter[Seq[String]]
      val injectable = provider.getInjectable(context, queryParam, param).asInstanceOf[ScalaCollectionQueryParamInjectable]

      injectable.getValue(httpContext).must(beEqualTo(Seq("one", "two", "three")))
    }

    "returns an injectable for List instances" in {
      val param = parameter[List[String]]
      val injectable = provider.getInjectable(context, queryParam, param).asInstanceOf[ScalaCollectionQueryParamInjectable]

      injectable.getValue(httpContext).must(beEqualTo(List("one", "two", "three")))
    }

    "returns an injectable for Vector instances" in {
      val param = parameter[Vector[String]]
      val injectable = provider.getInjectable(context, queryParam, param).asInstanceOf[ScalaCollectionQueryParamInjectable]

      injectable.getValue(httpContext).must(beEqualTo(Vector("one", "two", "three")))
    }

    "returns an injectable for IndexedSeq instances" in {
      val param = parameter[IndexedSeq[String]]
      val injectable = provider.getInjectable(context, queryParam, param).asInstanceOf[ScalaCollectionQueryParamInjectable]

      injectable.getValue(httpContext).must(beEqualTo(IndexedSeq("one", "two", "three")))
    }

    "return an injectable for Set instances" in {
      val param = parameter[Set[String]]
      val injectable = provider.getInjectable(context, queryParam, param).asInstanceOf[ScalaCollectionQueryParamInjectable]

      injectable.getValue(httpContext).must(beEqualTo(Set("one", "two", "three")))
    }

    "returns an injectable for Option instances" in {
      val param = parameter[Option[String]]
      val injectable = provider.getInjectable(context, queryParam, param).asInstanceOf[ScalaCollectionQueryParamInjectable]

      injectable.getValue(httpContext).must(beEqualTo(Some("one")))
    }
  }

  private[this] def parameter[T : Manifest]: Parameter = {
    new Parameter(Array(), null, null, "name", typeFromManifest(manifest[T]), manifest[T].runtimeClass, false, "default")
  }

  private[this] def typeFromManifest(m: Manifest[_]): Type = {
    if (m.typeArguments.isEmpty) {m.runtimeClass}
    else {
      new ParameterizedType {
        def getRawType = m.runtimeClass

        def getActualTypeArguments = m.typeArguments.map(typeFromManifest).toArray

        def getOwnerType = null
      }
    }
  }
}
