package org.prohax.scaml.output

import scala.xml._
import org.prohax.scaml.ScamlFile

object doctype extends ScamlFile {
  override def headers = """<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">"""

  def renderXml() = {
    Text("")
  }
}