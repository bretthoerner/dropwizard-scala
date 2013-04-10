package com.yammer.dropwizard.scala.params.tests

import javax.ws.rs.WebApplicationException
import com.yammer.dropwizard.scala.params.LongParam
import org.specs2.mutable._
import org.specs2.mock.Mockito

class LongParamTest extends Specification with Mockito {

  "A valid long parameter" should {
    val param = LongParam("40")

    "has an int value" in {
      param.value.must(beEqualTo(40L))
    }
  }

  "An invalid long parameter" should {
    "throws a WebApplicationException with an error message" in {
      LongParam("poop").must(throwA[Exception].like {
        case e: WebApplicationException => {
          val response = e.getResponse
          response.getStatus.must(beEqualTo(400))
          response.getEntity.must(beEqualTo("Invalid parameter: poop (Must be an integer value.)"))
        }
      })
    }
  }

}
