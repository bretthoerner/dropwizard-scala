package com.massrelevance.dropwizard.scala.params.tests

import javax.ws.rs.WebApplicationException
import com.massrelevance.dropwizard.scala.params.IntParam
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class IntParamTest extends FlatSpec with ShouldMatchers {

  "A valid int parameter" should "have an int value" in {
    val param = IntParam("40")
    param.value should equal (40)
  }

  "An invalid int parameter" should "throws a WebApplicationException with an error message" in {
    val e = intercept[WebApplicationException] {
      IntParam("poop")
    }

    val response = e.getResponse
    response.getStatus should equal (400)
    response.getEntity should equal ("Invalid parameter: poop (Must be an integer value.)")
  }
}
