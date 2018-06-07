package com.booking.service

import java.text.SimpleDateFormat

import com.booking.service.repositories.BookingRepository
import com.booking.service.services.BookingManagerImpl

object Application extends App {

  println("Hello Booking Service")

  val repository = new BookingRepository()
  repository.rooms.add(101)
  repository.rooms.add(102)
  repository.rooms.add(201)
  repository.rooms.add(203)

  val date = new SimpleDateFormat("yyyy-MM-dd").parse("2012-03-28")

  val bm = new BookingManagerImpl(repository)
  println(bm.isRoomAvailable(101, date))
  bm.addBooking("Smith", 101, date)
  println(bm.isRoomAvailable(101, date))
  bm.addBooking("Jones", 101, date)

}
