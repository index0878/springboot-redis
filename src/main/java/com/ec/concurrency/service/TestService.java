package com.ec.concurrency.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.ec.concurrency.dao.ProductDao;
import com.ec.concurrency.dao.SeckillGoodsDao;
import com.ec.concurrency.entity.Product;
import com.ec.concurrency.entity.SeckillGoods;
import com.ec.concurrency.exception.SeckillException;

import jakarta.annotation.Resource;

@Service
public class TestService {
	
	@Resource
	private ProductDao productDao;
	@Resource
	private RedisTemplate redisTemplate;
	@Resource
	private SeckillGoodsDao seckillGoodsDao;
	
	//把第一次抓取的結果放入緩存，第二次則會去緩存查看是否已有存入並抓取使用
	//，若緩存已有不會再次執行裡面的程式
	@Cacheable(value="product") //redis的key格式，value事前綴非值，會自動依傳入的值產生後面的序號 例:product::1 product::2
	public Product findProductById(Long id) {
		System.out.println("service"+id);
		return productDao.findById(id);
	}
	
	public Map<String,String> buySecKillGoods(String account, long goodsid) throws SeckillException {
		System.out.println(account);

		Map<String,String> map = new HashMap<>();
		SeckillGoods seckillGoods = seckillGoodsDao.findById(goodsid);
		if(seckillGoods != null && seckillGoods.getStatus().equals("1")) {
			Integer index = (Integer) redisTemplate.opsForList().rightPop("seckillGoods:count:"+goodsid);
			if(null == index ) {
				map.put("status", "F");
				map.put("msg", "已被搶購一空");
//				System.out.println("已被搶購一空");
			}else {
				boolean isExist = redisTemplate.opsForSet().isMember("seckillGoods:user:" + goodsid, account);
				if(!isExist) {
					redisTemplate.opsForSet().add("seckillGoods:user:"+ goodsid, account);
					map.put("status", "T");
					map.put("msg", "已購買成功");
					System.out.println("已購買成功" + account);
					
				}else {
					redisTemplate.opsForList().rightPush("seckillGoods:count:"+goodsid, goodsid);
					map.put("status", "F");
					map.put("msg", "請勿重新購買");
//					System.out.println("請勿重新購買");
					
				}
			}
			
		}else {
			throw new SeckillException("該商品並無在此次活動中");
		}
		return map;
	}
}
