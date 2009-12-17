package org.prohax.scaml.views

import scala.xml._
import org.prohax.scaml.ScamlFile

object empty extends ScamlFile {
  def render() = {
    <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
  }
}