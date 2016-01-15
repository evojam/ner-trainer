package com.evojam.nlp

import java.io.FileInputStream

import epic.sequences.SemiCRF

trait NERTrainer {
  def train(dataSet: FileInputStream): SemiCRF[String, String]
}
