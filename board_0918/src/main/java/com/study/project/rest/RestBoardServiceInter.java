package com.study.project.rest;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface RestBoardServiceInter {

	List<Map<String, Object>> findAll();

	List<RestBoardDTO> dtofindAll(SearchDTO searchDTO);

	int create(RestBoardDTO dto, List<MultipartFile> fileList) throws Exception;

	RestBoardDTO read(int num);

	RestPageDTO page(SearchDTO searchDTO);

	List<RestFileDTO> fileFind(int num);

}
