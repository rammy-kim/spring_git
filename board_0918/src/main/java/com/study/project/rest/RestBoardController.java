package com.study.project.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;

@RestController
// @controller + @ResponseBody
@RequestMapping("board")
public class RestBoardController {

	// 생성자 - 클래스명과 똑같은 이름의 메소드
//   @Autowired
	private final RestBoardServiceInter service;

	public RestBoardController(RestBoardServiceInter service) {
		this.service = service;
	}

//   @GetMapping
//   public List<Map<String, Object>> findAll(){
//      List<Map<String, Object>> list = service.findAll();
//      return list;
//   }

	@GetMapping
	public RestResponseDTO dtofindAll() {
		SearchDTO searchDTO = new SearchDTO();
		searchDTO.setCurPage(1);
		searchDTO.setPageSize(5);
		searchDTO.setOffset(0);
		
		List<RestBoardDTO> list = service.dtofindAll(searchDTO);		
		RestPageDTO pageDTO = service.page(searchDTO);
		RestResponseDTO resDTO = new RestResponseDTO();
		resDTO.setList(list);
		resDTO.setPage(pageDTO);

		return resDTO;
	}

	@PostMapping("/search")
	public RestResponseDTO search(@RequestBody SearchDTO searchDTO) {
		int curPage = searchDTO.getCurPage();
		int pageSize = searchDTO.getPageSize();
		System.out.println("/search==============================");
		System.out.println(curPage + " " + pageSize);

		int offset = (curPage - 1) * pageSize;
		searchDTO.setOffset(offset);
		
		List<RestBoardDTO> list = service.dtofindAll(searchDTO);		
		RestPageDTO pageDTO = service.page(searchDTO);
		RestResponseDTO resDTO = new RestResponseDTO();
		resDTO.setList(list);
		resDTO.setPage(pageDTO);

		return resDTO;
	}

	@GetMapping("/{num}")
	public Map<String, Object> read(@PathVariable int num) {
		//DTO 데이터 가공이 용이 
		//MAP<String, Object> 데이터 변경없이 전달만.
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		RestBoardDTO boardDto = service.read(num);
		List<RestFileDTO> listFile = service.fileFind(num);
		dataMap.put("boardDto", boardDto);
		dataMap.put("listFile", listFile);
		System.out.println("listFile" + listFile);
		
		return dataMap;
	}

	@PostMapping
	public Map<String, Object> createBoard(@ModelAttribute RestBoardDTO dto, 
			@RequestPart(value="files", required=false) List<MultipartFile> files ) throws Exception {
		//ModelAttribute는 Text만 저장 
		//RequestPart는 파일받을때.
		
		
		int insert = service.create(dto, files);
		Map<String, Object> status = new HashMap<String, Object>();
		status.put("status", insert == 0 ? false : true);
		return status;
	}
	
	@PostMapping("fileDownload")
	public void fileDownload(@RequestBody RestFileDTO fileDTO, HttpServletResponse response) throws IOException{
		
		File f = new File(fileDTO.getSavePath(), fileDTO.getSaveName());
		
		if(!f.exists()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	        return;
			
		}
        // file 다운로드 설정
		response.setContentType("application/octet-stream");
	    response.setHeader("Content-Disposition", "attachment; filename=\"" 
	                       + URLEncoder.encode(fileDTO.getRealName(), "UTF-8") + "\"");
        // response 객체를 통해서 서버로부터 파일 다운로드
        OutputStream os = response.getOutputStream();
        // 파일 입력 객체 생성
        FileInputStream fis = new FileInputStream(f);
        FileCopyUtils.copy(fis, os);
        fis.close();
        os.close();
	}
	
//	@PostMapping
//	public Map<String, Object> createBoard(@RequestBody RestBoardDTO dto) {
//		// TODO: process POST request
//		int insert = service.create(dto);
//		Map<String, Object> status = new HashMap<String, Object>();
//		status.put("status", insert == 0 ? false : true);
//		return status;
//	}

}
