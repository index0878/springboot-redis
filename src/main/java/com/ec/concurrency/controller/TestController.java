package com.ec.concurrency.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ec.concurrency.entity.Product;
import com.ec.concurrency.exception.SeckillException;
import com.ec.concurrency.service.TestService;

import jakarta.annotation.Resource;

@Controller
public class TestController {

	@Resource
	TestService service;

	@GetMapping("/showTemplateById")
	public ModelAndView showTemplate(@RequestParam(name = "id", defaultValue = "1") Long id) {
		System.out.println("C" + id);
		Product pd = service.findProductById(id);
		ModelAndView mv = new ModelAndView("/indexId");
		mv.addObject("product", pd);
		return mv;
	}

	@GetMapping("/showTemplate")
	public ModelAndView showTemplate() {

		ModelAndView mv = new ModelAndView("/index");
		return mv;
	}

	@GetMapping("/seckill")
	@ResponseBody
	public Map<String, String> seckill(@RequestParam(name = "account", defaultValue = "1") String account,
			@RequestParam(name = "goodid", defaultValue = "1") long goodid) {
		System.out.println("seckill");
		Map<String, String> map = new HashMap<>();
		try {
			map = service.buySecKillGoods(account, goodid);
		} catch (SeckillException e) {
			map.put("status", "F");
			map.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return map;
	}
}
