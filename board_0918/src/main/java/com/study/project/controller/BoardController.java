package com.study.project.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.study.project.service.BoardServiceInter;

@Controller
public class BoardController {

	@Autowired
	public BoardServiceInter service;

	@RequestMapping("list")
	@ResponseBody
	public List<Map<String, Object>> list() {

		List<Map<String, Object>> boardList = service.list();

		return boardList;
	}

	@RequestMapping("insert")
	@ResponseBody
	public Map<String, Object> insert(@RequestBody Map<String, Object> insertMap) {

		int insert = service.insert(insertMap);
		Map<String, Object> status = new HashMap<String, Object>();

		if (insert == 0) {
//			등록에 실패했다
			status.put("stus", "fail");
		} else {
			status.put("stus", "succ");
		}

		return status;
		// redirect , forward
	}

	@RequestMapping("/detail/{num}") // @PathVariable 은 {num} 가져오기 위함.
	@ResponseBody
	public Map<String, Object> detail(@PathVariable int num) {
		Map<String, Object> detailMap = service.detail(num);
		return detailMap;
	}

	@RequestMapping("update")
	@ResponseBody
	public Map<String, Object> update(@RequestBody Map<String, Object> dataMap) {

		int update = service.update(dataMap);
		Map<String, Object> status = new HashMap<String, Object>();

		if (update == 0) {
			status.put("stus", "fail");
		} else {
			status.put("stus", "succ");
		}
		return status;
	}
	
	@RequestMapping("/delete/{num}")
	@ResponseBody
	public Map<String, Object> delete (@PathVariable int num) {
		int delete = service.delete(num);
		Map<String, Object> status = new HashMap<String, Object>(); 
		
		if (delete == 0) {
			status.put("stus", "fail");
		} else {
			status.put("stus", "succ");
		}
		return status;
		
	}
	
	public static class IdsReq {
	    public List<Long> selected;
	  }
	
	@RequestMapping("/deleteSelected")
	@ResponseBody
	public Map<String, Object> deleteSelected (@RequestBody IdsReq req) {
		int delete = service.deleteSelected(req.selected);
		System.out.println("delete : "+delete);
		Map<String, Object> status = new HashMap<String, Object>(); 
		
		if (delete == 0) {
			status.put("stus", "fail");
		} else {
			status.put("stus", "succ");
		}
		return status;
		
	}
	
}
