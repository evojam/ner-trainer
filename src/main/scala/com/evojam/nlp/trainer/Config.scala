package com.evojam.nlp.trainer

import java.io.File

case class Config(
  in: Option[File] = None,
  out: Option[File] = None,
  load: Option[File] = None) {

  require(in != null, "in cannot be null")
  require(out != null, "out cannot be null")
}
