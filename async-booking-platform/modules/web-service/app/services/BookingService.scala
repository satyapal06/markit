package services

import java.util.Date

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import com.booking.service.actors.BookingSupervisor
import com.booking.service.domains.{AddBooking, GetAvailableRooms, HotelResponse, IsRoomAvailable}
import com.booking.service.repositories.BookingRepository
import com.booking.service.services.{BookingManager, BookingManagerImpl}
import javax.inject.{Inject, Singleton}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class BookingService @Inject()(actorSystem: ActorSystem)(implicit exec: ExecutionContext) {

  private val repository = initRooms()
  private val manager: BookingManager = new BookingManagerImpl(repository)

  val supervisor = actorSystem.actorOf(BookingSupervisor.props(manager), "booking-supervisor")

  implicit val timeout = Timeout(5 seconds)

  def getAvailableRoomDetails(date: Date): Future[HotelResponse] = {
    ask(supervisor, GetAvailableRooms(date)).mapTo[HotelResponse]
  }

  def createBooking(guest: String, room: Int, date: Date): Future[HotelResponse] = {
    ask(supervisor, AddBooking(guest, room, date)).mapTo[HotelResponse]
  }

  def checkRoomAvailability(room: Int, date: Date): Future[HotelResponse] = {
    ask(supervisor, IsRoomAvailable(room, date)).mapTo[HotelResponse]
  }

  def initRooms(): BookingRepository = {
    val repository = new BookingRepository()
    repository.rooms.add(101)
    repository.rooms.add(102)
    repository.rooms.add(201)
    repository.rooms.add(202)
    repository
  }


}
