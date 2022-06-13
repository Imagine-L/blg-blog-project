package top.liubaiblog.blog.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author 留白
 * @description
 */
@Component
@SuppressWarnings({"all"})
public class RedisUtils {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * @return 自动注入的 redisTemplate
     */
    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    /**
     * 获取keys集合
     *
     * @param pattern 模糊查询方式
     * @return keys集合
     */
    public Set<String> keys(String pattern) {
        try {
            return redisTemplate.keys(pattern);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 设置某个键的过期时间
     *
     * @param key      键
     * @param timeout  过期时间
     * @param timeUnit 单位
     * @return 是否成功
     */
    public Boolean expire(String key, long timeout, TimeUnit timeUnit) {
        try {
            return redisTemplate.expire(key, timeout, timeUnit);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取某个键剩余的过期时间
     *
     * @param key      键
     * @param timeUnit 单位
     * @return 过期时间
     */
    public Long getExpire(String key, TimeUnit timeUnit) {
        try {
            return redisTemplate.getExpire(key, timeUnit);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除某个键
     *
     * @param key 键
     * @return 是否成功
     */
    public Boolean delete(String key) {
        try {
            return redisTemplate.delete(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取String类型的键值对
     *
     * @param key 键
     * @return 值
     */
    public String valGet(String key) {
        try {
            Object val = redisTemplate.opsForValue().get(key);
            return val != null ? val.toString() : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取指定键值对，并且要求值的数据类型
     *
     * @param key   键
     * @param clazz 返回类型
     * @return 值
     */
    public <T> T valGetObject(String key, Class<T> clazz) {
        try {
            Object val = redisTemplate.opsForValue().get(key);
            return clazz.isInstance(val) ? (T) val : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 插入一个键值对
     *
     * @param key 键
     * @param val 值
     * @return 是否成功
     */
    public Boolean valSet(String key, Object val) {
        try {
            redisTemplate.opsForValue().set(key, val);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 插入一个键值对，并设置有效期
     *
     * @param key      键
     * @param val      值
     * @param expire   有效期
     * @param timeUnit 单位
     * @return 是否成功
     */
    public Boolean valSet(String key, Object val, long expire, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForValue().set(key, val, expire, timeUnit);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 如果指定key不存在，则插入一个键值对
     *
     * @param key 键
     * @param val 值
     * @return 是否成功
     */
    public Boolean valSetIfAbsent(String key, Object val) {
        try {
            redisTemplate.opsForValue().setIfAbsent(key, val);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 如果指定key不存在，则插入一个键值对，并设置有效期
     *
     * @param key      键
     * @param val      值
     * @param expire   有效期
     * @param timeUnit 单位
     * @return 是否成功
     */
    public Boolean valSetIfAbsent(String key, Object val, long expire, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForValue().setIfAbsent(key, val, expire, timeUnit);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 为指定键的值进行增量
     *
     * @param key  键
     * @param incr 增量值
     * @return 结果
     */
    public Long valIncrement(String key, long incr) {
        try {
            return redisTemplate.opsForValue().increment(key, incr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 为指定键的值进行减量
     *
     * @param key  键
     * @param incr 减量值
     * @return 结果
     */
    public Long valDecrement(String key, long incr) {
        try {
            return redisTemplate.opsForValue().decrement(key, incr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 在列表左边插入元素
     *
     * @param key 键
     * @param val 值
     * @return 添加的元素个数
     */
    public Long listLeftPush(String key, Object... val) {
        try {
            return redisTemplate.opsForList().leftPushAll(key, val);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 在列表右边插入元素
     *
     * @param key 键
     * @param val 值
     * @return 添加的元素个数
     */
    public Long listRightPush(String key, Object... val) {
        try {
            return redisTemplate.opsForList().rightPushAll(key, val);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取从指定集合左边弹出的对象
     *
     * @param key   键
     * @param clazz 对象类型
     * @return 弹出的对象
     */
    public <T> T listLeftPop(String key, Class<T> clazz) {
        try {
            Object val = redisTemplate.opsForList().leftPop(key);
            return clazz.isInstance(val) ? (T) val : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取从指定集合右边弹出的对象
     *
     * @param key   键
     * @param clazz 对象类型
     * @return 弹出的对象
     */
    public <T> T listRightPop(String key, Class<T> clazz) {
        try {
            Object val = redisTemplate.opsForList().rightPop(key);
            return clazz.isInstance(val) ? (T) val : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 求集合的长度
     *
     * @param key 键
     * @return 长度
     */
    public Long listSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 遍历redis集合
     *
     * @param key   键
     * @param start 起始位置
     * @param stop  结束位置
     * @return 集合
     */
    public List<Object> listRange(String key, long start, long stop) {
        try {
            return redisTemplate.opsForList().range(key, start, stop);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取某个集合index位置的值
     *
     * @param key   键
     * @param index 位置
     * @param clazz 值类型
     * @return 值
     */
    public <T> T listIndex(String key, long index, Class<T> clazz) {
        try {
            Object val = redisTemplate.opsForList().index(key, index);
            return clazz.isInstance(val) ? (T) val : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 向集合中插入元素
     *
     * @param key    键
     * @param member 元素
     * @return 插入的元素个数
     */
    public Long setAdd(String key, Object... member) {
        try {
            return redisTemplate.opsForSet().add(key, member);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取Set集合所有值
     *
     * @param key 键
     * @return 所有值
     */
    public Set<Object> setMembers(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判断set集合中某个值是否存在
     *
     * @param key    键
     * @param member 值
     * @return 是否存在
     */
    public Boolean setIsMember(String key, Object member) {
        try {
            return redisTemplate.opsForSet().isMember(key, member);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取某个set集合的长度
     *
     * @param key 键
     * @return 长度
     */
    public Long setSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从set集合中删除元素
     *
     * @param key    键
     * @param member 元素
     * @return 删除的数目
     */
    public Long setRemove(String key, Object... member) {
        try {
            return redisTemplate.opsForSet().remove(key, member);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从set集合中随机弹出一个值
     *
     * @param key   键
     * @param clazz 弹出值的类型
     * @return 弹出的值
     */
    public <T> T setPop(String key, Class<T> clazz) {
        try {
            Object val = redisTemplate.opsForSet().pop(key);
            return clazz.isInstance(val) ? (T) val : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从set集合中随机弹出多个值
     *
     * @param key   键
     * @param count 弹出的值的个数
     * @return 弹出的值集合
     */
    public List<Object> setPop(String key, long count) {
        try {
            return redisTemplate.opsForSet().pop(key, count);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 添加zset集合元素
     *
     * @param key    键
     * @param member 元素
     * @param score  分数
     * @return 是否成功
     */
    public Boolean zsetAdd(String key, Object member, double score) {
        try {
            return redisTemplate.opsForZSet().add(key, member, score);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 遍历zset集合
     *
     * @param key   键
     * @param start 起始位置
     * @param stop  结束位置
     * @return 集合
     */
    public Set<Object> zsetRange(String key, long start, long stop) {
        try {
            return redisTemplate.opsForZSet().range(key, start, stop);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取zset集合中分数介于min到max之间的元素
     *
     * @param key 键
     * @param min 最低分
     * @param max 最高分
     * @return 元素集合
     */
    public Set<Object> zsetRangeByScore(String key, long min, long max) {
        try {
            return redisTemplate.opsForZSet().rangeByScore(key, min, max);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将zset集合中的某个值的分数进行增量
     *
     * @param key    键
     * @param member 值
     * @param score  增量分数
     * @return 增量后的分数
     */
    public Double zsetIncrementScore(String key, Object member, double score) {
        try {
            return redisTemplate.opsForZSet().incrementScore(key, member, score);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从zset集合中删除指定元素
     *
     * @param key    键
     * @param member 元素
     * @return 删除的个数
     */
    public Long zsetRemove(String key, Object... member) {
        try {
            return redisTemplate.opsForZSet().remove(key, member);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取元素在zset集合中的排名(从0开始)
     *
     * @param key    键
     * @param member 元素
     * @return 排名
     */
    public Long zsetRank(String key, Object member) {
        try {
            return redisTemplate.opsForZSet().rank(key, member);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 向hash表中插入属性及其值
     *
     * @param key   键
     * @param field 属性
     * @param val   属性值
     * @return 是否成功
     */
    public boolean hashSet(String key, String field, Object val) {
        try {
            redisTemplate.opsForHash().put(key, field, val);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向hash表中插入属性及其值
     *
     * @param key 键
     * @param map 属性及其值组成的集合
     * @return 是否成功
     */
    public boolean hashSetAll(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 如果属性值不存在,则向hash表中插入属性及其值
     *
     * @param key   键
     * @param field 属性
     * @param val   属性值
     * @return 是否成功
     */
    public Boolean hashSetIfAbsent(String key, String field, Object val) {
        try {
            return redisTemplate.opsForHash().putIfAbsent(key, field, val);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 从hash表中获取某个属性的值
     *
     * @param key   键
     * @param field 属性
     * @param clazz 属性值类型
     * @return 属性值
     */
    public <T> T hashGet(String key, String field, Class<T> clazz) {
        try {
            Object val = redisTemplate.opsForHash().get(key, field);
            return clazz.isInstance(val) ? (T) val : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除hash表中的指定属性
     *
     * @param key   键
     * @param field 属性
     * @return 删除的个数
     */
    public Long hashDelete(String key, String... field) {
        try {
            return redisTemplate.opsForHash().delete(key, field);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判断hash表的某个属性是否存在
     *
     * @param key   键
     * @param field 属性
     * @return 是否存在
     */
    public Boolean hashExists(String key, String field) {
        try {
            return redisTemplate.opsForHash().hasKey(key, field);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取hash表的所有属性
     *
     * @param key 键
     * @return 属性集合
     */
    public Set<Object> hashKeys(String key) {
        try {
            return redisTemplate.opsForHash().keys(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取hash表的所有属性值
     *
     * @param key 键
     * @return 属性值集合
     */
    public List<Object> hashValues(String key) {
        try {
            return redisTemplate.opsForHash().values(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 为hash表的某个属性的值进行增量
     *
     * @param key   键
     * @param field 属性
     * @param incr  增量值
     * @return 增量后的大小
     */
    public Double hashIncrement(String key, String field, double incr) {
        try {
            return redisTemplate.opsForHash().increment(key, field, incr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
