package com.study.project.rest;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class RestBoardServiceClass implements RestBoardServiceInter {

	private String savePath = "C:/Users/skdis/Downloads";
	private final Path uploadRoot = Paths.get(savePath);

	private final RestBoardMapper mapper;

	public RestBoardServiceClass(RestBoardMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public List<Map<String, Object>> findAll() {
		// TODO Auto-generated method stub
		return mapper.findAll();
	}

	@Override
	public List<RestBoardDTO> dtofindAll(SearchDTO searchDTO) {
		// TODO Auto-generated method stub
		return mapper.dtoFindAll(searchDTO);
	}

	@Transactional
	@Override
	public int create(RestBoardDTO dto, List<MultipartFile> fileList) throws Exception {
		List<Path> savedFiles = new ArrayList<Path>();

		try {
			// TODO Auto-generated method stub
			mapper.create(dto);
			
			//마이바티스에서 
			//RestBoardMapper.xml에 useGeneratedKeys="true" keyProperty="boardNum"
			//이렇게 설정해주면 auto-increment 된 값을 반환 함.
			Integer boardNum = dto.getBoardNum(); //여기서 반환된 값 받음.
			

			if (boardNum == null)
				return 0;

			if (!Files.exists(uploadRoot)) {
				Files.createDirectories(uploadRoot);
			}

			if (fileList != null) {
				for (MultipartFile f : fileList) {
					if (f == null || f.isEmpty())
						continue;

					String realName = Paths.get(Objects.requireNonNull(f.getOriginalFilename())).getFileName()
							.toString();
					String saveName = UUID.randomUUID() + "_" + realName;
					Path target = uploadRoot.resolve(saveName);

					// 저장
					Files.copy(f.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

					RestFileDTO fileDTO = new RestFileDTO();
					fileDTO.setBoardNum(boardNum);
					fileDTO.setRealName(realName);
					fileDTO.setSaveName(saveName);
					fileDTO.setSavePath(savePath);

					int fileInsert = mapper.fileCreate(fileDTO);
					if (fileInsert == 0)
						return 0;
				}
			}
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			// 파일 삭제 (보상 처리)
			for (Path p : savedFiles) {
				try {
					Files.deleteIfExists(p);
				} catch (IOException ignore) {
				}
			}
			return 0; // 실패
		}
	}

	@Override
	public RestBoardDTO read(int num) {
		// TODO Auto-generated method stub
		return mapper.read(num);
	}

	@Override
	public RestPageDTO page(SearchDTO searchDTO) {

		int count = mapper.totalCount(searchDTO);

		int curPage = searchDTO.getCurPage();
		int pageSize = searchDTO.getPageSize();
		int blockSize = 5;

		int totalPages = (int) Math.ceil(count / (double) pageSize);
		int currentBlock = (int) Math.ceil((double) curPage / blockSize);

		int blockStart = (currentBlock - 1) * blockSize + 1;
		int blockEnd = Math.min(currentBlock * blockSize, Math.max(totalPages, 1));

		RestPageDTO pageDTO = new RestPageDTO();
		pageDTO.setBlockSize(blockSize);
		pageDTO.setCurPage(searchDTO.getCurPage());
		pageDTO.setPageSize(searchDTO.getPageSize());
		pageDTO.setCount(count);
		pageDTO.setTotalPages(totalPages);
		pageDTO.setCurrentBlock(currentBlock);
		pageDTO.setBlockStart(blockStart);
		pageDTO.setBlockEnd(blockEnd);

		return pageDTO;
	}

	@Override
	public List<RestFileDTO> fileFind(int num) {
		// TODO Auto-generated method stub
		return mapper.fileFind(num);
	}

}