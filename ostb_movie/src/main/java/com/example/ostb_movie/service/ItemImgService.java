package com.example.ostb_movie.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.example.ostb_movie.entity.Itemimg;
import com.example.ostb_movie.repository.ItemImgRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {
	private String itemImgLocation = "C:/hotel/item";
	private final ItemImgRepository itemImgRepository;
	private final FileService fileService;
	
	public void saveItemImg(Itemimg itemImg, MultipartFile itemImgFile) throws Exception{
		String oriImgName = itemImgFile.getOriginalFilename();
		String imgName= "";
		String imgUrl="";
		
		if(!StringUtils.isEmpty(oriImgName)) {
			imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
			imgUrl = "/image/item/" + imgName;
		}
		itemImg.updateItemImg(oriImgName, imgName, imgUrl);
			itemImgRepository.save(itemImg);
	}
}
