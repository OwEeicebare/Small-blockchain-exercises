package com.wangchao.nootbook.bean;

import lombok.Data;

/**
 * @Auther:WangChao
 * @ClassName:Block
 * @Date:Created in 2018/10/10 17:05
 * @Despriction:区块bean类
 * @Modify By:
 */
@Data
public class Block {
    //id
    public int id;
    //内容
    public String content;
    //哈希值
    public String hash;
    //工作量证明
    public int nonce;
    //之前区块的哈希值
    public String preHash;

    public Block() {
    }

    public Block(String preHash,int nonce,int id, String content, String hash) {
        this.id = id;
        this.content = content;
        this.hash = hash;
        this.nonce = nonce;
        this.preHash = preHash;
    }
}
