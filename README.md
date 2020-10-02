# Biosynth-custom

A custom reimplementation of the Biosynth framework available at https://github.com/Fxe/biosynth-framework/. The goal of this project is to apply an ETL methodology to reaction and metabolite data from KEGG, Bigg, MetaCyc and ModelSeed and load it into a Neo4j graph database.

## Getting Started

The biosynth-custom zip file contains a runnable JAR file containing all source code written in this project and two JSON files from ModelSeed listing all reactions and metabolites. To run the JAR file three arguments are necessary:

1 - Complete path to the Neo4j database that will be created from the execution of the JAR

2 - Complete path to the modelSeedCompounds.json file (available in the biosynth-custom zip file)

3 - Complete path to the modelSeedReactions.json file (available in the biosynth-custom zip file)

### Prerequisites

This project requires at least JRE 8. Source code was written within a Maven project.

