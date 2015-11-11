package com.massrelevance.dropwizard.scala.inject.tests

import javax.ws.rs.core.MultivaluedMap

import com.massrelevance.dropwizard.scala.inject.ScalaCollectionQueryParamInjectable
import com.sun.jersey.api.core.{ExtendedUriInfo, HttpContext}
import com.sun.jersey.core.util.MultivaluedMapImpl
import com.sun.jersey.server.impl.model.parameter.multivalued.MultivaluedParameterExtractor
import org.mockito.Matchers.{any, eq => meq}
import org.mockito.Mockito._
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import org.scalatest.{Matchers, FlatSpec}

class ScalaCollectionQueryParamInjectableTest extends FlatSpec with Matchers {
  // TODO: Aug 17, 2010 <coda> -- test error handling

  val extractor = mock(classOf[MultivaluedParameterExtractor])
  val context = mock(classOf[HttpContext])
  val uriInfo = mock(classOf[ExtendedUriInfo])
  val params = new MultivaluedMapImpl()
  val extracted = mock(classOf[Object])

  when(extractor.extract(any(classOf[MultivaluedMap[String, String]]))).thenAnswer(new Answer[AnyRef] {
    def answer(invocation: InvocationOnMock): AnyRef = extracted
  })

  when(context.getUriInfo).thenReturn(uriInfo)

  "A Scala collection query param injectable with decoding" should "extract the query parameters" in {
    val injectable = new ScalaCollectionQueryParamInjectable(extractor, true)
    when(uriInfo.getQueryParameters(meq(true))).thenReturn(params)

    injectable.getValue(context) should be(extracted)
  }

  "A Scala collection query param injectable without decoding" should "extract the query parameters" in {
    val injectable = new ScalaCollectionQueryParamInjectable(extractor, false)
    when(uriInfo.getQueryParameters(meq(false))).thenReturn(params)

    injectable.getValue(context) should be(extracted)
  }
}
