name := """hello-play"""
version := "1.0-SNAPSHOT"
lazy val root = (project in file(".")).enablePlugins(PlayScala)
resolvers += Resolver.sonatypeRepo("snapshots")
scalaVersion := "2.12.8"
libraryDependencies += guice
scalacOptions ++= Seq("-feature", "-deprecation", "-Xfatal-warnings")