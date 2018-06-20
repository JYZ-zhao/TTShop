package com.taotao.rest.dao;

public interface JedisClient {
	String get(String key);

	String set(String key, String value);

	String hget(String hkey, String key);
	
	long del(String key);

	long hset(String hkey, String key, String value);
	
	//自增
	long incr(String key);
	
	//过期时间
	long expire(String key ,int second);
	
	//查看过期时间
	long ttl(String key);
	
	long hdel(String hkey, String key);
}
