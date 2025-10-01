package com.study.project.rest;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RestBoardMapper {

	List<Map<String, Object>> findAll();
	List<RestBoardDTO> dtoFindAll(SearchDTO searchDTO);
	int create(RestBoardDTO dto);
	RestBoardDTO read(int num);
	int totalCount(SearchDTO searchDTO);
	int fileCreate(RestFileDTO fileDTO);
	List<RestFileDTO> fileFind(int num);

	

}
