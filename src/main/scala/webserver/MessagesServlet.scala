package webserver

import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json._
import org.scalatra._

import scala.collection.mutable.{HashMap, Map}


class MessagesServlet extends ScalatraServlet with JacksonJsonSupport {
  // Before every action runs, set the content type to be in JSON format.
  before() {
    contentType = formats("json")
  }

  get("/") {
    MessageStore.get_all()
  }

  get("/:id") {
    val id = params("id")
    MessageStore.get(id) match {
        case Some(msg) => Ok(msg)
        case None      => NotFound(Errors.NotFoundError(id))
    }
  }

  post("/") {
    MessageStore.put(parsedBody.extract[Msg])
  }

  put("/:id") {
    val id = params("id")
    if(MessageStore.contains(id)) {
      val incomplete_msg = parsedBody.extract[IncompleteMsg]
      val msg = Msg(params("id"), incomplete_msg.text)
      MessageStore.put(msg)
    } else {
      NotFound(Errors.NotFoundError(id))
    }
  }

  delete("/:id") {
    val id = params("id")
    if(MessageStore.contains(id)) {
      MessageStore.delete(params("id"))
    } else {
      NotFound(Errors.NotFoundError(id))
    }
  }

  // Sets up automatic case class to JSON output serialization, required by
  // the JValueResult trait.
  protected implicit lazy val jsonFormats: Formats = DefaultFormats
}

case class IncompleteMsg(text: String)
case class Msg(id: String, text: String)
case class Error(error: String)

object Errors {
  def NotFoundError(id: String): Error = {
    Error("Message with id " + id + " not found")
  }
}

object MessageStore {
  private var store: Map[String, Msg] = HashMap()

  def get_all() = {
    store.values
  }

  def get(id: String) = {
    store.get(id)
  }

  def contains(id: String): Boolean = {
    store.contains(id)
  }

  def put(message: Msg): Unit = {
    store.put(message.id, message)
  }

  def delete(id: String): Unit = {
    store.remove(id)
  }
}