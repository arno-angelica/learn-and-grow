package com.arno.grow.user.web.hash;

import java.util.LinkedList;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author Arno
 * @since 1.0.0
 */
public class ConsistentHash {
    /**
     * 待添加入Hash环的真实服务器节点列表
     */
    private static final LinkedList<Node> realNodes = new LinkedList<>();

    /**
     * 虚拟节点列表
     */
    private static final SortedMap<Integer, Node> sortedMap = new TreeMap<Integer, Node>();

    static {
        //初始化server 10台。
        for (int i = 0; i < 10; i++) {
            String nodeName = "server"+i;
            Node node = new Node(nodeName);
            realNodes.add(node);
        }

        //引入虚拟节点： 添加1000倍虚拟节点，将10台server对应的虚拟节点放入TreeMap中
        for (Node node : realNodes) {
            for (int i = 1; i <=1000; i++) {
                String nodeName = node.getName() + "-VM" + String.valueOf(i);
                int hash = getHash(nodeName);//nodeName.hashCode();
                sortedMap.put(hash, node);
                System.out.println("虚拟节点hash:" + hash + "【" + nodeName + "】放入");
            }
        }
    }

    /**
     * 使用FNV1_32_HASH算法计算服务器的Hash值
     * @param str
     * @return
     */
    private static int getHash(String str) {
        // int hash = str.hashCode();

        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < str.length(); i++) {
            hash = (hash ^ str.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;

        // 如果算出来的值为负数则取其绝对值
        if (hash < 0) {
            hash = Math.abs(hash);
        }
        return hash;
    }

    /**
     * 得到应当路由到的结点
     */
    private static Node getServer(String key) {
        //得到该key的hash值
        int hash = getHash(key);
        //得到大于该Hash值的所有Map
        Node server;
        SortedMap<Integer, Node> subMap = sortedMap.tailMap(hash);
        if (subMap.isEmpty()) {
            //如果没有比该key的hash值大的，则从第一个node开始
            Integer i = sortedMap.firstKey();
            //返回对应的服务器
            server = sortedMap.get(i);
        } else {
            //第一个Key就是顺时针过去离node最近的那个结点
            Integer i = subMap.firstKey();
            //返回对应的服务器
            server= subMap.get(i);
        }

        if(server!=null) {
            server.put(key,hash+"");
            System.out.println(server.getName());
        }
        return server;
    }

    /**
     * 获取实际服务器上负载平均值
     */
    public static double getAverage(LinkedList<Node> arr) {
        double sum = 0;
        int number = arr.size();
        for (Node node : arr) {
            sum += node.getCount();
        }
        return sum / number;
    }

    /**
     * 获取实际服务器上负载的标准差
     *
     * @param arr
     * @return
     */
    public static double getStandardDeviation(LinkedList<Node> arr) {
        double sum = 0;
        int number = arr.size();
        double avgValue = getAverage(arr);//获取平均值
        for (Node node : arr) {
            sum += Math.pow((node.getCount() - avgValue), 2);
        }

        return Math.sqrt((sum / (number - 1)));
    }


    public static void main(String[] args) {

        //模拟一百万用户
        for (int i = 0; i < 1000000; i++) {
            String key = "User:"+i;

            System.out.printf("[%s]的hash值为%s, 被路由到结点[%s]\n", key, getHash(key), getServer(key).getName());
        }

        //打印 Node的实际负载
        for (Node node : realNodes) {
            System.out.println(node.getName() + "-> count：" + node.getCount());
        }

        System.out.println("标准差："+ getStandardDeviation(realNodes)+"");
    }
}
