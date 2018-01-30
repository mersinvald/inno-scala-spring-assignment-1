package webserver

import org.scalatra.test.scalatest._

class MessagesServletTests extends ScalatraFunSuite {

  addServlet(classOf[MessagesServlet], "/*")

  test("GET / on MessagesServlet should return status 200"){
    get("/"){
      status should equal (200)
    }
  }

}
