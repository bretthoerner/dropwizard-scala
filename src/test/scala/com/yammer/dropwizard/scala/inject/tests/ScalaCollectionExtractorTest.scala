package com.yammer.dropwizard.scala.inject.tests

import com.sun.jersey.core.util.MultivaluedMapImpl
import com.yammer.dropwizard.scala.inject.ScalaCollectionExtractor
import org.specs2.mutable._
import org.specs2.mock.Mockito
import com.yammer.dropwizard.scala.util.StringExtractor

class ScalaCollectionExtractorTest extends Specification with Mockito {

  "Extracting a parameter" should {
    val extractor = new ScalaCollectionExtractor(Set, new StringExtractor("name", "default"))

    "has a name" in {
      extractor.getName.must(be("name"))
    }

    "has a default value" in {
      extractor.getDefaultStringValue.must(be("default"))
    }

    "extracts a set of parameter values" in {
      val params = new MultivaluedMapImpl()
      params.add("name", "one")
      params.add("name", "two")
      params.add("name", "three")

      val result = extractor.extract(params)
      result.must(beEqualTo(Set("one", "two", "three")))
    }

    "uses the default value if no parameter exists" in {
      val params = new MultivaluedMapImpl()

      val result = extractor.extract(params)
      result.must(beEqualTo(Set("default")))
    }
  }

  "Extracting a parameter with no default value" should {
    val extractor = new ScalaCollectionExtractor(Set, new StringExtractor("name"))

    "returns an empty collection" in {
      val params = new MultivaluedMapImpl()

      val result = extractor.extract(params)
      result.must(beEqualTo(Set.empty[String]))
    }
  }

}
