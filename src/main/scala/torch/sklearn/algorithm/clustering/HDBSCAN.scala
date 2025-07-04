// Storch - HDBSCAN
// 2016-11-12

package torch.sklearn.algorithm.clustering

import torch.sklearn.general._

// val data = Array(Array(1.0, 2.0), Array(1.0, 1.0), Array(0.8, 1.0),
//     Array(2.0, 3.0), Array(1.1, 1.1), Array(2.0, 2.2), Array(6.0, 5.0),
//     Array(6.0, 7.0), Array(6.0, 6.6), Array(6.0, 6.1), Array(6.0, 6.2))
// val hdbscan = new HDBSCAN()
// hdbscan.config(Map("limit" -> 2, "k" -> 2))
// hdbscan.cluster(data)

class HDBSCAN() extends Clustering {
    val algoname: String = "HDBSCAN"
    val version: String = "0.1"

    var splittree = Array[(Int, Int)]()
    var treecheck = Map[Int, Int]()
    var k = 2
    var limit = 2

    def distArr(a1: Array[Double], a2: Array[Double]): Double =
        Math.sqrt(arrayminussquare(a1, a2).sum)
    def cascade(p: Int, c: Int): Unit= {
        splittree.map{ l =>
            if (l._1 == p && treecheck(l._2) < 0) {
                treecheck += (l._2 -> c)
                cascade(l._2, c)
            } else if (l._2 == p && treecheck(l._1) < 0) {
                treecheck += (l._1 -> c)
                cascade(l._1, c)
            }
        }
    }

    override def clear(): Boolean = {
        splittree = Array[(Int, Int)]()
        treecheck = Map[Int, Int]()
        k = 2
        limit = 2
        true
    }

    override def config(paras: Map[String, Any]): Boolean = try {
        k = paras.getOrElse("K", paras.getOrElse("k", 2)).asInstanceOf[Int]
        limit = paras.getOrElse("LIMIT", paras.getOrElse("limit", 2)).asInstanceOf[Int]
        true
    } catch { case e: Exception =>
        Console.err.println(e)
        false
    }
    // --- HDBSCAN ---
    override def cluster(                    // DBSCAN
        data: Array[Array[Double]] // Data Array(xi)
    ): Array[Int] = {
        val n = data.size;
        val m = data.head.size;
        var distMatrix3 = new Array[Double](n*(n-1)/2)
        def setM3(x: Int, y: Int, v: Double) ={
            if (x < y) distMatrix3(x*n + y - ((Math.pow(x, 2) + 3*x)/2).toInt - 1) = v
            else if (y < x) distMatrix3(y*n + x - ((Math.pow(y, 2) + 3*y)/2).toInt - 1) = v
        }
        def getM3xy(x: Int, y: Int): Double = {
            if (x < y) return distMatrix3(x*n + y - ((Math.pow(x, 2) + 3*x)/2).toInt - 1)
            else if (y < x) return distMatrix3(y*n + x - ((Math.pow(y, 2) + 3*y)/2).toInt - 1)
            else return 0.0
        }
        def getM3x(x: Int): Array[Double] = (for (i <- 0 until n) yield getM3xy(i, x)).toArray
        
        for (i <- 0 to n-2) {
            for (j <- i+1 until n) {
                setM3(i, j, distArr(data(i), data(j)))
            }
        }
        for (i <- 0 until n) setM3(i, i, getM3x(i).sortBy(l => l).lift(limit).get)
        for (i <- 0 to n-2) {
            for (j <- i+1 until n) {
                setM3(i, j, Math.max(Math.max(getM3xy(i, i), getM3xy(j, j)), getM3xy(i, j)))
            }
        }
        var undone = Map(0 -> 0.0)
        undone ++= (1 until n).map((_, Double.MaxValue))
        var tree = Map[Int, (Int, Double)]()
        while (!undone.isEmpty) {
            val node = undone.minBy(_._2)._1
            undone -= node
            for (i <- 0 until n) {
                if (i != node) {
                    val v = getM3xy(node, i)
                    if (undone.contains(i) && v < undone(i)) {
                        undone += (i -> v)
                        tree += (i -> (node, v))
                    }
                }
            }
        }
        splittree = tree.toArray.sortBy(_._2._2).dropRight(k - 1).map(l => (l._1, l._2._1))
        treecheck = (0 until n).map((_, -1)).toMap
        var c = 1
        for (i <- 0 until n) {
            if (treecheck(i) < 0) {
                treecheck += (i -> c)
                cascade(i, c)
                c += 1
            }
        }
        return treecheck.toArray.sortBy(_._1).map(_._2)
    }
}
