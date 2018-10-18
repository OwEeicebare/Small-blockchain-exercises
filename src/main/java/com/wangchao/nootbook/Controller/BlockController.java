package com.wangchao.nootbook.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangchao.nootbook.BlockchainApplication;
import com.wangchao.nootbook.bean.Block;
import com.wangchao.nootbook.ws.MyServer;
import com.wangchao.nootbook.ws.MyClient;
import com.wangchao.nootbook.bean.MessageBean;
import com.wangchao.nootbook.bean.NoteBook;
import com.wangchao.nootbook.bean.Transaction;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


@RestController
public class BlockController {

    private NoteBook notebook = NoteBook.getInstance();

    @PostMapping("/addGenesis")
    public String addGenesis(String genesis) {

        try {
            notebook.addGenesis(genesis);
            return "添加成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "添加失败:" + e.getMessage();
        }
    }

    // 如果Controller中的方法参数是对象类型,需要通过对象接收前台传递过来的数据,必须要保证对应的实体类要有getter和setter方法

    @PostMapping("/addNote")
    public String addNote(Transaction transaction) {

        try {

            // 校验交易数据是否合法
            if (transaction.verify()) {

                // 包装数据
                MessageBean bean = new MessageBean(4, transaction.getContent());

                ObjectMapper objectMapper = new ObjectMapper();
                String msg = objectMapper.writeValueAsString(bean);

                // 广播交易数据
                server.broadcast(msg);

                notebook.addNote(transaction.getContent());
                return "添加成功";
            } else {
                return "数据校验失败";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "添加失败:" + e.getMessage();
        }
    }

    @GetMapping("/showlist")
    public List<Block> showlist() throws InterruptedException {

        Thread.sleep(1000);

        return notebook.showlist();
    }

    @GetMapping("/verify")
    public String verify() {

        String verify = notebook.verify();

        if (StringUtils.isEmpty(verify)) {
            return "数据没有问题";
        }

        return verify;
    }

    private HashSet<String> set = new HashSet<>();

    // 注册节点
    @RequestMapping("/regist")
    public String regist(String port) {
        set.add(port);
        return "注册成功";
    }

    // 存放所有连接websocket服务端的客户端
    private List<MyClient> clients = new ArrayList<>();


    // 连接
    @RequestMapping("/conn")
    public String conn() throws Exception {
        // 遍历本地存储的服务端地址,然后连接
        for (String node : set) {
            URI uri = new URI("ws://localhost:" + node);
            MyClient client = new MyClient(uri, node);
            client.connect();

            clients.add(client);
        }
        return "连接成功";

    }

    private MyServer server;

    // Controller创建后立刻调用该方法
    @PostConstruct
    public void init() {
        // 将springboot启动的端口号+1,作为WebSocket服务端启动时使用的端口号
        server = new MyServer(Integer.parseInt(BlockchainApplication.port) + 1);
        server.start();
    }

    // 请求同步数据,
    // 模拟场景 : 全新的一个节点上线了,但是没有区块链的数据,发送一个请求,要求获取其他节点上存储的区块链数据
    @RequestMapping("/syncData")
    public String syncData() {

        for (MyClient client : clients) {
            client.send("亲,把你的区块链数据给我一份");
        }

        return "连接成功";

    }
}
