package com.booking.service.actors

import java.util.Date

import akka.actor.{Actor, ActorLogging, Props}
import com.booking.service.domains._
import com.booking.service.exceptions.RoomNotAvailableException
import com.booking.service.services.BookingManager

object BookingSupervisor {

  def props(bookingManager: BookingManager): Props = Props(new BookingSupervisor(bookingManager))

}

class BookingSupervisor(bookingManager: BookingManager) extends Actor with ActorLogging {

  override def preStart(): Unit = log.info("Booking Service Application started")

  override def postStop(): Unit = log.info("Booking Service  Application stopped")


  override def receive: Receive = {
    case IsRoomAvailable(room, date) => sender() ! handleIsRoomAvailableMessage(room, date)
    case AddBooking(guest, room, date) => sender() ! handleAddBookingMessage(guest, room, date)
    case GetAvailableRooms(date) => sender() ! handleGetAvailableRoomsMessage(date)
  }

  def handleIsRoomAvailableMessage(room: Int, date: Date): HotelResponse = {
    log.info("IsRoomAvailableMessage room: {} and date: {}", room, date)
    try {
      if (bookingManager.isRoomAvailable(room, date)) {
        RoomAvailable()
      } else {
        NoRoomAvailable()
      }
    } catch {
      case ex: RoomNotAvailableException => {
        log.info("IsRoomAvailableMessage : {}", ex.getMessage)
        NoRoomAvailable()
      }
    }
  }

  def handleAddBookingMessage(guest: String, room: Int, date: Date): HotelResponse = {
    log.info("AddBookingMessage room: {} and date: {}", room, date)
    try {
      if (bookingManager.addBooking(guest, room, date)) {
        BookingMade()
      } else {
        ErrorOccurred()
      }
    } catch {
      case ex: RoomNotAvailableException => {
        log.info("AddBookingMessage : {}", ex.getMessage)
        ErrorOccurred()
      }
    }
  }

  def handleGetAvailableRoomsMessage(date: Date): HotelResponse = {
    log.info("GetAvailableRoomsMessage - date: {}", date)
    try {
      AvailableRooms(bookingManager.getAvailableRooms(date))
    } catch {
      case ex: RoomNotAvailableException => {
        log.info("GetAvailableRoomsMessage : {}", ex.getMessage)
        ErrorOccurred()
      }
    }
  }


}
