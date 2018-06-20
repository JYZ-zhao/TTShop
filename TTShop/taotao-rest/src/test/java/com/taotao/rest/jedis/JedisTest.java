package com.taotao.rest.jedis;

import java.util.HashSet;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class JedisTest {

	@Test
	public void TestJedisSingle() {
		// 创建一个Jedis的对象
		Jedis jedis = new Jedis("192.168.25.130", 6379);
		jedis.auth("123456");
		// 调用Jedis对象的方法，方法名称和redis的命令一致
		jedis.set("key1", "jedisTest");
		String string = jedis.get("key1");
		System.out.println(string);
		// 关闭jedis
		jedis.close();
	}

	/**
	 * 使用连接池
	 */
	@Test
	public void TestJedisPool() {
		//创建连接池
		JedisPool pool = new JedisPool("192.168.25.130", 6379);
		//从连接池中获取jedis对象
		Jedis jedis = pool.getResource();
		jedis.auth("123456");
		String string = jedis.get("key1");
		System.out.println(string);
		//关闭jedis对象
		jedis.close();
		pool.close();
	}
	
	/**
	 * 集群版测试
	 */
	@Test
	public void testJedisCluster() {
		HashSet<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("192.168.25.130", 7001));
		nodes.add(new HostAndPort("192.168.25.130", 7002));
		nodes.add(new HostAndPort("192.168.25.130", 7003));
		nodes.add(new HostAndPort("192.168.25.130", 7004));
		nodes.add(new HostAndPort("192.168.25.130", 7005));
		nodes.add(new HostAndPort("192.168.25.130", 7006));
		JedisCluster cluster = new JedisCluster(nodes);
		cluster.set("ttt", "32434");
		String string = cluster.get("ttt");
		System.out.println(string);
		
		//关闭集群
		cluster.close();
	}
	
	
	
	/**
	 * 单机版测试
	 * <p>Title: testSpringJedisSingle</p>
	 * <p>Description: </p>
	 */
	@Test
	public void testSpringJedisSingle() {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		JedisPool pool = (JedisPool) applicationContext.getBean("redisClient");
		Jedis jedis = pool.getResource();
		jedis.auth("123456");
		String string = jedis.get("key1");
		System.out.println(string);
		jedis.close();
		pool.close();
	}
	
	@Test
	public void testSpringJedisCluster() {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		JedisCluster jedisCluster =  (JedisCluster) applicationContext.getBean("redisClient");
		String string = jedisCluster.get("key1");
		System.out.println(string);
		jedisCluster.close();
	}
}
