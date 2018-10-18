package com.wangchao.nootbook.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangchao.nootbook.bean.Block;
import com.wangchao.nootbook.bean.MessageBean;
import com.wangchao.nootbook.bean.NoteBook;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.List;

public class MyServer extends WebSocketServer {

    private int port;

    public MyServer(int port) {
        super(new InetSocketAddress(port));
        this.port = port;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("服务端___" + port + "___打开了一个连接");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("服务端___" + port + "___关闭了一个连接");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("服务端___" + port + "___收到了消息:" + message);

        try {
            if ("亲,把你的区块链数据给我一份".equals(message)) {
                // 获取本节点的区块链数据
                NoteBook notebook = NoteBook.getInstance();
                List<Block> blockList = notebook.showlist();
                ObjectMapper objectMapper = new ObjectMapper();
                String blockchain = objectMapper.writeValueAsString(blockList);

                // 包装数据
                MessageBean bean = new MessageBean(1, blockchain);
                String msg = objectMapper.writeValueAsString(bean);
                // 广播数据
                broadcast(msg);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.out.println("服务端___" + port + "___发生了错误:" + ex.getMessage());
    }

    @Override
    public void onStart() {
        System.out.println("服务端___" + port + "___启动成功");
    }
}
