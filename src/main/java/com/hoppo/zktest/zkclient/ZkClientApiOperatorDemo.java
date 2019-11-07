package com.hoppo.zktest.zkclient;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class ZkClientApiOperatorDemo {

    private final static String CONNECTSTRING = "192.168.11.129:2181,192.168.11.134:2181," +
            "192.168.11.135:2181,192.168.11.136:2181";

    private static ZkClient getInstance() {
        return new ZkClient(CONNECTSTRING, 10000);
    }

    public static void main(String[] args) throws InterruptedException {
        ZkClient zkClient = getInstance();
        //zkclient 提供递归创建父节点的功能
       /* zkClient.createPersistent("/zkclient/zkclient1/zkclient1-1/zkclient1-1-1",true);
        System.out.println("success");*/

        //删除节点
//        zkClient.deleteRecursive("/zkclient");


        //获取子节点,返回的是集合
        List<String> list = zkClient.getChildren("/test");
        System.out.println(list);

        //watcher

        zkClient.subscribeDataChanges("/test", new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {
                System.out.println("节点名称：" + s + "->节点修改后的值" + o);
            }

            @Override
            public void handleDataDeleted(String s) throws Exception {

            }
        });

        zkClient.writeData("/test", "abc");
        TimeUnit.SECONDS.sleep(2);

        zkClient.subscribeChildChanges("/test", new IZkChildListener() {
            @Override
            public void handleChildChange(String s, List<String> list) throws Exception {

            }
        });

    }
}
