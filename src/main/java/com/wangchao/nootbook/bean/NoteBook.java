package com.wangchao.nootbook.bean;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangchao.nootbook.Utils.HashUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther:WangChao
 * @ClassName:NoteBook
 * @Date:Created in 2018/10/10 12:07
 * @Despriction:简易区块链
 * @Modify By:
 */
public class NoteBook {


    //用于保存数据的集合
    private ArrayList<Block> list = new ArrayList<>();

    //添加第一个区块
    public void addGenesis(String genesis){
        if (list.size()>0){
            throw new RuntimeException("添加第一个封面区块必须保证账本为添加任何数据");
        }
        //设置初始哈希值
        String preHash = "0000000000000000000000000000000000000000000000000000000000000000";
        //调用mine挖矿方法求一个特定的哈希值
        int nonce = mine(genesis, preHash);
        Block block = new Block(preHash,nonce,list.size()+1, genesis, HashUtils.sha256(genesis+preHash+nonce));
        list.add(block);

        //在添加区块时同时保存到本地
        save2Disk();

    }

    //添加普通区块
    public void addNote(String note){
        if (list.size()<1){
            throw new RuntimeException("添加普通区块前必须添加第一个封面区块");
        }
        //从list集合中拿到倒数第二个区块
        Block preBlock = list.get(list.size() - 1);
        //拿到倒数第二个区块的哈希值
        String preBlockHash = preBlock.hash;
        int nonce = mine(note,preBlockHash);

        //把这个新区块添加到list集合中
        list.add(new Block(preBlockHash,nonce,list.size()+1, note, HashUtils.sha256(note+preBlockHash+nonce)));

        //在添加区块时同时保存到本地
        save2Disk();
    }

    //展示数据
    public List<Block> showlist() {

        return list;
    }

    //保存到本地硬盘
    private void save2Disk(){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File("a.json");
            objectMapper.writeValue(file, list);
            //序列化保存到硬盘本地文件中
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //在构造函数中加载本地数据
    public NoteBook() {
        try {
            File file = new File("a.json");
            //确保文件存在并文件中有无数据
            if (file.exists()&&file.length()>0){
                ObjectMapper objectMapper = new ObjectMapper();
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, Block.class);
                list= objectMapper.readValue(file,javaType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //增加加载本地数据的方法
    private void loadFile(){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File("a.json");
            //判断文件是否存在
            if (file.exists()&&file.length()>0){
                //如果文件存在,读取之前的数据
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, String.class);
                objectMapper.readValue(file,javaType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 校验数据

    public String verify() {
        //new一个字符串容器
       StringBuilder stringBuilder = new StringBuilder();
        //循环遍历list集合拿到每个block对象
        for (int i =0;i<list.size();i++) {
            //拿到当前区块
            Block block = list.get(i);
            String content = block.content;//拿到当前区块的内容
            String savedHash = block.hash;//拿到当前区块的哈希值
            int nonce = block.nonce;//拿到当前区块的工作量证明
            String savedPreHash = block.preHash;//拿到当前区块保存的上一个哈希值
            int id = block.id;//拿到当前区块的id
            // 验证hash
            String caculatedHash = HashUtils.sha256(content + savedPreHash + nonce);
            //如果自己取得哈希值和当前区块的哈希值不一致
            if (!caculatedHash.equals(savedHash)) {
                stringBuilder.append("ID为" + id + "的区块数据中hash有问题,请注意检查<br/>");
            }

            // 验证prehash, 创世区块之后的区块
            if (i > 0) {
                Block preBlock = list.get(i - 1);
                String preBlockHash = preBlock.hash;//拿到上一个区块的哈希值
                //用保存的上一个区块的哈希值和取到的上一个区块的哈希值做比较
                if (!preBlockHash.equals(savedPreHash)) {
                    stringBuilder.append("ID为" + id + "的区块数据中prehash有问题,请注意检查<br/>");
                }
            }
 /*           Block block = list.get(i);
            //获取内容
            int id = block.id;
            String content = block.content;
            //获取工作量证明
            int nonce = block.nonce;
            //获取当前哈希
            String savedhash = block.hash;
//            //生成哈希值
//            String hashNew = HashUtils.sha256(content+nonce);
            //拿到前一个区块的哈希值
            String preHash = block.preHash;
            //如果遍历为第一个则赋值初始哈希值
            if (i==0){
                preHash = "0000000000000000000000000000000000000000000000000000000000000000";
                //校验创世区块
                String caculatedHash = HashUtils.sha256(content + preHash + nonce);
                if (!savedhash.equals(caculatedHash)){
                    stringBuilder.append("编号为" + id + "的数据有可能被篡改了,请注意防范黑客<br>");
                }
            }else {
                //校验其他区块
                String caculatedHash = HashUtils.sha256(content + preHash + nonce);
                if (!savedhash.equals(caculatedHash)) {
                    stringBuilder.append("编号为" + id + "的hash有问题,请注意防范黑客<br>");
                }
                Block preBlock = list.get(i - 1);
                String preBlockHash = preBlock.hash;
                if (!preBlockHash.equals(preHash)){
                    stringBuilder.append("编号为" + id + "的hash有问题,请注意防范黑客<br>");
                }
            }*/

//            // 比对hash,如果不一样说明数据内篡改
//            if (!hashNew.equals(savedhash)){
//                stringBuilder.append("编号为"+id+"的数据可能被篡改了<br/>");
//            }
        }
        return stringBuilder.toString();
    }

    //挖矿逻辑

    private int mine(String content, String preHash) {

        for (int i = 0; i < Integer.MAX_VALUE; i++) {

            String hash = HashUtils.sha256(content + preHash + i);
            if (hash.startsWith("0000")) {
                System.out.println("挖矿成功:" + i);
                return i;

            } else {
                System.out.println("这是第" + i + "次尝试挖矿");
            }
        }

        throw new RuntimeException("挖矿失败");

    }

}
