package org.prohax.scaml

class Scaml(projectImports: List[String]) {
  val commonImports = "org.prohax.scaml.Helpers._" :: projectImports
}