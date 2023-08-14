package com.example.ostb_movie.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.ostb_movie.dto.ItemFormDto;
import com.example.ostb_movie.dto.ItemSearchDto;
import com.example.ostb_movie.dto.MainItemDto;
import com.example.ostb_movie.entity.Item;
import com.example.ostb_movie.entity.Itemimg;
import com.example.ostb_movie.repository.ItemImgRepository;
import com.example.ostb_movie.repository.ItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {
	private final ItemRepository itemRepository;
	private final ItemImgService itemImgService;
	private final ItemImgRepository itemImgRepository;

	public Long saveItem(ItemFormDto itemFormDto, MultipartFile itemImgfile) throws Exception {

		Item item = itemFormDto.createItem(); // dto -< entity
		itemRepository.save(item); // insert(저장)
		Itemimg itemImg = new Itemimg();
		itemImg.setItem(item);

		itemImgService.saveItemImg(itemImg, itemImgfile);

		return item.getId(); // 등록한 상품 id를 리턴
	}

	public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
//		Page<Item> itemPage = itemRepository.getAdminItemPage(itemSearchDto, pageable);
//		return itemPage;
		return null;
	}

	public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
//		Page<MainItemDto> itemPage = itemRepository.getMainItemPage(itemSearchDto, pageable);
//		return itemPage;
		return null;
	}
}
