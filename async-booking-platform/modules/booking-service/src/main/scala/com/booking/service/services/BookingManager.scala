package com.booking.service.services

import java.util.Date

trait BookingManager {

  /**
    * Return true if there is no booking for the given room on the date,
    * otherwise false
    */
  def isRoomAvailable(room: Int, date: Date): Boolean

  /**
    * Add a booking for the given guest in the given room on the given
    * date. If the room is not available, throw a suitable Exception.
    */
  def addBooking(guest: String, room: Int, date: Date): Boolean

  /**
    * Return a list of all the available room numbers for the given date
    */
  def getAvailableRooms(date: Date): Seq[Int]

}
