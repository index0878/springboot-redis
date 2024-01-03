package com.ec.concurrency.dao;

import java.util.List;

import com.ec.concurrency.entity.SeckillGoods;

public interface SeckillGoodsDao {
	public List<SeckillGoods> findSeckillGoodsForOpen();
	public List<SeckillGoods> findSeckillGoodsForClose();
	public void update(SeckillGoods seckillGoods);
	public SeckillGoods findById(long id);
}
