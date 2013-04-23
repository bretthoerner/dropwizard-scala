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
import collection.{immutable, mutable}

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

    "returns an injectable for Option instances" in {
      getValue[Option[String]] must beEqualTo(Some("one"))
    }

    "returns an injectable for immutable.HashSet instances" in {
      getValue[immutable.HashSet[String]] must beEqualTo(immutable.HashSet("one", "two", "three"))
    }

    "returns an injectable for List instances" in {
      getValue[List[String]] must beEqualTo(List("one", "two", "three"))
    }

    "returns an injectable for ListSet instances" in {
      getValue[immutable.ListSet[String]] must beEqualTo(immutable.ListSet("one", "two", "three"))
    }

    "returns an injectable for immutable.Queue instances" in {
      getValue[immutable.Queue[String]] must beEqualTo(immutable.Queue("one", "two", "three"))
    }

    "returns an injectable for immutable.Set instances" in {
      getValue[immutable.Set[String]] must beEqualTo(immutable.Set("one", "two", "three"))
    }

    "returns an injectable for immutable.Vector instances" in {
      getValue[immutable.Vector[String]] must beEqualTo(immutable.Vector("one", "two", "three"))
    }

    "returns an injectable for IndexedSeq instances" in {
      getValue[IndexedSeq[String]] must beEqualTo(IndexedSeq("one", "two", "three"))
    }

    "returns an injectable for mutable.ArraySeq instances" in {
      getValue[mutable.ArraySeq[String]] must beEqualTo(mutable.ArraySeq("one", "two", "three"))
    }

    "returns an injectable for mutable.Buffer instances" in {
      getValue[mutable.Buffer[String]] must beEqualTo(mutable.Buffer("one", "two", "three"))
    }

    "returns an injectable for mutable.HashSet instances" in {
      getValue[mutable.HashSet[String]] must beEqualTo(mutable.HashSet("one", "two", "three"))
    }

    "returns an injectable for mutable.IndexedSeq instances" in {
      getValue[mutable.IndexedSeq[String]] must beEqualTo(mutable.IndexedSeq("one", "two", "three"))
    }

    "returns an injectable for mutable.LinearSeq instances" in {
      getValue[mutable.LinearSeq[String]] must beEqualTo(mutable.LinearSeq("one", "two", "three"))
    }

    "returns an injectable for mutable.LinkedHashSet instances" in {
      getValue[mutable.LinkedHashSet[String]] must beEqualTo(mutable.LinkedHashSet("one", "two", "three"))
    }

    "returns an injectable for mutable.ListBuffer instances" in {
      getValue[mutable.ListBuffer[String]] must beEqualTo(mutable.ListBuffer("one", "two", "three"))
    }

    "returns an injectable for mutable.MutableList instances" in {
      getValue[mutable.MutableList[String]] must beEqualTo(mutable.MutableList("one", "two", "three"))
    }

    "returns an injectable for mutable.Queue instances" in {
      getValue[mutable.Queue[String]] must beEqualTo(mutable.Queue("one", "two", "three"))
    }

    "returns an injectable for mutable.ResizeableArray instances" in {
      getValue[mutable.ResizableArray[String]] must beEqualTo(mutable.ResizableArray("one", "two", "three"))
    }

    "return an injectable for mutable.Set instances" in {
      getValue[mutable.Set[String]] must beEqualTo(mutable.Set("one", "two", "three"))
    }

    "returns an injectable for Seq instances" in {
      getValue[Seq[String]] must beEqualTo(Seq("one", "two", "three"))
    }

    "returns an injectable for Stream instances" in {
      getValue[Stream[String]] must beEqualTo(Stream("one", "two", "three"))
    }

    def getValue[T : Manifest]: AnyRef = {
      provider.getInjectable(context, queryParam, parameter[T]).asInstanceOf[ScalaCollectionQueryParamInjectable].getValue(httpContext)
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
