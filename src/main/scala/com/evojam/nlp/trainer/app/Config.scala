package com.evojam.nlp.trainer.app

import java.io.File

case class Config(in: File, out: File = new File("out.gz"))
