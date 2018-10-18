package com.wangchao.nootbook.bean;

/**
 * @Auther:WangChao
 * @ClassName:Block
 * @Date:Created in 2018/10/11 17:05
 * @Despriction:用来封装数据的bean类
 * @Modify By:
 */
public class MessageBean {
    // 标识消息的作用
    // 1 传递区块链数据
    // 2 节点数据
    // 3 区块
    // 4 交易数据
    public int code;
    // 真实要传递的消息
    public String msg;

    public MessageBean() {
    }

    public MessageBean(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
