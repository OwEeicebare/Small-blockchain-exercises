package com.wangchao.nootbook.Controller;

import com.wangchao.nootbook.bean.Block;
import com.wangchao.nootbook.bean.NoteBook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * @Auther:WangChao
 * @ClassName:BlockController
 * @Date:Created in 2018/10/10 16:27
 * @Despriction:
 * @Modify By:
 */
@RestController
public class BlockController {
    NoteBook book = new NoteBook();

    //添加封面区块入口如果添加成功返回success失败则返回fail和失败原因
    @PostMapping(("/addGenesis"))
    public String addGenesis(String genesis){
        try {
            book.addGenesis(genesis);
            return "success";
        } catch (Exception e) {
            return "fail:" + e.getMessage();
        }

    }

    //添加封面区块入口如果添加成功返回success失败则返回fail和失败原因
    @PostMapping(("/addNote"))
    public String addNote(String note) {
        try {
            book.addNote(note);
            return "success";
        } catch (Exception e) {
            return "fail:" + e.getMessage();
        }

       }

    //展示入口返回这个数据的集合
    @GetMapping("/showlist")
    public List<Block> showlist() throws InterruptedException {

        Thread.sleep(1000);
        return book.showlist();
    }

    //校验功能
    @GetMapping("/verify")
    public String verify() {
        //调用验证方法拿到拼接的字符串
        String verify = book.verify();
        //如果拿到的字符串没有有数据证明内容没有问题
        if (org.springframework.util.StringUtils.isEmpty(verify)){
            return "数据没有问题";
        }
        return verify;
    }
}
