package com.ec.concurrency.entity;

import java.io.Serializable;

import lombok.Data;
import utils.ImageUtils;

@Data
public class Product implements Serializable{


		private Long id;
		private String name;
		private String text;
		private String code;
		private Integer price;
		private Long userId;
		private byte[] blob;
//		
		public String getBlob() {
			return ImageUtils.convertBinImageToString(this.blob);
		}
	}

