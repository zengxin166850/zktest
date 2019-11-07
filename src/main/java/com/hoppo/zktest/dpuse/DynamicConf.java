package com.hoppo.zktest.dpuse;

import com.alibaba.fastjson.JSON;
import com.hoppo.zktest.curator.CuratorClientUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.zookeeper.data.Stat;

import java.io.FileWriter;
import java.util.Map;

/**
 * Title: DynamicConf
 * Description: 配置
 * Copyright: Copyright (c) 2007
 * Company 北京华宇信息技术有限公司
 *
 * @author zengxin@thunisoft.com
 * @version 1.0
 * @date 2019/11/4 10:58
 */
public class DynamicConf {
    private static String nodePath = "/confPath";
    private static String confPath = System.getProperty("user.dir") + "/src/main/resources/test.properties";

    public void getConf() throws Exception {
        CuratorFramework instance = CuratorClientUtils.getInstance();
        //如果节点不存在，创建节点
        Stat stat = instance.checkExists().forPath(nodePath);

        if (stat == null) {
            instance.create().creatingParentsIfNeeded().forPath(nodePath, "{name:}".getBytes());
        }
        NodeCache cache = new NodeCache(instance,nodePath);
        while(true){
            instance.checkExists().forPath(nodePath);
        }
    }

    //检测
    public void synConf() throws Exception {
        FileWriter writer = new FileWriter(confPath);
        CuratorFramework instance = CuratorClientUtils.getInstance();
        byte[] bytes = instance.getData().forPath(nodePath);
        Map<String, String> map = JSON.parseObject(String.valueOf(bytes), Map.class);
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue() + "\n");
        }

    }
}