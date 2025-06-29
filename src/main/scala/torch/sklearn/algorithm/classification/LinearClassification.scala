// Storch - Linear Classification
// 2015-12-21

package torch.sklearn.algorithm.classification

import torch.sklearn.general._

class LinearClassification() extends Classification {
    val algoname: String = "LinearRegression"
    val version: String = "0.1"

    private def dot(x:Array[Double], y:Array[Double]): Double =
        arraymultiply(x, y).sum

    var projector = Array[(Int, Int, Array[Double], Array[Double])]()

    override def clear(): Boolean = {
        projector = Array[(Int, Int, Array[Double], Array[Double])]()
        true
    }

    override def config(paras: Map[String, Any]): Boolean = {
        true
    }

    // --- Start Linear Classification Function ---
    override def train(
        data: Array[(Int, Array[Double])]   // Data Array(yi, xi)
    ): Boolean = { // Return PData Class
        val centers = data.groupBy(_._1).map { l =>
            val datasize = l._2.size
            (l._1, matrixaccumulate(l._2.map(_._2)).map(_ / datasize))
        }
        centers.map { center1 =>
            centers.map { center2 =>
                if (center1._1 < center2._1) {
                    val m = arraysum(center1._2, center2._2).map(_ / 2)
                    var w = arrayminus(center2._2, center1._2)
                    if (w.sum > 1) w = w.map(_ / w.sum)
                    projector :+= (center1._1, center2._1, m, w)
                }
            }
        }
        true
    }
    
    // --- Dual Projection Linear Classification ---
    override def predict(
        data: Array[Array[Double]]
    ): Array[Int] = {
        return data.map { d =>
            projector.map(p =>
                if (dot(arrayminus(d, p._3), p._4) < 0) p._1 else p._2
            ).groupBy(identity).mapValues(_.size).maxBy(_._2)._1
        }
    }
}
