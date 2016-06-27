package com.massrelevance.dropwizard.scala.inject.tests

import com.massrelevance.dropwizard.scala.inject.ScalaCollectionStringReaderExtractor
import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap
import org.scalatest.{FlatSpec, Matchers}


class ScalaCollectionExtractorTest extends FlatSpec with Matchers {
  val extractor = new ScalaCollectionStringReaderExtractor[Set]("name", "default", Set)

  "Extracting a parameter" should "have a name" in {
    extractor.getName should be("name")
  }

  it should "have a default value" in {
    extractor.getDefaultValueString should be("default")
  }

  it should "extract a set of parameter values" in {
    val params = new MultivaluedStringMap()
    params.add("name", "one")
    params.add("name", "two")
    params.add("name", "three")

    val result = extractor.extract(params)
    result should equal(Set("one", "two", "three"))
  }

  it should "use the default value if no parameter exists" in {
    val params = new MultivaluedStringMap()

    val result = extractor.extract(params)
    result should equal(Set("default"))
  }

  "Extracting a parameter with no default value" should "return an empty collection" in {
    val extractor = new ScalaCollectionStringReaderExtractor[Set]("name", null, Set)

    val params = new MultivaluedStringMap()

    val result = extractor.extract(params)
    result should equal(Set.empty[String])
  }
}
