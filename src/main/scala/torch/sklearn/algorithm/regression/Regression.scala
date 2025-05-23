// Storch - Regression
// 2025-09-12

package torch.sklearn.algorithm.regression

import torch.sklearn.algorithm.Algorithm

trait Regression extends Algorithm {
    val algotype: String = "Regression"
    def train(data: Array[(Double, Array[Double])]): Boolean
    def predict(data: Array[Array[Double]]): Array[Double]
}