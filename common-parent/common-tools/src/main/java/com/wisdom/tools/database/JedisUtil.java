package com.wisdom.tools.database;


import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.jedis.util.SafeEncoder;

import java.util.*;

/**
 * Copyright © 2018 dragonSaberCaptain. All rights reserved.
 *
 * @author captain
 * @date 2018-07-09 13:40 星期一
 */

@Deprecated
public class JedisUtil {
    /**
     * 单机版连接池
     */
    private static JedisPool jedisPools = null;

    /**
     * 集群连接池
     */
    private static JedisCluster jedisClusters = null;

    /**
     * 连接池配置
     */
    private static JedisPoolConfig poolConfig;

    /**
     * 集群模式，默认关闭
     */
    private static boolean clusterPattern = false;

    /**
     * 缓存生存时间
     */
    private static int expire;

    /**
     * 连接超时时间
     */
    private static int connectionTimeout;

    /**
     *
     */
    private static int soTimeout;

    /**
     * 重连次数
     */
    private static int maxAttempts;

    static {
        maxAttempts = 3;
        soTimeout = 60000;
        connectionTimeout = 60000;
        expire = 60000;
        poolConfig = new JedisPoolConfig();

        /* 最大连接数 */
        poolConfig.setMaxTotal(500);

        /* 最大空闲连接数 */
        poolConfig.setMaxIdle(100);

        /* 最小空闲连接数 */
        poolConfig.setMinIdle(3);

        /*
         * 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException
         */
        poolConfig.setMaxWaitMillis(3000);

        /* 在空闲时检查有效性, 默认false，只有在timeBetweenEvictionRunsMillis大于0时才有意义 */
        poolConfig.setTestWhileIdle(true);

        /* 逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1 */
        poolConfig.setTimeBetweenEvictionRunsMillis(30000);

        /* 每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3 */
        poolConfig.setNumTestsPerEvictionRun(10);

        /* 逐出连接的最小空闲时间 默认1800000毫秒(30分钟) */
        poolConfig.setMinEvictableIdleTimeMillis(1800000);

        /*
         * 对象空闲多久后逐出,当空闲时间>该值且空闲连接>最大空闲数时直接逐出,不再根据MinEvictableIdleTimeMillis判断(
         * 默认逐出策略)
         */
        poolConfig.setSoftMinEvictableIdleTimeMillis(1800000);
    }

    /**
     * 创建缓存连接池<br/>
     *
     * @param nodes    ip地址与端口号
     * @param password 密码
     * @return boolean true(成功)
     * @createDate: 2017年12月15日 下午5:08:48
     * @author dragonSaberCaptain
     */
    public static boolean createPool(Set<HostAndPort> nodes, String password,
                                     boolean isCluster) {
        // 集群开关
        clusterPattern = isCluster;
        password = StringUtils.isBlank(password) ? null : password.trim();

        if (nodes.size() == 0) {
            return false;
        }
        if (isCluster) {
            try {
                jedisClusters = new JedisCluster(nodes, connectionTimeout,
                        soTimeout, maxAttempts, password, poolConfig);
            } catch (JedisDataException e) {
                e.printStackTrace();
                throw new JedisDataException("该实例不支持集群");
            }
            JedisCluster jedisCluster = getJedisCluster();
            if (jedisCluster != null) {
                return true;
            }
        } else {
            // 有且只有一个节点时,才会是单机版
            if (nodes.size() == 1) {
                for (HostAndPort hostAndPort : nodes) {
                    jedisPools = new JedisPool(poolConfig,
                            hostAndPort.getHost(), hostAndPort.getPort(),
                            connectionTimeout, password);
                }
                Jedis jedis = getJedis();
                if (jedis != null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 解析ip地址与端口号 <br/>
     *
     * @param hostAndPorts ip地址与端口号,格式127.0.0.1:6379,127.0.0.1:6380(以逗号分隔)
     * @return Set<HostAndPort> ip地址与端口号
     * @createDate: 2017年12月15日 下午5:12:17
     * @author dragonSaberCaptain
     */
    public static Set<HostAndPort> getHostAndPort(String hostAndPorts) {
        Set<HostAndPort> nodes = new HashSet<>();

        String[] hostAndPort = hostAndPorts.split(",");
        String[] ipPortPair;
        for (String ipPort : hostAndPort) {
            ipPortPair = ipPort.split(":");
            if (ipPortPair.length == 1) {
                throw new JedisConnectionException("连接异常,ip与端口解析错误");
            }
            nodes.add(new HostAndPort(ipPortPair[0].trim(),
                    Integer.parseInt(ipPortPair[1].trim())));
        }
        return nodes;
    }

    /**
     * 获取Jedis实例 <br/>
     *
     * @return Jedis
     * @createDate: 2017年12月15日 下午5:14:26
     * @author dragonSaberCaptain
     */
    public static synchronized Jedis getJedis() {
        try {
            if (jedisPools != null) {
                Jedis jedis = jedisPools.getResource();
                if (StringUtils.isNotBlank(jedis.ping())) {
                    return jedis;
                }
            }
        } catch (JedisException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static synchronized JedisCluster getJedisCluster() {
        try {
            if (jedisClusters != null) {

                JedisCluster jedisCluster = jedisClusters;

                Map<String, JedisPool> map = jedisCluster.getClusterNodes();
                if (map.size() != 0) {
                    boolean bool = false;
                    for (JedisPool jedisPool : map.values()) {
                        if (StringUtils.isBlank(jedisPool.getResource().ping())) {
                            bool = true;
                            break;
                        }
                    }
                    if (!bool) {
                        return jedisCluster;
                    }
                }
            }
        } catch (JedisException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    // *******************************************Keys*******************************************//
    public static class Keys {
        /**
         * 设置key的过期时间，以秒为单位 <br/>
         *
         * @param key     密匙
         * @param seconds 时间,已秒为单位
         * @return 影响的记录数
         * @createDate: 2017年12月18日 上午9:35:32
         * @author dragonSaberCaptain
         */
        public static long expire(String key, int seconds) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.expire(key, seconds);
                }
            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.expire(key, seconds);
                    jedis.close();
                }
            }
            return result;
        }

        /**
         * 设置默认过期时间<br/>
         *
         * @param key 密匙
         * @return 1(成功) 0(失败)
         * @createDate: 2017年12月15日 下午5:32:57
         * @author dragonSaberCaptain
         */
        public static void expire(String key) {
            expire(key, expire);
        }

        /**
         * 清空所有key (集群:已过时)
         */
        public static String flushAll() {
            Jedis jedis = getJedis();
            String result = jedis.flushAll();
            jedis.close();
            return result;
        }

        /**
         * 更改key
         *
         * @param oldkey
         * @param newkey
         * @return 状态码
         */
        public static String rename(String oldkey, String newkey) {
            return rename(SafeEncoder.encode(oldkey),
                    SafeEncoder.encode(newkey));
        }

        /**
         * 更改key,仅当新key不存在时才执行 <br/>
         *
         * @param oldkey
         * @param newkey
         * @return 0(失败) 1(成功)
         * @createDate: 2017年12月18日 上午9:26:52
         * @author dragonSaberCaptain
         */
        public static long renamenx(String oldkey, String newkey) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.renamenx(oldkey, newkey);
                }
            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.renamenx(oldkey, newkey);
                    jedis.close();
                }
            }
            return result;
        }

        /**
         * 更改key
         *
         * @param oldkey
         * @param newkey
         * @return 状态码
         */
        public static String rename(byte[] oldkey, byte[] newkey) {
            String result = null;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.rename(oldkey, newkey);
                }
            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.rename(oldkey, newkey);
                    jedis.close();
                }
            }

            return result;
        }

        /**
         * 设置key的过期时间,它是距历元（即格林威治标准时间 1970 年 1 月 1 日的 00:00:00，格里高利历）的偏移量。 <br/>
         *
         * @param key       密匙
         * @param timestamp 时间 ,已秒为单位
         * @return 影响的记录数
         * @createDate: 2017年12月18日 上午9:40:01
         * @author dragonSaberCaptain
         */
        public static long expireAt(String key, long timestamp) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.expireAt(key, timestamp);
                }
            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.expireAt(key, timestamp);
                    jedis.close();
                }
            }

            return result;
        }

        /**
         * 查询key的过期时间 <br/>
         *
         * @param key 密匙
         * @return 以秒为单位的时间表示
         * @createDate: 2017年12月18日 上午9:42:31
         * @author dragonSaberCaptain
         */
        public static long ttl(String key) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.ttl(key);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.ttl(key);
                    jedis.close();
                }

            }

            return result;
        }

        /**
         * 取消对key过期时间的设置 <br/>
         *
         * @param key 密匙
         * @return 影响的记录数
         * @createDate: 2017年12月18日 上午9:43:56
         * @author dragonSaberCaptain
         */
        public static long persist(String key) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.persist(key);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.persist(key);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 删除keys对应的记录,可以是多个key <br/>
         *
         * @param keys
         * @return 删除的记录数
         * @createDate: 2017年12月18日 上午9:45:19
         * @author dragonSaberCaptain
         */
        public static long del(String... keys) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.del(keys);
                }
            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.del(keys);
                    jedis.close();
                }
            }
            return result;
        }

        /**
         * 删除keys对应的记录,可以是多个key <br/>
         *
         * @param keys
         * @return 删除的记录数
         * @createDate: 2017年12月18日 上午9:47:41
         * @author dragonSaberCaptain
         */
        public static long del(byte[]... keys) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.del(keys);
                }
            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.del(keys);
                    jedis.close();
                }
            }
            return result;
        }

        /**
         * 判断key是否存在 <br/>
         *
         * @param key
         * @return boolean true(存在)
         * @createDate: 2017年12月18日 上午9:49:51
         * @author dragonSaberCaptain
         */
        public static boolean exists(String key) {
            boolean result = false;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.exists(key);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.exists(key);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 对List,Set,SortSet进行排序,如果集合数据较大应避免使用这个方法 <br/>
         *
         * @param key
         * @return List<String> 集合的全部记录
         * @createDate: 2017年12月18日 上午9:54:23
         * @author dragonSaberCaptain
         */
        public static List<String> sort(String key) {
            List<String> result = null;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.sort(key);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.sort(key);
                    jedis.close();
                }

            }

            return result;
        }

        /**
         * 对List,Set,SortSet进行排序或limit <br/>
         *
         * @param key
         * @param parame 定义排序类型或limit的起止位置.
         * @return List<String> 全部或部分记录
         * @createDate: 2017年12月18日 上午9:57:57
         * @author dragonSaberCaptain
         */
        public static List<String> sort(String key, SortingParams parame) {
            List<String> result = null;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.sort(key, parame);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.sort(key, parame);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 返回指定key存储的类型 <br/>
         *
         * @param key
         * @return String string|list|set|zset|hash
         * @createDate: 2017年12月18日 上午9:59:33
         * @author dragonSaberCaptain
         */
        public static String type(String key) {
            String result = null;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.type(key);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.type(key);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 查找所有匹配给定的模式的键 <br/>
         *
         * @param key *表示多个,？表示一个
         * @return Set<String>单机返回 TreeSet<String>集群返回
         * @createDate: 2017年12月18日 上午10:11:27
         * @author dragonSaberCaptain
         */
        public static Set<String> keys(String key) {
            Set<String> result = null;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = new TreeSet<String>();
                    JedisPool jp;
                    Jedis connection;
                    Map<String, JedisPool> clusterNodes = jedisCluster
                            .getClusterNodes();
                    for (String k : clusterNodes.keySet()) {
                        jp = clusterNodes.get(k);
                        connection = jp.getResource();
                        try {
                            result.addAll(connection.keys(key));
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            connection.close();// 用完一定要close这个链接！！！
                        }
                    }
                }
            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.keys(key);
                    jedis.close();
                }

            }

            return result;
        }
    }

    // *******************************************Sets*******************************************//
    public static class Sets {

        /**
         * 向Set添加一条记录 <br/>
         *
         * @param key
         * @param member
         * @return 操作码 0(不存在) 1(存在)
         * @createDate: 2017年12月18日 上午10:18:56
         * @author dragonSaberCaptain
         */
        public static long sadd(String key, String member) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.sadd(key, member);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.sadd(key, member);
                    jedis.close();
                }

            }
            return result;
        }

        public static long sadd(byte[] key, byte[] member) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.sadd(key, member);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.sadd(key, member);
                    jedis.close();
                }

            }

            return result;
        }

        /**
         * 获取给定key中元素个数 <br/>
         *
         * @param key
         * @return 元素个数
         * @createDate: 2017年12月18日 上午10:27:12
         * @author dragonSaberCaptain
         */
        public static long scard(String key) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.scard(key);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.scard(key);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 返回从第一组和所有的给定集合之间的差异的成员 <br/>
         *
         * @param keys
         * @return 差异的成员集合
         * @createDate: 2017年12月18日 上午10:28:52
         * @author dragonSaberCaptain
         */
        public static Set<String> sdiff(String... keys) {
            Set<String> result = null;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.sdiff(keys);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.sdiff(keys);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 这个命令等于sdiff,但返回的不是结果集,而是将结果集存储在新的集合中，如果目标已存在，则覆盖。 <br/>
         *
         * @param newkey 新结果集的key
         * @param keys   比较的集合
         * @return 新集合中的记录数
         * @createDate: 2017年12月18日 上午10:30:02
         * @author dragonSaberCaptain
         */
        public static long sdiffstore(String newkey, String... keys) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.sdiffstore(newkey, keys);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.sdiffstore(newkey, keys);
                    jedis.close();
                }
            }
            return result;
        }

        /**
         * 返回给定集合交集的成员,如果其中一个集合为不存在或为空，则返回空Set <br/>
         *
         * @param keys
         * @return 交集成员的集合
         * @createDate: 2017年12月18日 上午10:32:05
         * @author dragonSaberCaptain
         */
        public static Set<String> sinter(String... keys) {
            Set<String> result = null;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.sinter(keys);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.sinter(keys);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 这个命令等于sinter,但返回的不是结果集,而是将结果集存储在新的集合中，如果目标已存在，则覆盖。 <br/>
         *
         * @param newkey 新结果集的key
         * @param keys   比较的集合
         * @return 新集合中的记录数
         * @createDate: 2017年12月18日 上午10:33:50
         * @author dragonSaberCaptain
         */
        public static long sinterstore(String newkey, String... keys) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.sinterstore(newkey, keys);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.sinterstore(newkey, keys);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 确定一个给定的值是否存在 <br/>
         *
         * @param key
         * @param member 要判断的值
         * @return 0(不存在) 1(存在)
         * @createDate: 2017年12月18日 上午10:38:39
         * @author dragonSaberCaptain
         */
        public static boolean sismember(String key, String member) {
            boolean result = false;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.sismember(key, member);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.sismember(key, member);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 返回集合中的所有成员 <br/>
         *
         * @param key
         * @return 成员集合
         * @createDate: 2017年12月18日 上午10:40:29
         * @author dragonSaberCaptain
         */
        public static Set<String> smembers(String key) {
            Set<String> result = null;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.smembers(key);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.smembers(key);
                    jedis.close();
                }

            }
            return result;
        }

        public static Set<byte[]> smembers(byte[] key) {
            Set<byte[]> result = null;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.smembers(key);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.smembers(key);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 将成员从源集合移出放入目标集合 <br/>
         * 如果源集合不存在或不包哈指定成员，不进行任何操作，返回0<br/>
         * 否则该成员从源集合上删除，并添加到目标集合，如果目标集合中成员已存在，则只在源集合进行删除 <br/>
         *
         * @param srckey 源集合
         * @param dstkey 目标集合
         * @param member 源集合中的成员
         * @return 状态码 0(失败) 1(成功)
         * @createDate: 2017年12月18日 上午10:42:34
         * @author dragonSaberCaptain
         */
        public static long smove(String srckey, String dstkey, String member) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.smove(srckey, dstkey, member);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.smove(srckey, dstkey, member);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 从集合中删除成员 <br/>
         *
         * @param key
         * @return 被删除的成员
         * @createDate: 2017年12月18日 上午10:45:45
         * @author dragonSaberCaptain
         */
        public static String spop(String key) {
            String result = null;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.spop(key);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.spop(key);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 从集合中删除指定成员 <br/>
         *
         * @param key
         * @param member 要删除的成员
         * @return 状态码 0(失败、不存在) 1(成功)
         * @createDate: 2017年12月18日 上午10:47:07
         * @author dragonSaberCaptain
         */
        public static long srem(String key, String member) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.srem(key, member);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.srem(key, member);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 合并多个集合并返回合并后的结果，合并后的结果集合并不保存 <br/>
         *
         * @param keys
         * @return 合并后的结果集合
         * @createDate: 2017年12月18日 上午10:49:44
         * @author dragonSaberCaptain
         * see sunionstore
         */
        public static Set<String> sunion(String... keys) {
            Set<String> result = null;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.sunion(keys);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.sunion(keys);
                    jedis.close();
                }

            }

            return result;
        }

        /**
         * 合并多个集合并将合并后的结果集保存在指定的新集合中，如果新集合已经存在则覆盖 <br/>
         *
         * @param newkey 新集合的key
         * @param keys   要合并的集合
         * @return
         * @createDate: 2017年12月18日 上午10:51:51
         * @author dragonSaberCaptain
         */
        public static long sunionstore(String newkey, String... keys) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.sunionstore(newkey, keys);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.sunionstore(newkey, keys);
                    jedis.close();
                }

            }
            return result;
        }
    }

    // *******************************************SortSets*******************************************//
    public static class SortSets {
        /**
         * 向集合中增加一条记录 <br/>
         *
         * @param key
         * @param score  权重
         * @param member 要加入的值
         * @return 状态码 0(已存在,重置权重) 1(成功)
         * @createDate: 2017年12月18日 上午10:56:23
         * @author dragonSaberCaptain
         */
        public static long zadd(String key, double score, String member) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.zadd(key, score, member);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.zadd(key, score, member);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 以Map的形式向集合中增加一条记录 <br/>
         *
         * @param key
         * @param scoreMembers
         * @return 状态码 0(已存在member的值) 1(成功)
         * @createDate: 2017年12月18日 上午11:00:07
         * @author dragonSaberCaptain
         */
        public static long zadd(String key, Map<String, Double> scoreMembers) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.zadd(key, scoreMembers);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.zadd(key, scoreMembers);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 获取集合中元素的数量 <br/>
         *
         * @param key
         * @return 0(集合不存在)
         * @createDate: 2017年12月18日 上午11:05:14
         * @author dragonSaberCaptain
         */
        public static long zcard(String key) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.zcard(key);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.zcard(key);
                    jedis.close();
                }
            }
            return result;
        }

        /**
         * 获取指定权重区间内集合的数量 <br/>
         *
         * @param key
         * @param min 最小排序位置
         * @param max 最大排序位置
         * @return
         * @createDate: 2017年12月18日 上午11:06:57
         * @author dragonSaberCaptain
         */
        public static long zcount(String key, double min, double max) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.zcount(key, min, max);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.zcount(key, min, max);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 获得set的长度 <br/>
         *
         * @param key
         * @return
         * @createDate: 2017年12月18日 上午11:11:15
         * @author dragonSaberCaptain
         */
        public static int zlength(String key) {
            return zrange(key, 0, -1).size();
        }

        /**
         * 权重增加给定值，如果给定的member已存在 <br/>
         *
         * @param key
         * @param score  要增的权重
         * @param member 要插入的值
         * @return 增后的权重
         * @createDate: 2017年12月18日 上午11:13:18
         * @author dragonSaberCaptain
         */
        public static Double zincrby(String key, double score, String member) {
            Double result = -1D;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.zincrby(key, score, member);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.zincrby(key, score, member);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 返回指定位置的集合元素,0为第一个元素，-1为最后一个元素 <br/>
         *
         * @param key
         * @param start 开始位置(包含)
         * @param end   结束位置(包含)
         * @return Set<String>
         * @createDate: 2017年12月18日 上午11:14:22
         * @author dragonSaberCaptain
         */
        public static Set<String> zrange(String key, int start, int end) {
            Set<String> result = null;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.zrange(key, start, end);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.zrange(key, start, end);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 返回指定权重区间的元素集合 <br/>
         *
         * @param key
         * @param min 上限权重
         * @param max 下限权重
         * @return
         * @createDate: 2017年12月18日 上午11:15:53
         * @author dragonSaberCaptain
         */
        public static Set<String> zrangeByScore(String key, double min,
                                                double max) {
            Set<String> result = null;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.zrangeByScore(key, min, max);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.zrangeByScore(key, min, max);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * Redis Zrevrangebyscore 返回有序集中指定分数区间内的所有的成员。有序集成员按分数值递减(从大到小)的次序排列。
         * 具有相同分数值的成员按字典序的逆序(reverse lexicographical order )排列。
         * 除了成员按分数值递减的次序排列这一点外， ZREVRANGEBYSCORE 命令的其他方面和 ZRANGEBYSCORE 命令一样。
         *
         * @param key
         * @param max
         * @param min
         * @param offset
         * @param count
         * @return 指定区间内，带有分数值(可选)的有序集成员的列表。
         */
        public static Set<String> zrevrangebyscore(String key, String max,
                                                   String min, int offset, int count) {
            Set<String> result = null;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.zrevrangeByScore(key, min, max);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.zrevrangeByScore(key, offset, count);
                    jedis.close();
                }

            }

            return result;
        }

        /**
         * 获取指定值在集合中的位置，集合排序从低到高 <br/>
         *
         * @param key
         * @param member
         * @return 位置
         * @createDate: 2017年12月18日 上午11:16:55
         * @author dragonSaberCaptain
         * see zrevrank
         */
        public static long zrank(String key, String member) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.zrank(key, member);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.zrank(key, member);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 获取指定值在集合中的位置，集合排序从高到低 <br/>
         *
         * @param key
         * @param member
         * @return 位置
         * @createDate: 2017年12月18日 上午11:19:05
         * @author dragonSaberCaptain
         */
        public static long zrevrank(String key, String member) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.zrevrank(key, member);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.zrevrank(key, member);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 从集合中删除成员 <br/>
         *
         * @param key
         * @param member
         * @return 0(失败) 1(成功)
         * @createDate: 2017年12月18日 上午11:20:27
         * @author dragonSaberCaptain
         */
        public static long zrem(String key, String member) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.zrem(key, member);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.zrem(key, member);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 删除 <br/>
         *
         * @param key
         * @return
         * @createDate: 2017年12月18日 上午11:21:58
         * @author dragonSaberCaptain
         */
        public static long zrem(String key) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.del(key);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.del(key);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 删除给定位置区间的元素 <br/>
         *
         * @param key
         * @param start 开始区间，从0开始(包含)
         * @param end   结束区间,-1为最后一个元素(包含)
         * @return 删除的数量
         * @createDate: 2017年12月18日 上午11:22:52
         * @author dragonSaberCaptain
         */
        public static long zremrangeByRank(String key, int start, int end) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.zremrangeByRank(key, start, end);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.zremrangeByRank(key, start, end);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 删除给定权重区间的元素 <br/>
         *
         * @param key
         * @param min 下限权重(包含)
         * @param max 上限权重(包含)
         * @return 删除的数量
         * @createDate: 2017年12月18日 上午11:23:44
         * @author dragonSaberCaptain
         */
        public static long zremrangeByScore(String key, double min, double max) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.zremrangeByScore(key, min, max);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.zremrangeByScore(key, min, max);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 获取给定区间的元素，原始按照权重由高到低排序 <br/>
         *
         * @param key
         * @param start
         * @param end
         * @return Set<String> 指定区间内，带有分数值(可选)的有序集成员的列表。
         * @createDate: 2017年12月18日 上午11:25:14
         * @author dragonSaberCaptain
         */
        public static Set<String> zrevrange(String key, int start, int end) {
            Set<String> result = null;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.zrevrange(key, start, end);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.zrevrange(key, start, end);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 获取给定值在集合中的权重 <br/>
         *
         * @param key
         * @param memebr
         * @return 权重
         * @createDate: 2017年12月18日 上午11:29:06
         * @author dragonSaberCaptain
         */
        public static Double zscore(String key, String memebr) {
            Double result = null;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.zscore(key, memebr);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.zscore(key, memebr);
                    jedis.close();
                }

            }
            return result;
        }
    }

    // *******************************************Hashs*******************************************//
    public static class Hashs {
        /**
         * 从hash中删除指定的存储 <br/>
         *
         * @param key
         * @param fieid 存储的名字
         * @return 状态码 0(失败) 1(成功)
         * @createDate: 2017年12月18日 上午11:30:17
         * @author dragonSaberCaptain
         */
        public static long hdel(String key, String fieid) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.hdel(key, fieid);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.hdel(key, fieid);
                    jedis.close();
                }

            }
            return result;
        }

        public static long hdel(String key) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.del(key);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.del(key);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 判断hash中指定的存储是否存在 <br/>
         *
         * @param key
         * @param fieid 存储的名字
         * @return true(存在)
         * @createDate: 2017年12月18日 上午11:33:04
         * @author dragonSaberCaptain
         */
        public static boolean hexists(String key, String fieid) {
            boolean result = false;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.hexists(key, fieid);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.hexists(key, fieid);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 返回hash中指定存储的值 <br/>
         *
         * @param key
         * @param fieid 存储的名字
         * @return 存储对应的值
         * @createDate: 2017年12月18日 上午11:34:30
         * @author dragonSaberCaptain
         */
        public static String hget(String key, String fieid) {
            String result = null;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.hget(key, fieid);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.hget(key, fieid);
                    jedis.close();
                }

            }
            return result;
        }

        public static byte[] hget(byte[] key, byte[] fieid) {
            byte[] result = null;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.hget(key, fieid);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.hget(key, fieid);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 以Map的形式返回hash中的所有存储和值 <br/>
         *
         * @param key
         * @createDate: 2017年12月18日 上午11:37:29
         * @author dragonSaberCaptain
         */
        public static Map<String, String> hgetAll(String key) {
            Map<String, String> result = null;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.hgetAll(key);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.hgetAll(key);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 添加一个对应关系 <br/>
         *
         * @param key
         * @param fieid
         * @param value
         * @return 状态码 0(失败、fieid已存在将更新) 1(成功)
         * @createDate: 2017年12月18日 上午11:39:08
         * @author dragonSaberCaptain
         */
        public static long hset(String key, String fieid, String value) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.hset(key, fieid, value);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.hset(key, fieid, value);
                    jedis.close();
                }

            }
            return result;
        }

        public static long hset(byte[] key, byte[] fieid, byte[] value) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.hset(key, fieid, value);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.hset(key, fieid, value);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 添加对应关系，只有在fieid不存在时才执行 <br/>
         *
         * @param key
         * @param fieid
         * @param value
         * @return 状态码 0(失败、fieid已存在) 1(成功)
         * @createDate: 2017年12月18日 上午11:44:38
         * @author dragonSaberCaptain
         */
        public static long hsetnx(String key, String fieid, String value) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.hsetnx(key, fieid, value);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.hsetnx(key, fieid, value);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 获取hash中value的集合 <br/>
         *
         * @param key
         * @return List<String>
         * @createDate: 2017年12月18日 上午11:46:34
         * @author dragonSaberCaptain
         */
        public static List<String> hvals(String key) {
            List<String> result = null;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.hvals(key);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.hvals(key);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 在指定的存储位置加上指定的数字，存储位置的值必须可转为数字类型 <br/>
         *
         * @param key
         * @param fieid 存储位置
         * @param value 要增加的值,可以是负数
         * @return 增加指定数字后，存储位置的值
         * @createDate: 2017年12月18日 上午11:47:43
         * @author dragonSaberCaptain
         */
        public static long hincrby(String key, String fieid, long value) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.hincrBy(key, fieid, value);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.hincrBy(key, fieid, value);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 返回指定hash中的所有存储名字,类似Map中的keySet方法 <br/>
         *
         * @param key
         * @return Set<String> 存储名称的集合
         * @createDate: 2017年12月18日 上午11:49:18
         * @author dragonSaberCaptain
         */
        public static Set<String> hkeys(String key) {
            Set<String> result = null;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.hkeys(key);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.hkeys(key);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 获取hash中存储的个数，类似Map中size方法 <br/>
         *
         * @param key
         * @return 存储的个数
         * @createDate: 2017年12月18日 上午11:50:24
         * @author dragonSaberCaptain
         */
        public static long hlen(String key) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.hlen(key);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.hlen(key);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 根据多个key，获取对应的value，返回List,如果指定的key不存在,List对应位置为null <br/>
         *
         * @param key
         * @param fieids 存储位置
         * @return List<String>
         * @createDate: 2017年12月18日 上午11:52:28
         * @author dragonSaberCaptain
         */
        public static List<String> hmget(String key, String... fieids) {
            List<String> result = null;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.hmget(key, fieids);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.hmget(key, fieids);
                    jedis.close();
                }

            }
            return result;
        }

        public static List<byte[]> hmget(byte[] key, byte[]... fieids) {
            List<byte[]> result = null;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.hmget(key, fieids);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.hmget(key, fieids);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 添加对应关系，如果对应关系已存在，则覆盖 <br/>
         *
         * @param key
         * @param map 对应关系
         * @return 状态，成功返回OK
         * @createDate: 2017年12月18日 上午11:54:01
         * @author dragonSaberCaptain
         */
        public static String hmset(String key, Map<String, String> map) {
            String result = null;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.hmset(key, map);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.hmset(key, map);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 添加对应关系，如果对应关系已存在，则覆盖 <br/>
         *
         * @param key
         * @param map 对应关系
         * @return 状态，成功返回OK
         * @createDate: 2017年12月18日 上午11:55:13
         * @author dragonSaberCaptain
         */
        public static String hmset(byte[] key, Map<byte[], byte[]> map) {
            String result = null;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.hmset(key, map);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.hmset(key, map);
                    jedis.close();
                }

            }
            return result;
        }

    }

    // *******************************************Strings*******************************************//
    public static class Strings {
        /**
         * 根据key获取记录 <br/>
         *
         * @param key
         * @return 值
         * @createDate: 2017年12月18日 上午11:56:34
         * @author dragonSaberCaptain
         */
        public static String get(String key) {
            String result = null;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.get(key);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.get(key);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 根据key获取记录 <br/>
         *
         * @param key
         * @return 值
         * @createDate: 2017年12月18日 上午11:57:07
         * @author dragonSaberCaptain
         */
        public static byte[] get(byte[] key) {
            byte[] result = null;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.get(key);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.get(key);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 添加有过期时间的记录 <br/>
         *
         * @param key
         * @param value
         * @param seconds 过期时间，以秒为单位
         * @return 操作状态
         * @createDate: 2017年12月18日 上午11:57:50
         * @author dragonSaberCaptain
         */
        public static String setEx(String key, String value, int seconds) {
            String result = null;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.setex(key, seconds, value);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.setex(key, seconds, value);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 添加有过期时间的记录 <br/>
         *
         * @param key
         * @param seconds 过期时间，以秒为单位
         * @param value
         * @return 操作状态
         * @createDate: 2017年12月18日 上午11:58:46
         * @author dragonSaberCaptain
         */
        public static String setEx(byte[] key, int seconds, byte[] value) {
            String result = null;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.setex(key, seconds, value);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.setex(key, seconds, value);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 添加一条记录，仅当给定的key不存在时才插入 <br/>
         *
         * @param key
         * @param value
         * @return 状态码 0(未插入，key存在) 1(插入成功且key不存在)
         * @createDate: 2017年12月18日 上午11:59:45
         * @author dragonSaberCaptain
         */
        public static long setnx(String key, String value) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.setnx(key, value);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.setnx(key, value);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 添加记录,如果记录已存在将覆盖原有的value
         *
         * @param key
         * @param value
         * @return 状态码
         */
        public static String set(String key, String value) {
            return set(SafeEncoder.encode(key), SafeEncoder.encode(value));
        }

        /**
         * 添加记录,如果记录已存在将覆盖原有的value
         *
         * @param key
         * @param value
         * @return 状态码
         */
        public static String set(String key, byte[] value) {
            return set(SafeEncoder.encode(key), value);
        }

        /**
         * 添加记录,如果记录已存在将覆盖原有的value
         *
         * @param key
         * @param value
         * @return 状态码
         */
        public static String set(byte[] key, byte[] value) {
            String result = null;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.set(key, value);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.set(key, value);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 从指定位置开始插入数据，插入的数据会覆盖指定位置以后的数据<br/>
         * 例:String str1="123456789";<br/>
         * 对str1操作后setRange(key,4,0000)，str1="123400009";
         *
         * @param key
         * @param offset
         * @param value
         * @return value的长度
         * @createDate: 2017年12月18日 下午1:03:18
         * @author dragonSaberCaptain
         */
        public static long setRange(String key, long offset, String value) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.setrange(key, offset, value);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.setrange(key, offset, value);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 在指定的key中追加value <br/>
         *
         * @param key
         * @param value
         * @return 追加后value的长度
         * @createDate: 2017年12月18日 下午1:07:15
         * @author dragonSaberCaptain
         */
        public static long append(String key, String value) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.append(key, value);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.append(key, value);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 将key对应的value减去指定的值，只有value可以转为数字时该方法才可用 <br/>
         *
         * @param key
         * @param number 要减去的值
         * @return 减指定值后的值
         * @createDate: 2017年12月18日 下午1:08:12
         * @author dragonSaberCaptain
         */
        public static long decrBy(String key, long number) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.decrBy(key, number);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.decrBy(key, number);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * <b>可以作为获取唯一id的方法</b><br/>
         * 将key对应的value加上指定的值，只有value可以转为数字时该方法才可用
         *
         * @param key
         * @param number 要减去的值
         * @return 相加后的值
         * @createDate: 2017年12月18日 下午1:08:55
         * @author dragonSaberCaptain
         */
        public static long incrBy(String key, long number) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.incrBy(key, number);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.incrBy(key, number);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 对指定key对应的value进行截取 <br/>
         *
         * @param key
         * @param startOffset 开始位置(包含)
         * @param endOffset   结束位置(包含)
         * @return 截取的值
         * @createDate: 2017年12月18日 下午1:12:02
         * @author dragonSaberCaptain
         */
        public static String getrange(String key, long startOffset,
                                      long endOffset) {
            String result = null;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.getrange(key, startOffset, endOffset);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.getrange(key, startOffset, endOffset);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 获取并设置指定key对应的value<br/>
         * 如果key存在返回之前的value,否则返回null
         *
         * @param key
         * @param value
         * @return 原始value或null
         * @createDate: 2017年12月18日 下午1:13:16
         * @author dragonSaberCaptain
         */
        public static String getSet(String key, String value) {
            String result = null;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.getSet(key, value);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.getSet(key, value);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 批量获取记录,如果指定的key不存在返回List的对应位置将是null <br/>
         *
         * @param keys
         * @return List<String> 值得集合
         * @createDate: 2017年12月18日 下午1:14:08
         * @author dragonSaberCaptain
         */
        public static List<String> mget(String... keys) {
            List<String> result = null;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.mget(keys);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.mget(keys);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 批量存储记录 <br/>
         *
         * @param keysvalues 例:keysvalues="key1","value1","key2","value2";
         * @return 状态码
         * @createDate: 2017年12月18日 下午1:14:56
         * @author dragonSaberCaptain
         */
        public static String mset(String... keysvalues) {
            String result = null;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.mset(keysvalues);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.mset(keysvalues);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 获取key对应的值的长度 <br/>
         *
         * @param key
         * @return value值得长度
         * @createDate: 2017年12月18日 下午1:22:02
         * @author dragonSaberCaptain
         */
        public static long strlen(String key) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.strlen(key);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.strlen(key);
                    jedis.close();
                }

            }
            return result;
        }
    }

    // *******************************************Lists*******************************************//
    public static class Lists {
        /**
         * List长度
         *
         * @param key
         * @return 长度
         */
        public static long llen(String key) {
            return llen(SafeEncoder.encode(key));
        }

        /**
         * List长度 <br/>
         *
         * @param key
         * @return 长度
         * @createDate: 2017年12月18日 下午1:23:04
         * @author dragonSaberCaptain
         */
        public static long llen(byte[] key) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.llen(key);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.llen(key);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 覆盖操作,将覆盖List中指定位置的值
         *
         * @param key
         * @param index 位置
         * @param value 值
         * @return 状态码
         */
        public static String lset(byte[] key, int index, byte[] value) {
            String result = null;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.lset(key, index, value);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.lset(key, index, value);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 覆盖操作,将覆盖List中指定位置的值
         *
         * @param key
         * @param index 位置
         * @param value 值
         * @return 状态码
         */
        public static String lset(String key, int index, String value) {
            return lset(SafeEncoder.encode(key), index,
                    SafeEncoder.encode(value));
        }

        /**
         * 在value的相对位置插入记录
         *
         * @param key
         * @param where 前面插入或后面插入
         * @param pivot 相对位置的内容
         * @param value 插入的内容
         * @return 记录总数
         */
        public static long linsert(String key, ListPosition where,
                                   String pivot, String value) {
            return linsert(SafeEncoder.encode(key), where,
                    SafeEncoder.encode(pivot), SafeEncoder.encode(value));
        }

        /**
         * 在指定位置插入记录 <br/>
         *
         * @param key
         * @param where 从前面插入还是后面插入(=.=)
         * @param pivot 相对位置的内容
         * @param value 插入的内容
         * @return 记录总数
         * @createDate: 2017年12月18日 下午1:25:25
         * @author dragonSaberCaptain
         */
        public static long linsert(byte[] key, ListPosition where,
                                   byte[] pivot, byte[] value) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.linsert(key, where, pivot, value);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.linsert(key, where, pivot, value);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 获取List中指定位置的值
         *
         * @param key
         * @param index 位置
         * @return 值
         **/
        public static String lindex(String key, int index) {
            return SafeEncoder.encode(lindex(SafeEncoder.encode(key), index));
        }

        /**
         * 获取List中指定位置的值 <br/>
         *
         * @param key
         * @param index 位置
         * @return 值
         * @createDate: 2017年12月18日 下午1:27:14
         * @author dragonSaberCaptain
         */
        public static byte[] lindex(byte[] key, int index) {
            byte[] result = null;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.lindex(key, index);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.lindex(key, index);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 将List中的第一条记录移出List
         *
         * @param key
         * @return 移出的记录
         */
        public static String lpop(String key) {
            return SafeEncoder.encode(lpop(SafeEncoder.encode(key)));
        }

        /**
         * 将List中的第一条记录移出List <br/>
         *
         * @param key
         * @return 移出的记录
         * @createDate: 2017年12月18日 下午1:28:59
         * @author dragonSaberCaptain
         */
        public static byte[] lpop(byte[] key) {
            byte[] result = null;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.lpop(key);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.lpop(key);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 将List中最后第一条记录移出List <br/>
         *
         * @param key
         * @return 移出的记录
         * @createDate: 2017年12月18日 下午1:29:37
         * @author dragonSaberCaptain
         */
        public static String rpop(String key) {
            String result = null;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.rpop(key);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.rpop(key);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 向List尾部追加记录
         *
         * @param key
         * @param value
         * @return 记录总数
         */
        public static long lpush(String key, String value) {
            return lpush(SafeEncoder.encode(key), SafeEncoder.encode(value));
        }

        /**
         * 向List头部追加记录 <br/>
         *
         * @param key
         * @param value
         * @return 记录总数
         * @createDate: 2017年12月18日 下午1:30:47
         * @author dragonSaberCaptain
         */
        public static long rpush(String key, String value) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.rpush(key, value);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.rpush(key, value);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 向List头部追加记录 <br/>
         *
         * @param key
         * @param value
         * @return 记录总数
         * @createDate: 2017年12月18日 下午1:31:49
         * @author dragonSaberCaptain
         */
        public static long rpush(byte[] key, byte[] value) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.rpush(key, value);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.rpush(key, value);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 向List中追加记录 <br/>
         *
         * @param key
         * @param value
         * @return 记录总数
         * @createDate: 2017年12月18日 下午1:32:52
         * @author dragonSaberCaptain
         */
        public static long lpush(byte[] key, byte[] value) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.lpush(key, value);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.lpush(key, value);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 获取指定范围的记录，可以做为分页使用 <br/>
         *
         * @param key
         * @param start
         * @param end
         * @return
         * @createDate: 2017年12月18日 下午1:33:34
         * @author dragonSaberCaptain
         */
        public static List<String> lrange(String key, long start, long end) {
            List<String> result = null;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.lrange(key, start, end);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.lrange(key, start, end);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 获取指定范围的记录，可以做为分页使用 <br/>
         *
         * @param key
         * @param start
         * @param end   如果为负数，则尾部开始计算
         * @return
         * @createDate: 2017年12月18日 下午1:34:14
         * @author dragonSaberCaptain
         */
        public static List<byte[]> lrange(byte[] key, int start, int end) {
            List<byte[]> result = null;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.lrange(key, start, end);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.lrange(key, start, end);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 删除List中num条记录，被删除的记录值为value <br/>
         *
         * @param key
         * @param num   要删除的数量，如果为负数则从List的尾部检查并删除符合的记录
         * @param value 要匹配的值
         * @return 删除后的List中的记录数
         * @createDate: 2017年12月18日 下午1:35:09
         * @author dragonSaberCaptain
         */
        public static long lrem(byte[] key, int num, byte[] value) {
            long result = -1;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.lrem(key, num, value);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.lrem(key, num, value);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 删除List中c条记录，被删除的记录值为value
         *
         * @param key
         * @param c     要删除的数量，如果为负数则从List的尾部检查并删除符合的记录
         * @param value 要匹配的值
         * @return 删除后的List中的记录数
         */
        public static long lrem(String key, int c, String value) {
            return lrem(SafeEncoder.encode(key), c, SafeEncoder.encode(value));
        }

        /**
         * 算是删除吧，只保留start与end之间的记录 <br/>
         *
         * @param key
         * @param start 记录的开始位置(0表示第一条记录)
         * @param end   记录的结束位置（如果为-1则表示最后一个，-2，-3以此类推）
         * @return 执行状态码
         * @createDate: 2017年12月18日 下午1:38:15
         * @author dragonSaberCaptain
         */
        public static String ltrim(byte[] key, int start, int end) {
            String result = null;
            if (clusterPattern) {
                JedisCluster jedisCluster = getJedisCluster();
                if (jedisCluster != null) {
                    result = jedisCluster.ltrim(key, start, end);
                }

            } else {
                Jedis jedis = getJedis();
                if (jedis != null) {
                    result = jedis.ltrim(key, start, end);
                    jedis.close();
                }

            }
            return result;
        }

        /**
         * 算是删除吧，只保留start与end之间的记录
         *
         * @param key
         * @param start 记录的开始位置(0表示第一条记录)
         * @param end   记录的结束位置（如果为-1则表示最后一个，-2，-3以此类推）
         * @return 执行状态码
         */
        public static String ltrim(String key, int start, int end) {
            return ltrim(SafeEncoder.encode(key), start, end);
        }
    }
}
