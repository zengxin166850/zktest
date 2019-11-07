package com.hoppo.zktest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TopSku {

    private static String filePath = "C:\\Users\\lenovo\\Downloads\\data";
    private static String xpath = "C:\\Users\\lenovo\\Downloads\\x.txt";
    private static String ypath = "C:\\Users\\lenovo\\Downloads";

    private List<File> paths = new ArrayList<>();

    public void getPaths(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isDirectory())
                    getPaths(f);
                else
                    paths.add(f);
            }
        } else {
            paths.add(file);
        }
    }

    @Test
    //1.使用dom4j将sku号读取到x文本中（多线程是否可行？内存不限制？）
    public void readSku() throws IOException {
        long lasting = System.currentTimeMillis();
        FileWriter writer = new FileWriter(xpath);
        try {
//            getPaths(new File(filePath+"/1/1.xml"));
            getPaths(new File(filePath));
            SAXReader reader = new SAXReader();
            for (File f : paths) {
                Document doc = reader.read(f);
                Element root = doc.getRootElement();
                Element foo;
                for (Iterator i = root.elementIterator("order"); i.hasNext(); ) {
                    foo = (Element) i.next();
                    writer.write(foo.elementText("sku") + "," + foo.elementText("num") + "\n");
//                    System.out.print("订单号码:" + foo.elementText("sku"));
//                    System.out.println("数量:" + foo.elementText("num"));
                }
            }
            writer.close();
            System.err.println("cost time " + (System.currentTimeMillis() - lasting));
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    //2.将x文本划分为小文件。每个sku为16字节。，64个sku为1kb。按理1m内存可以存储64000个sku，考虑程序本身耗存储。
    //每个文件存储10000个sku左右(约200k)，将sku取模后存入y个小文件。
    //直接取模由于极端情况，hash分配是不均匀的。需要考虑其他方法,,
    @Test
    public void splitFile() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(xpath));
            String order = null;
            while((order = reader.readLine())!=null){
                String[] split = order.split(",");
                String sku = split[1];
                FileWriter writer = new FileWriter(ypath+"\\test"+sku.hashCode()%20+".txt");
                writer.write(order);
            }
//            String s = reader.readLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //3.统计y个小文件中sku词频。
    public void countSku() {
    }

    //4.找出每个文件频率最大的10个()
    public void topSkuStepOne() {
    }

    //5.归并排序？
    public void topSkuStepTwo() {
    }
}