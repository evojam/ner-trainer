package com.evojam.util

import java.io._

import scala.util.control.Exception.catching

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
          new BufferedOutputStream(fos)))
      .flatMap(oos =>
        catching(classOf[IOException])
          .opt(writeObject(obj, oos)))
}
