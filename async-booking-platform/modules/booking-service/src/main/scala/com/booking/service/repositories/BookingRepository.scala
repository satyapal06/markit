package com.booking.service.repositories

import com.booking.service.domains.AddBooking

import scala.collection.mutable

class BookingRepository {

  val rooms: mutable.Set[Int] = mutable.Set.empty

  val bookings: mutable.Map[Int, List[AddBooking]] = mutable.Map()

}
