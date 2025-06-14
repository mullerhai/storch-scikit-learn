// Storch - Gaussian Process
// 2016-11-24

package torch.sklearn.algorithm.classification

import torch.sklearn.general._

class GaussianProcess() extends Classification {
    val algoname: String = "GaussianProcess"
    val version: String = "0.1"

    var pointGroups = Map[Int, Array[Array[Double]]]()
    var std: Double = 1.0 // Standard Deviation

    override def clear(): Boolean = {
        pointGroups = Map[Int, Array[Array[Double]]]()
        std = 1.0
        true
    }

    override def config(paras: Map[String, Any]): Boolean = try {
        std = paras.getOrElse("STANDARD_DEVIATION", paras.getOrElse("standard_deviation", paras.getOrElse("std", 1.0))).asInstanceOf[Double]
        true
    } catch { case e: Exception =>
        Console.err.println(e)
        false
    }

    private def prob(a1: Array[Double], a2:Array[Double], s: Double): Double =
        Math.exp(-arrayminussquare(a1, a2).sum / Math.pow(s, 2))
    
    override def train(tdata: Array[(Int, Array[Double])]): Boolean = {
        pointGroups = tdata.groupBy(_._1).map(l => (l._1, l._2.map(_._2)))
        true
    }

    override def predict(                   // Gaussian Process
        pdata: Array[Array[Double]],        // Data Array(xi)
    ): Array[Int] = {                       // Return PData Class
        return pdata.map { pd =>
            pointGroups.map { gdata =>
                (gdata._1, gdata._2.map(gd => prob(pd, gd, std)).sum)
            }.maxBy(_._2)._1            
        }
    }
}
