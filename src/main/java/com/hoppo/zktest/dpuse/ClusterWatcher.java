package com.hoppo.zktest.dpuse;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.SynchronousQueue;

/**
 *   用于集群管理的Watcher
 * @author zengxin@thunisoft.com
 * @date: 2019-10-29 11:22
 * @version 1.0
 */
public class ClusterWatcher implements Watcher {

    private final static String CONNECTSTRING = "192.168.11.129:2181,192.168.11.134:2181," +
            "192.168.11.135:2181,192.168.11.136:2181";
    private ZooKeeper zooKeeper;
    private SynchronousQueue<Integer> lock = new SynchronousQueue<Integer>();
    private static final String CLUSTER_PATH = "/testCluster";

    public ClusterWatcher() throws IOException {
        //；连接
        zooKeeper = new ZooKeeper(CONNECTSTRING, 5000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                if(event.getType() != Event.EventType.None){
                    System.out.println("变化路径: " + event.getPath() + "; 变化类型: " + event.getType().name());
                }
            }
        });
    }

    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
            try {
                lock.put(1);
                synchronized (zooKeeper) {
                    int chidren_actual = zooKeeper.getChildren(CLUSTER_PATH, false).size();
                    int children_before = Integer.valueOf(new String(zooKeeper.getData(CLUSTER_PATH, false, null)));
                    zooKeeper.setData(CLUSTER_PATH, String.valueOf(chidren_actual).getBytes(), -1);
                    if (chidren_actual > children_before) {
                        System.out.println("集群中有新服务上线...");
                    } else {
                        System.out.println("集群中有服务下线...");
                    }
                }

            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void await() throws InterruptedException {
        lock.take();
    }
}