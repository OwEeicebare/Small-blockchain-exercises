package com.wangchao.nootbook.ws;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;


import com.wangchao.nootbook.bean.Block;
import com.wangchao.nootbook.bean.MessageBean;
import com.wangchao.nootbook.bean.NoteBook;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.util.List;

public class MyClient extends WebSocketClient {

    private String name;

    public MyClient(URI serverUri, String name) {
        super(serverUri);
        this.name = name;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("客户端___" + name + "___打开了一个连接");
    }

    @Override
    public void onMessage(String message) {

        System.out.println("客户端___" + name + "___收到了消息:" + message);
        NoteBook notebook = NoteBook.getInstance();
        try {
            if (!StringUtils.isEmpty(message)) {
                // 把收到的消息还原成对象
                ObjectMapper objectMapper = new ObjectMapper();
                MessageBean messageBean = objectMapper.readValue(message, MessageBean.class);

                if (messageBean.code == 1) {
                    // 表示收到的消息是区块链数据.List<Block>

                    JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, Block.class);
                    List<Block> newList = objectMapper.readValue(messageBean.msg, javaType);
                    // 要去做数据的比对,然后判断是否接受对方传递过来的区块链数据

                    notebook.checkNewList(newList);
                } else if (messageBean.code == 4) {
                    // 交易数据
                    String transactionMsg = messageBean.msg;
                    // 添加数据到区块
                    notebook.addNote(transactionMsg);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("客户端___" + name + "___关闭了一个连接");
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("客户端___" + name + "___发生了错误:" + ex.getMessage());
    }
}
