package com.wangchao.nootbook.bean;

import lombok.Data;

/**
 * @Auther:WangChao
 * @ClassName:Block
 * @Date:Created in 2018/10/10 17:05
 * @Despriction:
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

    public Block() {
    }

    public Block(int id, String content, String hash) {
        this.id = id;
        this.content = content;
        this.hash = hash;
    }
}
