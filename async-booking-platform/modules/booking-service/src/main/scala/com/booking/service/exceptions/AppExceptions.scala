package com.booking.service.exceptions

case class RoomNotAvailableException(msg: String) extends Exception(msg)
