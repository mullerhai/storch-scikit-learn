// Storch - Clustering
// 2025-09-30

package torch.sklearn.algorithm.clustering

import torch.sklearn.algorithm.Algorithm

trait Clustering extends Algorithm {
    val algotype: String = "Clustering"
    def cluster(data: Array[Array[Double]]): Array[Int]
}