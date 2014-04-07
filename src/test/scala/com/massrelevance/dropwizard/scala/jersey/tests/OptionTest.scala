package com.massrelevance.dropwizard.scala.jersey.tests

import com.sun.jersey.test.framework.{LowLevelAppDescriptor, AppDescriptor, JerseyTest}
import javax.ws.rs.{Path, Produces, GET, QueryParam}
import javax.ws.rs.core.MediaType
import io.dropwizard.jersey.DropwizardResourceConfig
import junit.framework.Assert
import org.junit.Test
import com.massrelevance.dropwizard.scala.inject.ScalaCollectionsQueryParamInjectableProvider
import com.codahale.metrics.MetricRegistry

class OptionTest extends JerseyTest {
  @Path("/")
  @Produces(Array(MediaType.APPLICATION_JSON))
  class ExampleResource {
    @Path("/opt/")
    @GET
    def none(@QueryParam("id") id: Option[String]): Boolean = {
      id == None
    }

    @Path("/string/")
    @GET
    def string(@QueryParam("id") id: Option[String]): String = {
      Assert.assertTrue(id.get.isInstanceOf[String])
      id.get
    }

    @Path("/int/")
    @GET
    def int(@QueryParam("id") id: Option[java.lang.Integer]): Int = {
      Assert.assertTrue(id.get.isInstanceOf[java.lang.Integer])
      id.get
    }

    @Path("/long/")
    @GET
    def long(@QueryParam("id") id: Option[java.lang.Long]): Long = {
      Assert.assertTrue(id.get.isInstanceOf[java.lang.Long])
      id.get
    }

    @Path("/boolean/")
    @GET
    def boolean(@QueryParam("id") id: Option[java.lang.Boolean]): Boolean = {
      Assert.assertTrue(id.get.isInstanceOf[java.lang.Boolean])
      id.get
    }
  }

  override def configure(): AppDescriptor = {
    val config = DropwizardResourceConfig.forTesting(new MetricRegistry)
    config.getClasses.add(classOf[ScalaCollectionsQueryParamInjectableProvider])
    config.getSingletons.add(new ExampleResource())
    new LowLevelAppDescriptor.Builder(config).build()
  }

  @Test
  def injectsNoneInsteadOfNull() {
    Assert.assertTrue(client().resource("/opt/").get(classOf[Boolean]))
  }

  @Test
  def injectsEmptyString() {
    Assert.assertEquals("", client().resource("/string/").queryParam("id", "").get(classOf[String]))
  }

  @Test
  def injectsString() {
    Assert.assertEquals("id", client().resource("/string/").queryParam("id", "id").get(classOf[String]))
  }

  @Test
  def injectsInt() {
    Assert.assertEquals(200, client().resource("/int/").queryParam("id", "200").get(classOf[Int]))
  }

  @Test
  def injectsLong() {
    Assert.assertEquals(200L, client().resource("/long/").queryParam("id", "200").get(classOf[Long]))
  }

  @Test
  def injectsBooleanTrue() {
    Assert.assertTrue(client().resource("/boolean/").queryParam("id", "true").get(classOf[Boolean]))
  }

  @Test
  def injectsBooleanFalse() {
    Assert.assertFalse(client().resource("/boolean/").queryParam("id", "false").get(classOf[Boolean]))
  }
}
