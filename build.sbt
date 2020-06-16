name := """test-project"""
organization := "com.ranbhr.test"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.2"

libraryDependencies += guice
libraryDependencies += javaJpa
libraryDependencies += "org.hibernate" % "hibernate-core" % "5.4.9.Final" // replace by your jpa implementation
libraryDependencies += "org.postgresql" % "postgresql" % "42.2.12"
libraryDependencies += javaWs
libraryDependencies += "org.mockito" % "mockito-core" % "2.10.0" % "test"

// Java project. Don't expect Scala IDE
EclipseKeys.projectFlavor := EclipseProjectFlavor.Java

// Use .class files instead of generated .scala files for views and routes
EclipseKeys.createSrc := EclipseCreateSrc.ValueSet(EclipseCreateSrc.ManagedClasses, EclipseCreateSrc.ManagedResources)


// to work in prod else, FileNotFoundException for persistence.xml (as persistence.xml lies outside jar)
PlayKeys.externalizeResourcesExcludes += baseDirectory.value / "conf" / "META-INF" / "persistence.xml"