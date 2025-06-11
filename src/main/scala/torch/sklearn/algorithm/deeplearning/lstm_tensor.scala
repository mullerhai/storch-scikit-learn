//// Storch - 2017-01-16
//
//package torch.sklearn.algorithm.deeplearning
//
//// 导入 javacpp-pytorch 相关库
//import org.bytedeco.pytorch.global.torch as torchGlobal
//import org.bytedeco.pytorch.*
//
//class LstmParam(val mem_cell_ct: Int, val x_dim: Int) {
//    val concat_len = x_dim + mem_cell_ct
//    // 使用 Tensor 初始化权重和偏置
//    var wg = torchGlobal.randn(mem_cell_ct, concat_len).mul(0.2).sub(0.1)
//    var wi = torchGlobal.randn(mem_cell_ct, concat_len).mul(0.2).sub(0.1)
//    var wf = torchGlobal.randn(mem_cell_ct, concat_len).mul(0.2).sub(0.1)
//    var wo = torchGlobal.randn(mem_cell_ct, concat_len).mul(0.2).sub(0.1)
//    var bg = torchGlobal.randn(mem_cell_ct).mul(0.2).sub(0.1)
//    var bi = torchGlobal.randn(mem_cell_ct).mul(0.2).sub(0.1)
//    var bf = torchGlobal.randn(mem_cell_ct).mul(0.2).sub(0.1)
//    var bo = torchGlobal.randn(mem_cell_ct).mul(0.2).sub(0.1)
//    // 差异也使用 Tensor
//    var wg_diff = torchGlobal.zeros(mem_cell_ct, concat_len)
//    var wi_diff = torchGlobal.zeros(mem_cell_ct, concat_len)
//    var wf_diff = torchGlobal.zeros(mem_cell_ct, concat_len)
//    var wo_diff = torchGlobal.zeros(mem_cell_ct, concat_len)
//    var bg_diff = torchGlobal.zeros(mem_cell_ct)
//    var bi_diff = torchGlobal.zeros(mem_cell_ct)
//    var bf_diff = torchGlobal.zeros(mem_cell_ct)
//    var bo_diff = torchGlobal.zeros(mem_cell_ct)
//
//    def clear_diff()= {
//        wg_diff = torchGlobal.zeros(mem_cell_ct, concat_len)
//        wi_diff = torchGlobal.zeros(mem_cell_ct, concat_len)
//        wf_diff = torchGlobal.zeros(mem_cell_ct, concat_len)
//        wo_diff = torchGlobal.zeros(mem_cell_ct, concat_len)
//        bg_diff = torchGlobal.zeros(mem_cell_ct)
//        bi_diff = torchGlobal.zeros(mem_cell_ct)
//        bf_diff = torchGlobal.zeros(mem_cell_ct)
//        bo_diff = torchGlobal.zeros(mem_cell_ct)
//    }
//
//    def apply_diff(lr: Double)= {
//        wg = wg.sub(wg_diff.mul(lr))
//        wi = wi.sub(wi_diff.mul(lr))
//        wf = wf.sub(wf_diff.mul(lr))
//        wo = wo.sub(wo_diff.mul(lr))
//        bg = bg.sub(bg_diff.mul(lr))
//        bi = bi.sub(bi_diff.mul(lr))
//        bf = bf.sub(bf_diff.mul(lr))
//        bo = bo.sub(bo_diff.mul(lr))
//        clear_diff()
//    }
//
//    def clear_wb() ={
//        wg = torchGlobal.randn(mem_cell_ct, concat_len).mul(0.2).sub(0.1)
//        wi = torchGlobal.randn(mem_cell_ct, concat_len).mul(0.2).sub(0.1)
//        wf = torchGlobal.randn(mem_cell_ct, concat_len).mul(0.2).sub(0.1)
//        wo = torchGlobal.randn(mem_cell_ct, concat_len).mul(0.2).sub(0.1)
//        bg = torchGlobal.randn(mem_cell_ct).mul(0.2).sub(0.1)
//        bi = torchGlobal.randn(mem_cell_ct).mul(0.2).sub(0.1)
//        bf = torchGlobal.randn(mem_cell_ct).mul(0.2).sub(0.1)
//        bo = torchGlobal.randn(mem_cell_ct).mul(0.2).sub(0.1)
//        clear_diff()
//    }
//}
//
//class LstmNode(val param: LstmParam) {
//    var g: Tensor = _
//    var i: Tensor = _
//    var f: Tensor = _
//    var o: Tensor = _
//    var s: Tensor = _
//    var h: Tensor = _
//    var bottom_diff_h: Tensor = _
//    var bottom_diff_s: Tensor = _
//    var bottom_diff_x: Tensor = _
//
//    val mem_cell_ct = param.mem_cell_ct
//    val x_dim = param.x_dim
//    val concat_len = param.concat_len
//    var s_prev: Tensor = _
//    var h_prev: Tensor = _
//    var xc: Tensor = _
//
//    def tanh_arr(x: Tensor): Tensor = {
//        return torchGlobal.tanh(x)
//    }
//
//    def sigmoid_arr(x: Tensor): Tensor = {
//        return torchGlobal.sigmoid(x)
//    }
//
//    def dot_wxb(w: Tensor, x: Tensor, b: Tensor): Tensor = {
//        return w.matmul(x).add(b)
//    }
//
//    def dot_wd(w: Tensor, d: Tensor): Tensor = {
//        return w.t().matmul(d)
//    }
//
//    def diff_arr(arr: Tensor, diff: Tensor): Tensor = {
//        return arr.mul(1 - arr).mul(diff)
//    }
//
//    def outer_arr(a1: Tensor, a2: Tensor): Tensor = {
//        return a1.unsqueeze(1).matmul(a2.unsqueeze(0))
//    }
//
//    def set_bottom_data(x: Tensor, s_prev_in: Tensor, h_prev_in: Tensor) ={
//        s_prev = s_prev_in
//        h_prev = h_prev_in
//        xc = torchGlobal.cat(Array(x, h_prev_in))
//        g = tanh_arr(dot_wxb(param.wg, xc, param.bg))
//        i = sigmoid_arr(dot_wxb(param.wi, xc, param.bi))
//        f = sigmoid_arr(dot_wxb(param.wf, xc, param.bf))
//        o = sigmoid_arr(dot_wxb(param.wo, xc, param.bo))
//        s = g.mul(i).add(s_prev_in.mul(f))
//        h = s.mul(o)
//    }
//
//    def set_top_diff(top_diff_h: Tensor, top_diff_s: Tensor)= {
//        val d_s = o.mul(top_diff_h).add(top_diff_s)
//        val d_o = s.mul(top_diff_h)
//        val d_i = g.mul(d_s)
//        val d_g = i.mul(d_s)
//        val d_f = s_prev.mul(d_s)
//
//        val d_i_input = diff_arr(i, d_i)
//        val d_f_input = diff_arr(f, d_f)
//        val d_o_input = diff_arr(o, d_o)
//        val d_g_input = g.mul(g).mul(-1).add(1).mul(d_g)
//
//        param.wi_diff = param.wi_diff.add(outer_arr(d_i_input, xc))
//        param.wf_diff = param.wf_diff.add(outer_arr(d_f_input, xc))
//        param.wo_diff = param.wo_diff.add(outer_arr(d_o_input, xc))
//        param.wg_diff = param.wg_diff.add(outer_arr(d_g_input, xc))
//        param.bi_diff = param.bi_diff.add(d_i_input)
//        param.bf_diff = param.bf_diff.add(d_f_input)
//        param.bo_diff = param.bo_diff.add(d_o_input)
//        param.bg_diff = param.bg_diff.add(d_g_input)
//
//        val dxc = dot_wd(param.wi, d_i_input)
//          .add(dot_wd(param.wf, d_f_input))
//          .add(dot_wd(param.wo, d_o_input))
//          .add(dot_wd(param.wg, d_g_input))
//
//        val (b_d_x, b_d_h) = dxc.split(Array(x_dim, mem_cell_ct))
//        bottom_diff_x = b_d_x
//        bottom_diff_h = b_d_h
//        bottom_diff_s = d_s.mul(f)
//    }
//}
//
//class LstmNetwork(val param: LstmParam) {
//    var node_list = Array[LstmNode]()
//    var x_list = Array[Tensor]()
//
//    def set_y_list(y_list: Array[Tensor], loss_func: (Tensor, Tensor) => Tensor, diff_func: (Tensor, Tensor) => Tensor): Tensor = {
//        if (y_list.length == x_list.length) {
//            var idx = x_list.length - 1
//            var h = node_list(idx).h
//            var y = y_list(idx)
//
//            var loss = loss_func(h, y)
//            var diff_h = diff_func(h, y)
//            var diff_s = torchGlobal.zeros(param.mem_cell_ct)
//            node_list(idx).set_top_diff(diff_h, diff_s)
//            idx -= 1
//
//            while (idx >= 0) {
//                h = node_list(idx).h
//                y = y_list(idx)
//                val p1_node = node_list(idx + 1)
//                loss = loss.add(loss_func(h, y))
//                diff_h = diff_func(h, y).add(p1_node.bottom_diff_h)
//                diff_s = p1_node.bottom_diff_s
//                node_list(idx).set_top_diff(diff_h, diff_s)
//                idx -= 1
//            }
//
//            return loss
//        } else {
//            System.err.println("Y_LIST_SIZE != X_LIST_SIZE")
//            return torchGlobal.zeros(1)
//        }
//    }
//
//    def set_x_list(input_x_list: Array[Tensor])= {
//        x_list = input_x_list
//        var s_prev = torchGlobal.zeros(param.mem_cell_ct)
//        var h_prev = torchGlobal.zeros(param.mem_cell_ct)
//        for (idx <- 0 until x_list.length) {
//            val x = x_list(idx)
//            val node = new LstmNode(param)
//            node.set_bottom_data(x, s_prev, h_prev)
//            node_list :+= node
//            s_prev = node.s
//            h_prev = node.h
//        }
//    }
//
//    def clear()= {
//        node_list = Array[LstmNode]()
//        x_list = Array[Tensor]()
//    }
//}