package com.booking.service.domains

import java.util.Date

class AppDomains

trait HotelRequest

trait HotelResponse

trait IsRoomAvailableResponse extends HotelResponse

trait AddBookingResponse extends HotelResponse

trait AvailableRoomsResponse extends HotelResponse

final case class ErrorOccurred() extends HotelResponse

final case class ErrorResponse() extends HotelResponse

final case class IsRoomAvailable(room: Int, date: Date) extends HotelRequest

final case class RoomAvailable() extends IsRoomAvailableResponse

final case class NoRoomAvailable() extends IsRoomAvailableResponse

final case class AddBooking(guest: String, room: Int, date: Date) extends HotelRequest

final case class BookingMade() extends AddBookingResponse

final case class GetAvailableRooms(date: Date) extends HotelRequest

final case class AvailableRooms(availableRoomNumbers: Seq[Int]) extends AvailableRoomsResponse