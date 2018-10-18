package com.wangchao.nootbook.bean;


import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.wangchao.nootbook.Utils.RSAUtils;

import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;


/**
 * @Auther:WangChao
 * @ClassName:Wallet
 * @Date:Created in 2018/10/11 11:34
 * @Despriction:钱包bean类
 * @Modify By:
 */
public class Wallet {
    // 公钥
    public PublicKey publicKey;
    //私钥
    public PrivateKey privateKey;

    private String name;
    public Wallet(String name){
        //保存公私钥匙的文件
        File pubFile = new File(name+".pub");
        File priFile = new File(name+".pri");
        //如果文件不存在,说明没有公私钥文件
        if (!pubFile.exists() || !priFile.exists() || pubFile.length() ==0 || priFile.length() == 0){
            RSAUtils.generateKeysJS("RSA", name + ".pri", name + ".pub");
        }
        // 从文件中读取公私钥
//        publicKey = RSAUtils.getPublicKeyFromFile("RSA", name + ".pub");
//        privateKey = RSAUtils.getPrivateKey("RSA", name + ".pri");

    }
    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


        //转账的时候需要写入收款方的公钥还有转账的金额就是内容
        public Transaction sendMoney(String receiverPublickKey, String content){
        //将公钥转为字符串
           String publicKeyEncode =  Base64.encode(publicKey.getEncoded());

            // 生成签名
            String signature = RSAUtils.getSignature("SHA256withRSA", privateKey, content);

            //生成交易对象
            Transaction transaction = new Transaction(publicKeyEncode,receiverPublickKey,signature,content);

            return transaction;
        }

    public static void main(String[] args) {
        //生成两个公私钥
        Wallet a = new Wallet("a");
        Wallet b = new Wallet("b");

//        String receiverAddress = Base64.encode(b.getPublicKey().getEncoded());
//
//        Transaction transaction = a.sendMoney(receiverAddress, "100");
//        boolean verify = transaction.verify();
//        System.out.println(verify);
    }
}
