package com.yammer.dropwizard.scala.inject.tests

import com.sun.jersey.core.util.MultivaluedMapImpl
import com.yammer.dropwizard.scala.inject.ScalaOptionStringExtractor
import org.specs2.mutable._
import org.specs2.mock.Mockito

class ScalaOptionStringExtractorTest extends Specification with Mockito {

   "Extracting a parameter" should {
    val extractor = new ScalaOptionStringExtractor("name", "default")

    "has a name" in {
      extractor.getName.must(be("name"))
    }

    "has a default value" in {
      extractor.getDefaultStringValue.must(be("default"))
    }

    "extracts the first of a set of parameter values" in {
      val params = new MultivaluedMapImpl()
      params.add("name", "one")
      params.add("name", "two")
      params.add("name", "three")

      val result = extractor.extract(params)
      result.must(beEqualTo(Some("one")))
    }

    "uses the default value if no parameter exists" in {
      val params = new MultivaluedMapImpl()

      val result = extractor.extract(params)
      result.must(beEqualTo(Some("default")))
    }
  }

  "Extracting a parameter with no default value" should {
    val extractor = new ScalaOptionStringExtractor("name", null)

    "returns None" in {
      val params = new MultivaluedMapImpl()

      val result = extractor.extract(params)
      result.must(beEqualTo(None))
    }
  }
}
