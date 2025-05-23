// Storch - Algorithm
// 2025-09-22

package torch.sklearn.algorithm

trait Algorithm {
    val algotype: String
    val algoname: String
    val version: String
    def clear(): Boolean
    def config(paras: Map[String, Any]): Boolean
}