package com.ec.concurrency.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ec.concurrency.dao.SeckillGoodsDao;
import com.ec.concurrency.entity.SeckillGoods;

import jakarta.annotation.Resource;

@Component
public class SecKillStart {
	
	@Autowired
	SeckillGoodsDao dao;

	@Resource
	RedisTemplate redisTemplate;
	
	@Scheduled(cron="0/5 * * * * ?")//可依照秒殺活動開始時間去設定，這邊我是把活動時間存在資料庫裡
	public void startSecKill() {
		System.out.println("secKillStart");
		
		List<SeckillGoods> list = dao.findSeckillGoodsForOpen();
		
		for(SeckillGoods seckillGoods : list) {
			//刪除舊的活動資料
			redisTemplate.delete("seckillGoods:count:"+seckillGoods.getGoodId());
			for(int i = 0 ; i < seckillGoods.getCount() ; i++) {
				System.out.println(i);
				redisTemplate.opsForList().rightPush("seckillGoods:count:"+seckillGoods.getGoodId(), seckillGoods.getGoodId());
			}
			seckillGoods.setStatus("1");
			dao.update(seckillGoods);
			
		}
		
	}
	@Scheduled(cron="0/5 * * * * ?")
	public void endSecKill() {
		System.out.println("secKillEnd");
		
		List<SeckillGoods> list = dao.findSeckillGoodsForClose();
		
		for(SeckillGoods seckillGoods : list) {
			redisTemplate.delete("seckillGoods:count:"+seckillGoods.getGoodId());
			seckillGoods.setStatus("2");
			dao.update(seckillGoods);
			
		}
		
	}
}
