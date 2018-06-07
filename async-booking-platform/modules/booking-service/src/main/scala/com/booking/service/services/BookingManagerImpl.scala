package com.booking.service.services

import java.util.Date

import com.booking.service.domains.AddBooking
import com.booking.service.exceptions.RoomNotAvailableException
import com.booking.service.repositories.BookingRepository

class BookingManagerImpl(repository: BookingRepository) extends BookingManager {

  /**
    * Return true if there is no booking for the given room on the date,
    * otherwise false
    */
  override def isRoomAvailable(room: Int, date: Date): Boolean = {
    if (!repository.rooms.contains(room)) {
      throw RoomNotAvailableException("The room provided does not exist on our system.")
    }

    //!repository.bookings.contains(room) ||
    !repository.bookings.getOrElse(room, List.empty).exists(b => b.date.equals(date))
  }

  /**
    * Add a booking for the given guest in the given room on the given
    * date. If the room is not available, throw a suitable Exception.
    */
  override def addBooking(guest: String, room: Int, date: Date): Boolean = {
    if (isRoomAvailable(room, date)) {
      val booking = AddBooking(guest, room, date)

      val bookings = repository.bookings.get(room) match {
        case bookings: List[AddBooking] => bookings ++ List(booking)
        case _ => List(booking)
      }

      repository.bookings.put(room, bookings)
      true
    } else {
      throw RoomNotAvailableException("The room is not available for the given date. = " + repository.bookings)
    }
  }

  /**
    * Return a list of all the available room numbers for the given date
    */
  override def getAvailableRooms(date: Date): Seq[Int] = {
    val availableRooms = repository.rooms.clone()

    repository.bookings.foreach { case (room: Int, bookings: List[AddBooking]) =>
      if (bookings.exists(b => {
        b.date.equals(date)
      })) {
        availableRooms.remove(room)
      }
    }

    availableRooms.toSeq
  }

}
