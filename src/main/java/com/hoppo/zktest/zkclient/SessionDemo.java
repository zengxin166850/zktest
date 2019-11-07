package com.hoppo.zktest.zkclient;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;


public class SessionDemo {

    private final static String CONNECTSTRING = "192.168.11.129:2181,192.168.11.134:2181," +
            "192.168.11.135:2181,192.168.11.136:2181";

    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient(CONNECTSTRING, 4000);

        System.out.println(zkClient + " - > success");
        //创建节点
        zkClient.create("/test", "123", CreateMode.PERSISTENT);
        zkClient.subscribeDataChanges("/test", new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {
                System.out.println("节点名称：" + s + "->节点修改后的值" + o);
            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                System.out.println("节点名称：" + s + "->节点值被删除-------");
            }
        });
    }
}
