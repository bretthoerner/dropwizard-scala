package com.massrelevance.dropwizard.scala.params.tests

import javax.ws.rs.WebApplicationException

import com.massrelevance.dropwizard.scala.params.LongParam
import org.scalatest.{Matchers, FlatSpec}

class LongParamTest extends FlatSpec with Matchers {

  "A valid long parameter" should "have an int value" in {
    val param = LongParam("40")
    param.value should equal(40L)
  }

  "An invalid long parameter" should "throw a WebApplicationException with an error message" in {
    val e = intercept[WebApplicationException] {
      LongParam("poop")
    }

    val response = e.getResponse
    response.getStatus should equal(400)
    response.getEntity should equal("Invalid parameter: poop (Must be an integer value.)")
  }
}
