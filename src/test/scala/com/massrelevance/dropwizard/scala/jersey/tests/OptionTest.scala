package com.massrelevance.dropwizard.scala.jersey.tests

import javax.ws.rs.core.{Application, MediaType}
import javax.ws.rs.{GET, Path, Produces, QueryParam}

import com.massrelevance.dropwizard.scala.inject.ScalaCollectionsQueryParamFactoryProvider
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.test.JerseyTest
import org.junit.{Test, Assert}

class OptionTest extends JerseyTest {

  @Path("/")
  @Produces(Array(MediaType.APPLICATION_JSON))
  class ExampleResource {
    @Path("/opt/")
    @GET
    def none(@QueryParam("id") id: Option[String]): Boolean = {
      id.isEmpty
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

  override def configure(): Application = {
    val config = new ResourceConfig()
    config.register(classOf[ScalaCollectionsQueryParamFactoryProvider])
    config.register(classOf[ExampleResource])
    config
  }

  @Test
  def injectsNoneInsteadOfNull() {
    Assert.assertTrue(target("/opt/").request.get().getEntity == Boolean.getClass)
  }

  @Test
  def injectsEmptyString() {
    Assert.assertEquals("", client().target("/string/").queryParam("id", "").request().get(classOf[String]))
  }

  @Test
  def injectsString() {
    Assert.assertEquals("id", client().target("/string/").queryParam("id", "id").request().get(classOf[String]))
  }

  @Test
  def injectsInt() {
    Assert.assertEquals(200, client().target("/int/").queryParam("id", "200").request().get(classOf[Int]))
  }

  @Test
  def injectsLong() {
    Assert.assertEquals(200L, client().target("/long/").queryParam("id", "200").request().get(classOf[Long]))
  }

  @Test
  def injectsBooleanTrue() {
    Assert.assertTrue(client().target("/boolean/").queryParam("id", "true").request().get(classOf[Boolean]))
  }

  @Test
  def injectsBooleanFalse() {
    Assert.assertFalse(client().target("/boolean/").queryParam("id", "false").request().get(classOf[Boolean]))
  }
}
