package com.lp.springmvc.demo.zk;

import java.util.List;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

public class GateWayClient3 {

	public static void main(String[] args) throws Exception {
		 RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);//刚开始重试间隔为1秒，之后重试间隔逐渐增加，最多重试不超过三次
	        /*RetryPolicy retryPolicy1 = new RetryNTimes(3, 1000);//最大重试次数，和两次重试间隔时间
	        RetryPolicy retryPolicy2 = new RetryUntilElapsed(5000, 1000);//会一直重试直到达到规定时间，第一个参数整个重试不能超过时间，第二个参数重试间隔
	        //第一种方式
	        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.0.3:2181", 5000,5000,retryPolicy);//最后一个参数重试策略
	        */
		//第二种方式
     CuratorFramework client1 = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
             .sessionTimeoutMs(5000)//会话超时时间
             .connectionTimeoutMs(5000)//连接超时时间
             .retryPolicy(retryPolicy)
             .build();

     client1.start();
    
     String gateway = client1.create().creatingParentsIfNeeded()//若创建节点的父节点不存在会先创建父节点再创建子节点
             .withMode(CreateMode.EPHEMERAL)//withMode节点类型，
             .forPath("/baymax/gateways/g3/status",null);
    /* String gatewayStatus = client1.create().creatingParentsIfNeeded()//若创建节点的父节点不存在会先创建父节点再创建子节点
             .withMode(CreateMode.EPHEMERAL)//withMode节点类型，
             .forPath("/baymax/gateways/g3/status",null);
     System.out.println(gatewayStatus);*/

	    List<String> list = client1.getChildren().forPath("/baymax/gateways");
	    System.out.println(list);


	    Thread.sleep(Integer.MAX_VALUE);

	}

}
