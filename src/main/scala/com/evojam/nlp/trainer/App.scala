package com.evojam.nlp.trainer

import java.io.File

import scala.io.StdIn

import epic.sequences.{Segmentation, SemiCRF}
import scopt.OptionParser

import com.evojam.nlp.trainer.ner.SimpleNERTrainer
import com.evojam.util.IO

object App {
  val parser = new OptionParser[Config]("ner-trainer") {
    head("ner-trainer", "0.1")

    opt[File]('i', "in") valueName("<input file>") action {
      (in, config) =>
        config.copy(in = Some(in))
    }

    opt[File]('o', "out") valueName("<output file>") action {
      (out, config) =>
        config.copy(out = Some(out))
    }

    opt[File]('l', "load") valueName("<load SemiCRF file>") action {
      (load, config) =>
        config.copy(load = Some(load))
    }
  }

  lazy val nerTrainer = new SimpleNERTrainer()

  def subsequence[T](seq: IndexedSeq[T], beg: Int, end: Int): IndexedSeq[T] =
    for (i <- beg until end) yield seq(i)

  def unpackSegmentation[L, W](segmentation: Segmentation[L, W]): IndexedSeq[(L, IndexedSeq[W])] =
    segmentation.segments map {
      case (label, span) =>
        (label, subsequence(segmentation.words, span.begin, span.end))
    }

  def format(unpacked: IndexedSeq[(String, IndexedSeq[String])]): String =
    unpacked.map {
      case (tag, subsequence) =>
        val str = subsequence.mkString(" ")
        s"[$tag: $str]"
    } mkString " "

  def interactive(semiCrf: SemiCRF[String, String]) {
    while(true) {
      println("\nEnter statement:")

      val in = StdIn.readLine()
      val tokenized = epic.preprocess.tokenize(in.toLowerCase)
      val bestSequence = semiCrf.bestSequence(tokenized)
      val unpacked = unpackSegmentation(bestSequence)

      println (format(unpacked))
    }
  }

  def main(args: Array[String]) {
    parser.parse(args, Config()).foreach { config =>
      config.load match {
        case Some(load) =>
          interactive(IO.load(load.getAbsolutePath))
        case None =>
          config.in
            .flatMap(in => IO.read(in.getAbsolutePath))
            .map(nerTrainer.train)
            .foreach(semiCrf => {
              config.out match {
                case Some(out) =>
                  IO.write(semiCrf, out.getAbsolutePath)
                case None =>
                  interactive(semiCrf)
              }
            })
      }
    }
  }
}
