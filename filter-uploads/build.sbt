description := "Support for multi-part uploads for servlet filters"

unmanagedClasspath in (local("filter-uploads"), Test) <++=
  (fullClasspath in (local("spec"), Compile))

libraryDependencies <++= scalaVersion(v => Seq(
  Common.servletApiDep,
  "commons-fileupload" % "commons-fileupload" % "1.2.1"
) ++ Common.integrationTestDeps(v))
