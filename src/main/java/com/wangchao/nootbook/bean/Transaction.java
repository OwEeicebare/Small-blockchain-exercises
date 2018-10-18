package com.wangchao.nootbook.bean;

import com.wangchao.nootbook.Utils.RSAUtils;

import java.security.PublicKey;

/**
 * @Auther:WangChao
 * @ClassName:Transaction
 * @Date:Created in 2018/10/11 12:13
 * @Despriction:收款方bean类
 * @Modify By:
 */
public class Transaction {
        //发送地址
        private String senderAddress;

    //收款地址
    private String receiveerAddress;
    //金额/附加信息
    private String content;
    //签名
    private  String signature;

    @Override
    public String toString() {
        return "Transaction{" +
                "senderAddress='" + senderAddress + '\'' +
                ", receiveerAddress='" + receiveerAddress + '\'' +
                ", content='" + content + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getReceiveerAddress() {
        return receiveerAddress;
    }

    public void setReceiveerAddress(String receiveerAddress) {
        this.receiveerAddress = receiveerAddress;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }


    public Transaction() {
    }

    public Transaction(String senderAddress, String receiveerAddress, String content, String signature) {

        this.senderAddress = senderAddress;
        this.receiveerAddress = receiveerAddress;
        this.content = content;
        this.signature = signature;
    }
    // 验证交易是否合法
    public boolean verify() {

        PublicKey publicKey = RSAUtils.getPublicKeyFromString("RSA", senderAddress);
        return RSAUtils.verifyDataJS("SHA256withRSA", publicKey, content, signature);

    }


}
