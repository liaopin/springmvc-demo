package com.lp.springmvc.demo.zk;

import java.util.List;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

public class SystemClient2 {

	public static void main(String[] args) throws Exception {
		 RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);//刚开始重试间隔为1秒，之后重试间隔逐渐增加，最多重试不超过三次
	        /*RetryPolicy retryPolicy1 = new RetryNTimes(3, 1000);//最大重试次数，和两次重试间隔时间
	        RetryPolicy retryPolicy2 = new RetryUntilElapsed(5000, 1000);//会一直重试直到达到规定时间，第一个参数整个重试不能超过时间，第二个参数重试间隔
	        //第一种方式
	        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.0.3:2181", 5000,5000,retryPolicy);//最后一个参数重试策略
	        */
		//第二种方式
        final CuratorFramework client1 = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
                .sessionTimeoutMs(5000)//会话超时时间
                .connectionTimeoutMs(5000)//连接超时时间
                .retryPolicy(retryPolicy)
                .build();

        client1.start();
        
        String system = client1.create().creatingParentsIfNeeded()//若创建节点的父节点不存在会先创建父节点再创建子节点
                .withMode(CreateMode.EPHEMERAL)//withMode节点类型，
                .forPath("/baymax/systems/s2",null);
        System.out.println(system);

	    List<String> list = client1.getChildren().forPath("/baymax/systems");
	    System.out.println(list);
	    
	    final PathChildrenCache cache = new PathChildrenCache(client1,"/baymax/gateways",true);
        cache.start();

        cache.getListenable().addListener(new PathChildrenCacheListener() {

            @Override
            public void childEvent(CuratorFramework curator, PathChildrenCacheEvent event) throws Exception {
                switch (event.getType()) {
                case CHILD_ADDED:
                	
                	//如果是添加新的节点，则监听该新节点的子节点事件
                	final PathChildrenCache cache2 = new PathChildrenCache(client1,event.getData().getPath(),true);
                    cache2.start();
                    cache2.getListenable().addListener(new PathChildrenCacheListener(){

						@Override
						public void childEvent(CuratorFramework arg0,
								PathChildrenCacheEvent gateWayStatusevent) throws Exception {
									switch (gateWayStatusevent.getType()) {
					                case CHILD_ADDED:
					                	 System.out.println("网关启动add:" + gateWayStatusevent.getData());
					                	break;
					                case CHILD_REMOVED:	
					                	 System.out.println("网关关闭update:" + gateWayStatusevent.getData());
					                     break;
					                default:
					                    break;
									}
						}
                    	
                    });
                	
                    System.out.println("add:" + event.getData());
                    break;
                case CHILD_UPDATED:
                    System.out.println("update:" + event.getData());
                    break;
                case CHILD_REMOVED:
                    System.out.println("remove:" + event.getData());
                    break;
                default:
                    break;
                }
            }
        });



	    Thread.sleep(Integer.MAX_VALUE);
	}

}
