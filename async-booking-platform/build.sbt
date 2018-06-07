version      := "1.0.0-SNAPSHOT"
scalaVersion := "2.12.6"
organization := "com.booking"

crossScalaVersions := Seq("2.11.12", "2.12.4")

resolvers += Resolver.sonatypeRepo("snapshots")

// -------------------------------------------------------------------------------------------------------------------
// Root Project
// -------------------------------------------------------------------------------------------------------------------
lazy val root = Project("async-booking-platform", file("."))
  .aggregate(bookingService, webService)
  .dependsOn(bookingService, webService)
  .settings(name := "booking-platform")  
  .settings(mainClass in (Compile, packageBin) := Some("com.booking.service.Application"))
  .settings(mainClass in (Compile, run) := Some("com.booking.service.Application"))   

// -------------------------------------------------------------------------------------------------------------------
// Booking Service
// -------------------------------------------------------------------------------------------------------------------
lazy val bookingService = Project("booking-service", file("modules/booking-service"))
  .settings(name := "booking-service")
  .settings(libraryDependencies ++= Seq(akkaActor, akkaTestKit, akkaSlf4j, scalaTest))
  
// -------------------------------------------------------------------------------------------------------------------
// Web Service
// -------------------------------------------------------------------------------------------------------------------
lazy val webService = Project("web-service", file("modules/web-service"))
  .aggregate(bookingService)
  .dependsOn(bookingService)
  .settings(name := "web-service")
  .settings(libraryDependencies ++= Seq(guice, h2Database, scalaTestPlus))
  .enablePlugins(PlayScala)

// Project Dependencies
val akkaVersion = "2.5.12"
val akkaActor = "com.typesafe.akka" %% "akka-actor" % akkaVersion
val akkaTestKit = "com.typesafe.akka" %% "akka-testkit" % akkaVersion
val akkaSlf4j = "com.typesafe.akka" %% "akka-slf4j" % akkaVersion
val h2Database = "com.h2database" % "h2" % "1.4.196"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5" % Test
val scalaTestPlus = "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test

