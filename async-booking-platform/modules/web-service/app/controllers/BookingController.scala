package controllers

import java.text.SimpleDateFormat
import java.util.Date

import akka.actor.ActorSystem
import javax.inject._
import play.api.libs.json.Json
import play.api.mvc._
import services.BookingService

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future, Promise}

/**
  * This controller creates an `Action` that demonstrates how to write
  * simple asynchronous code in a controller. It uses a timer to
  * asynchronously delay sending a response for 1 second.
  *
  * @param cc          standard controller components
  * @param actorSystem We need the `ActorSystem`'s `Scheduler` to
  *                    run code after a delay.
  * @param exec        We need an `ExecutionContext` to execute our
  *                    asynchronous code.  When rendering content, you should use Play's
  *                    default execution context, which is dependency injected.  If you are
  *                    using blocking operations, such as database or network access, then you should
  *                    use a different custom execution context that has a thread pool configured for
  *                    a blocking API.
  */
@Singleton
class BookingController @Inject()
(
  cc: ControllerComponents,
  actorSystem: ActorSystem,
  service: BookingService
)(implicit exec: ExecutionContext)
  extends AbstractController(cc) {

  /**
    * Return Json if there is no booking for the given room on the date,
    * otherwise
    * @param dateStr
    * @return
    */
  def rooms(dateStr: String) = Action.async {
    val date = parseDateString(dateStr)
    service.getAvailableRoomDetails(date) map { result => Ok(Json.toJson(result.toString))}
  }

  def availability(room: Int, dateStr: String) = Action.async {
    val date = parseDateString(dateStr)
    service.checkRoomAvailability(room, date) map { result => Ok(Json.toJson(result.toString))}
  }

  def create(guest: String, room: Int, dateStr: String) = Action.async {
    val date = parseDateString(dateStr)
    service.createBooking(guest, room, date) map { result => Ok(Json.toJson(result.toString))}
  }

  private def parseDateString(dateStr: String): Date = {
    new SimpleDateFormat("yyyy-MM-dd").parse(dateStr)
  }

}
