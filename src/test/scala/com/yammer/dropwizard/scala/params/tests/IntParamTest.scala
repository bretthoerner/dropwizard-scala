package com.yammer.dropwizard.scala.params.tests

import javax.ws.rs.WebApplicationException
import com.yammer.dropwizard.scala.params.IntParam
import org.specs2.mutable._
import org.specs2.mock.Mockito

class IntParamTest extends Specification with Mockito {

  "A valid int parameter" should {
    val param = IntParam("40")

    "has an int value" in {
      param.value.must(beEqualTo(40))
    }
  }

  "An invalid int parameter" should {
    "throws a WebApplicationException with an error message" in {
      IntParam("poop").must(throwA[Exception].like {
        case e: WebApplicationException => {
          val response = e.getResponse
          response.getStatus.must(beEqualTo(400))
          response.getEntity.must(beEqualTo("Invalid parameter: poop (Must be an integer value.)"))
        }
      })
    }
  }
}
