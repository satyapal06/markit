package com.booking.service

import java.text.SimpleDateFormat

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import com.booking.service.actors.BookingSupervisor
import com.booking.service.domains._
import com.booking.service.repositories.BookingRepository
import com.booking.service.services.BookingManagerImpl

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.io.StdIn
import scala.util.{Failure, Success, Try}

object BookingApp {

  def main(args: Array[String]): Unit = {

    val system = ActorSystem("booking-system")

    import system.dispatcher // The ExecutionContext that will be used

    implicit val timeout = Timeout(5 seconds) // needed for `?` below

    try {
      val repository = new BookingRepository()
      repository.rooms.add(101)
      repository.rooms.add(102)
      repository.rooms.add(201)
      repository.rooms.add(203)

      val date = new SimpleDateFormat("yyyy-MM-dd").parse("2012-03-28")

      val bm = new BookingManagerImpl(repository)

      // Create top level supervisor
      val supervisor = system.actorOf(BookingSupervisor.props(bm), "booking-supervisor")

      val f: Future[Any] =
        for {
          x <- ask(supervisor, IsRoomAvailable(101, date)).mapTo[HotelResponse]
          s <- ask(supervisor, AddBooking("Test", 101, date)).mapTo[HotelResponse]
          d <- ask(supervisor, AddBooking("Test", 101, date)).mapTo[HotelResponse]
        } yield (x, s, d)

      f.onComplete {
        case Success(status) => println(status)
        case Failure(message) => println(message)
      }

      // Exit the system after ENTER is pressed
      StdIn.readLine()
    } finally {
      system.terminate()
    }
  }

}
