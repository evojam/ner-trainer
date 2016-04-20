package com.evojam.util

import java.io._
import java.util.zip.{GZIPInputStream, GZIPOutputStream}

import scala.util.control.Exception.catching

import epic.sequences.SemiCRF

object IO {
  def read(filePath: String): Option[FileInputStream] =
    catching(classOf[IOException]).opt(
      new FileInputStream(filePath))

  private def writeObject(obj: AnyRef, oos: ObjectOutputStream) =
    try {
      oos.writeObject(obj)
    } finally {
      oos.close()
    }

  def write(obj: AnyRef, targetPath: String): Option[Unit] =
    catching(classOf[IOException])
      .opt(new FileOutputStream(targetPath))
      .map(fos =>
        new ObjectOutputStream(
          new BufferedOutputStream(
            new GZIPOutputStream(fos))))
      .flatMap(oos =>
        catching(classOf[IOException])
          .opt(writeObject(obj, oos)))

  def load(filePath: String): SemiCRF[String, String] = {
    val gzipin = breeze.util.nonstupidObjectInputStream(
      new BufferedInputStream(
        new GZIPInputStream(
          new FileInputStream(filePath)
        )
      )
    )

    try {
      gzipin.readObject().asInstanceOf[SemiCRF[String, String]]
    } finally {
      gzipin.close()
    }
  }
}
