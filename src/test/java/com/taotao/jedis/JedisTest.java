package com.taotao.jedis;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.taotao.rest.component.JedisClient;
import com.taotao.rest.component.impl.JedisClientCluster;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class JedisTest {

	//@Test
	public void testJedisSingle() throws Exception{
		Jedis jedis = new Jedis("192.168.6.188",6379);
		jedis.set("test", "hello jedis");
		String string = jedis.get("test");
		System.out.println(string);
		jedis.close();
	}
	@Test
	public void testJedisPool(){
		 JedisPool jedisPool = new JedisPool("192.168.6.188",6379);
		 
		 Jedis jedis = jedisPool.getResource();
		 String string = jedis.get("test");
		 System.out.println(string);
		 
		 jedis.close();
		 jedisPool.close();
	}
	
	@Test
	public void testJedisCluter() throws IOException{
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("192.168.6.188",7001));
		nodes.add(new HostAndPort("192.168.6.188",7002));
		nodes.add(new HostAndPort("192.168.6.188",7003));
		nodes.add(new HostAndPort("192.168.6.188",7004));
		nodes.add(new HostAndPort("192.168.6.188",7005));
		nodes.add(new HostAndPort("192.168.6.188",7006));
		JedisCluster jedisCluster = new JedisCluster(nodes);
		
		jedisCluster.set("name", "Tom");
		jedisCluster.set("value", "100");
		String string1 = jedisCluster.get("name");
		String string2 = jedisCluster.get("value");
		System.out.println(string1);
		System.out.println(string2);
		
		
		jedisCluster.close();
	}
	@Test
	public void testJedisClientSpring() throws Exception{
		ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		JedisClient jedisClient = ac.getBean(JedisClient.class);
		
		jedisClient.set("testClient", "100");
		
		String string = jedisClient.get("testClient");
	
		System.out.println(string);
		ac.destroy();
	}
	
	
}
