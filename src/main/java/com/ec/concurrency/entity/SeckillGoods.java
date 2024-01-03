package com.ec.concurrency.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class SeckillGoods implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = -4408294878131189605L;

	private long goodId;
	private int count;
	private String status;
	private Date startTime;
	private Date endTime;
}
