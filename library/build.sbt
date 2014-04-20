description := "Core library for describing requests and responses"

unmanagedClasspath in (LocalProject("unfiltered"), Test) <++=
  (fullClasspath in (local("spec"), Compile),
   fullClasspath in (local("filter"), Compile)) map { (s, f) =>
    s ++ f
}

libraryDependencies <++= scalaVersion(v => Seq(
  "commons-codec" % "commons-codec" % "1.4",
  Common.specsDep(v) % "test"
))

libraryDependencies <<= (libraryDependencies, scalaVersion){
  (dependencies, scalaVersion) =>
  if(!(scalaVersion.startsWith("2.9") || scalaVersion.startsWith("2.10")))
    ("org.scala-lang.modules" %% "scala-xml" % "1.0.1") +: dependencies
  else
    dependencies
}
