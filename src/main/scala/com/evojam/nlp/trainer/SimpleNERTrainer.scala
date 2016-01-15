package com.evojam.nlp.trainer

import java.io.FileInputStream

import epic.corpora.CONLLSequenceReader
import epic.sequences.{Segmentation, SemiCRF}
import epic.trees.Span
import nak.data.Example

import com.evojam.util.IO

class SimpleNERTrainer extends NERTrainer {
  private def segmentation(
    ex: Example[IndexedSeq[String], IndexedSeq[IndexedSeq[String]]]): Segmentation[Any, String] = {
    val segments = ex.label.foldLeft(List.empty[(String, Int, Int)]) {
      case (acc, label) => acc match {
        case head :: tail => head match {
          case (`label`, beg, end) => (label, beg, end + 1) :: tail
          case (nextLabel, beg, end) => (label, end, end + 1) :: head :: tail
        }
        case Nil => List((label, 0, 1))
      }
    }

    val segmentsSeq = segments
      .reverse.map {
        case (label, beg, end) => (label, Span(beg, end))
      }
      .toIndexedSeq

    Segmentation(segmentsSeq, ex.features.map(_.mkString), ex.id)
  }

  def train(dataSet: FileInputStream): SemiCRF[String, String] = {
    val train =
      CONLLSequenceReader
        .readTrain(dataSet)
        .toIndexedSeq
        .map(segmentation)
    SemiCRF
      .buildSimple(train)
      .asInstanceOf[SemiCRF[String, String]]
  }
}
