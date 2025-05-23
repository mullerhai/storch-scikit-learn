// Storch - Classification
// 2025-09-12

package torch.sklearn.algorithm.classification

import torch.sklearn.algorithm.Algorithm

trait Classification extends Algorithm {
    val algotype: String = "Classification"
    def train(data: Array[(Int, Array[Double])]): Boolean
    def predict(data: Array[Array[Double]]): Array[Int]
}