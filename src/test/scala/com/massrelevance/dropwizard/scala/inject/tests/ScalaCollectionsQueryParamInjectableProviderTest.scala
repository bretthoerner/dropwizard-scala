package com.massrelevance.dropwizard.scala.inject.tests

import javax.ws.rs.QueryParam
import com.sun.jersey.api.model.Parameter
import com.sun.jersey.core.spi.component.{ProviderServices, ComponentContext, ComponentScope}
import com.sun.jersey.core.util.MultivaluedMapImpl
import com.sun.jersey.api.core.{HttpContext, ExtendedUriInfo}
import com.massrelevance.dropwizard.scala.inject.{ScalaCollectionQueryParamInjectable, ScalaCollectionsQueryParamInjectableProvider}
import java.lang.reflect.{ParameterizedType, Type}
import collection.{immutable, mutable}
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FlatSpec
import org.mockito.Mockito._
import org.mockito.Matchers._

class ScalaCollectionsQueryParamInjectableProviderTest extends FlatSpec with ShouldMatchers {
  val httpContext = mock(classOf[HttpContext])
  val uriInfo = mock(classOf[ExtendedUriInfo])
  val params = new MultivaluedMapImpl()
  params.add("name", "one")
  params.add("name", "two")
  params.add("name", "three")

  when(httpContext.getUriInfo).thenReturn(uriInfo)
  when(uriInfo.getQueryParameters(any(classOf[Boolean]))).thenReturn(params)

  val context = mock(classOf[ComponentContext])
  val queryParam = mock(classOf[QueryParam])

  val provider = new ScalaCollectionsQueryParamInjectableProvider(mock(classOf[ProviderServices]))

  "A Scala collections query param injectable provider" should "have a per-request scope" in {
    provider.getScope should be(ComponentScope.PerRequest)
  }

  it should "return an injectable for Option instances" in {
    getValue[Option[String]] should equal (Some("one"))
  }

  it should "return an injectable for immutable.HashSet instances" in {
    getValue[immutable.HashSet[String]] should equal (immutable.HashSet("one", "two", "three"))
  }

  it should "return an injectable for List instances" in {
    getValue[List[String]] should equal (List("one", "two", "three"))
  }

  it should "return an injectable for ListSet instances" in {
    getValue[immutable.ListSet[String]] should equal (immutable.ListSet("one", "two", "three"))
  }

  it should "return an injectable for immutable.Queue instances" in {
    getValue[immutable.Queue[String]] should equal (immutable.Queue("one", "two", "three"))
  }

  it should "return an injectable for immutable.Set instances" in {
    getValue[immutable.Set[String]] should equal (immutable.Set("one", "two", "three"))
  }

  it should "return an injectable for immutable.Vector instances" in {
    getValue[immutable.Vector[String]] should equal (immutable.Vector("one", "two", "three"))
  }

  it should "return an injectable for IndexedSeq instances" in {
    getValue[IndexedSeq[String]] should equal (IndexedSeq("one", "two", "three"))
  }

  it should "return an injectable for mutable.ArraySeq instances" in {
    getValue[mutable.ArraySeq[String]] should equal (mutable.ArraySeq("one", "two", "three"))
  }

  it should "return an injectable for mutable.Buffer instances" in {
    getValue[mutable.Buffer[String]] should equal (mutable.Buffer("one", "two", "three"))
  }

  it should "return an injectable for mutable.HashSet instances" in {
    getValue[mutable.HashSet[String]] should equal (mutable.HashSet("one", "two", "three"))
  }

  it should "return an injectable for mutable.IndexedSeq instances" in {
    getValue[mutable.IndexedSeq[String]] should equal (mutable.IndexedSeq("one", "two", "three"))
  }

  it should "return an injectable for mutable.LinearSeq instances" in {
    getValue[mutable.LinearSeq[String]] should equal (mutable.LinearSeq("one", "two", "three"))
  }

  it should "return an injectable for mutable.LinkedHashSet instances" in {
    getValue[mutable.LinkedHashSet[String]] should equal (mutable.LinkedHashSet("one", "two", "three"))
  }

  it should "return an injectable for mutable.ListBuffer instances" in {
    getValue[mutable.ListBuffer[String]] should equal (mutable.ListBuffer("one", "two", "three"))
  }

  it should "return an injectable for mutable.MutableList instances" in {
    getValue[mutable.MutableList[String]] should equal (mutable.MutableList("one", "two", "three"))
  }

  it should "return an injectable for mutable.Queue instances" in {
    getValue[mutable.Queue[String]] should equal (mutable.Queue("one", "two", "three"))
  }

  it should "return an injectable for mutable.ResizeableArray instances" in {
    getValue[mutable.ResizableArray[String]] should equal (mutable.ResizableArray("one", "two", "three"))
  }

  it should "return an injectable for mutable.Set instances" in {
    getValue[mutable.Set[String]] should equal (mutable.Set("one", "two", "three"))
  }

  it should "return an injectable for Seq instances" in {
    getValue[Seq[String]] should equal (Seq("one", "two", "three"))
  }

  it should "return an injectable for Stream instances" in {
    getValue[Stream[String]] should equal (Stream("one", "two", "three"))
  }

  def getValue[T : Manifest]: AnyRef = {
    provider.getInjectable(context, queryParam, parameter[T]).asInstanceOf[ScalaCollectionQueryParamInjectable].getValue(httpContext)
  }

  private[this] def parameter[T : Manifest]: Parameter = {
    new Parameter(Array(), null, null, "name", typeFromManifest(manifest[T]), manifest[T].erasure, false, "default")
  }

  private[this] def typeFromManifest(m: Manifest[_]): Type = {
    if (m.typeArguments.isEmpty) {m.erasure}
    else {
      new ParameterizedType {
        def getRawType = m.erasure

        def getActualTypeArguments = m.typeArguments.map(typeFromManifest).toArray

        def getOwnerType = null
      }
    }
  }
}
