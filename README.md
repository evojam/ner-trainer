# ner-trainer

Prepare package:

    sbt universal:packageBin
    
Unzip package:

    unzip ./target/universal/ner-trainer-VERSION.zip
    
Run:

    ./ner-trainer-VERSION/bin/ner-trainer [options]

Usage:

    ner-trainer [options]
      -i <input file> | --in <input file>
      -o <output file> | --out <output file>
      -l <load SemiCRF file> | --load <load SemiCRF file>
