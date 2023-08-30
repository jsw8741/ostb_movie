package com.example.ostb_movie.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.example.ostb_movie.entity.Itemimg;
import com.example.ostb_movie.repository.ItemImgRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {
	private String itemImgLocation = "C:/movie/item";
	private final ItemImgRepository itemImgRepository;
	private final FileService fileService;

	public void saveItemImg(Itemimg itemImg, MultipartFile itemImgFile) throws Exception {
		String oriImgName = itemImgFile.getOriginalFilename();
		String imgName = "";
		String imgUrl = "";

		if (!StringUtils.isEmpty(oriImgName)) {
			imgName = fileService.itemUploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
			imgUrl = "/images/item/" + imgName;
		}
		itemImg.updateItemImg(oriImgName, imgName, imgUrl);
		itemImgRepository.save(itemImg);
	}

	public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception {
		if (!itemImgFile.isEmpty()) {
			Itemimg savedItemImg = itemImgRepository.findById(itemImgId).orElseThrow(EntityNotFoundException::new);

			if (!StringUtils.isEmpty(savedItemImg.getImgName())) {
				fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getImgName());
			}

			String oriImgName = itemImgFile.getOriginalFilename();
			String imgName = fileService.itemUploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
			String imgUrl = "/image/item/" + imgName;
			savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
		}
	}
	
	public List<Itemimg> allItemList(){
		List<Itemimg> allItem = itemImgRepository.findAll();
		
		return allItem;
	}
}
