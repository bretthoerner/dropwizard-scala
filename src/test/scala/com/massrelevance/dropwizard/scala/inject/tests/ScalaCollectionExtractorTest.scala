package com.massrelevance.dropwizard.scala.inject.tests

import com.sun.jersey.core.util.MultivaluedMapImpl
import com.massrelevance.dropwizard.scala.inject.ScalaCollectionExtractor
import com.massrelevance.dropwizard.scala.util.StringExtractor
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class ScalaCollectionExtractorTest extends FlatSpec with ShouldMatchers {
  val extractor = new ScalaCollectionExtractor(Set, new StringExtractor("name", "default"))

  "Extracting a parameter" should "have a name" in {
    extractor.getName should  be ("name")
  }

  it should "have a default value" in {
    extractor.getDefaultStringValue should be ("default")
  }

  it should "extract a set of parameter values" in {
    val params = new MultivaluedMapImpl()
    params.add("name", "one")
    params.add("name", "two")
    params.add("name", "three")

    val result = extractor.extract(params)
    result should equal (Set("one", "two", "three"))
  }

  it should "use the default value if no parameter exists" in {
    val params = new MultivaluedMapImpl()

    val result = extractor.extract(params)
    result should equal (Set("default"))
  }

  "Extracting a parameter with no default value" should "return an empty collection" in {
    val extractor = new ScalaCollectionExtractor(Set, new StringExtractor("name"))

    val params = new MultivaluedMapImpl()

    val result = extractor.extract(params)
    result should equal (Set.empty[String])
  }
}
