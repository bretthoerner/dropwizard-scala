package com.yammer.dropwizard.scala.params.tests

import javax.ws.rs.WebApplicationException
import com.yammer.dropwizard.scala.params.BooleanParam
import org.specs2.mutable._
import org.specs2.mock.Mockito

class BooleanParamTest extends Specification with Mockito {

  "A valid boolean parameter" should {
    val param = BooleanParam("true")

    "has a boolean value" in {
      param.value.must(beEqualTo(true))
    }
  }

  "An invalid boolean parameter" should {
    "throws a WebApplicationException with an error message" in {
      BooleanParam("poop").must(throwA[Exception].like {
        case e: WebApplicationException => {
          val response = e.getResponse
          response.getStatus.must(beEqualTo(400))
          response.getEntity.must(beEqualTo("Invalid parameter: poop (Must be \"true\" or \"false\".)"))
        }
      })
    }
  }
}
