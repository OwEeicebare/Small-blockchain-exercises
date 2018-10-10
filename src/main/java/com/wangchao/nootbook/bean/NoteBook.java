package com.wangchao.nootbook.bean;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangchao.nootbook.Utils.HashUtils;
import org.springframework.web.bind.annotation.GetMapping;

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
        list.add(new Block(list.size()+1, genesis, HashUtils.sha256(genesis)));

        //在添加区块时同时保存到本地
        save2Disk();

    }

    //添加普通区块
    public void addNote(String note){
        if (list.size()<1){
            throw new RuntimeException("添加普通区块前必须添加第一个封面区块");
        }
        list.add(new Block(list.size()+1, note, HashUtils.sha256(note)));

        //在添加区块时同时保存到本地
        save2Disk();
    }

    //展示数据
    public ArrayList<Block> showlist(){

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
        for (Block block : list) {
            //获取内容
            String content = block.content;
            //生成哈希值
            String hashNew = HashUtils.sha256(content);
            // 比对hash,如果不一样说明数据内篡改
            if (!hashNew.equals(block.hash)){
                stringBuilder.append("编号为"+block.id+"的数据可能被篡改了<br/>");
            }
        }
        return stringBuilder.toString();
    }

}
