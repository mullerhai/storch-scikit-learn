// Storch - Abnormal Detection
// 2025-03-04

package torch.sklearn.algorithm.abnormal

import torch.sklearn.algorithm.Algorithm

trait Abnormal extends Algorithm {
    val algotype: String = "Abnormal"
    def train(data: Array[Array[Double]]): Boolean
    def predict(data: Array[Array[Double]]): Array[Double]
}