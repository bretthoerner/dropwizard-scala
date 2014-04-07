package com.massrelevance.dropwizard.scala.params.tests

import javax.ws.rs.WebApplicationException
import com.massrelevance.dropwizard.scala.params.BooleanParam
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class BooleanParamTest extends FlatSpec with ShouldMatchers {

  "A valid boolean parameter" should "have a boolean value" in {
    val param = BooleanParam("true")
    param.value should equal (true)
  }

  "An invalid boolean parameter" should "throw a WebApplicationException with an error message" in {
    val e = intercept[WebApplicationException] {
      BooleanParam("poop")
    }

    val response = e.getResponse
    response.getStatus should equal (400)
    response.getEntity should equal ("Invalid parameter: poop (Must be \"true\" or \"false\".)")
  }
}
