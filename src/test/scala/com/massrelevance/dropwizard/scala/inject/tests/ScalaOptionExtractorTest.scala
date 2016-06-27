package com.massrelevance.dropwizard.scala.inject.tests

import com.massrelevance.dropwizard.scala.inject.ScalaOptionStringExtractor
import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap
import org.scalatest.{FlatSpec, Matchers}

class ScalaOptionExtractorTest extends FlatSpec with Matchers {
  val extractor = new ScalaOptionStringExtractor("name", "default")

  "Extracting a parameter" should "have a name" in {
    extractor.getName should be("name")
  }

  it should "have a default value" in {
    extractor.getDefaultValueString should be("default")
  }

  it should "extract the first of a set of parameter values" in {
    val params = new MultivaluedStringMap()
    params.add("name", "one")
    params.add("name", "two")
    params.add("name", "three")

    val result = extractor.extract(params)
    result should equal(Some("one"))
  }

  it should "use the default value if no parameter exists" in {
    val params = new MultivaluedStringMap()

    val result = extractor.extract(params)
    result should equal(Some("default"))
  }

  "Extracting a parameter with no default value" should "return None" in {
    val extractor = new ScalaOptionStringExtractor("name", null)

    val params = new MultivaluedStringMap()

    val result = extractor.extract(params)
    result should equal(None)
  }
}
