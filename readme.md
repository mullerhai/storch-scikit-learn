# Storch-Scikit-Learn: Scala 3 Implementation of Scikit-Learn

## Overview
Storch-Scikit-Learn is a Scala 3 implementation inspired by Python's scikit-learn. It provides seamless interoperability with Python, allowing users to read and write common data formats such as `.npz`, `.npy`, `.pkl`, `.pt`, `.pth`, and `.hdf5`. Additionally, it supports common `pickle` function operations, with plans to expand functionality in the future.

## Features
- **Scala 3 Compatibility**: Built with the latest Scala 3 features for improved performance and developer experience.
- **Python Interoperability**: Exchange data with Python easily, leveraging the power of both ecosystems.
- **Multi-Format Support**: Read and write data in `.npz`, `.npy`, `.pkl`, `.pt`, `.pth`, and `.hdf5` formats.
- **Pickle Operations**: Support for common `pickle` functions to serialize and deserialize data.

## Installation
To use Storch-Scikit-Learn in your project, add the following dependencies to your `build.sbt` file:

```scala:build.sbt
sbt compile
libraryDependencies += "io.github.mullerhai" % "storch-scikit-learn_3" % "0.1.0"


````