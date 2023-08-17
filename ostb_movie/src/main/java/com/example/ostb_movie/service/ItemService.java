package com.example.ostb_movie.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.ostb_movie.dto.ItemFormDto;
import com.example.ostb_movie.dto.ItemImgDto;
import com.example.ostb_movie.dto.ItemSearchDto;
import com.example.ostb_movie.dto.MainItemDto;
import com.example.ostb_movie.entity.Item;
import com.example.ostb_movie.entity.Itemimg;
import com.example.ostb_movie.repository.ItemImgRepository;
import com.example.ostb_movie.repository.ItemRepository;

import jakarta.persistence.EntityNotFoundException;
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

	@Transactional(readOnly = true) // 트랜젝션 읽기 전용(변경감지 수행하지 않음) -> 성능향상
	public ItemFormDto getItemDtl(Long itemId) {
		// 1. item_img 테이블의 이미지를 가져온다.
		ItemImgDto itemImgDto =itemRepository.findByImg(itemId);

	
		// 2. item 테이블에 있는 데이터를 가져온다.
		Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);

		// Item 앤티티 객체 -> dto로 변환
		ItemFormDto itemFormDto = ItemFormDto.of(item);

		// 3. ItemFormDto에 이미지 정보(itemImgDtoList)를 넣어준다.
		itemFormDto.setItemImgDto(itemImgDto);

		return itemFormDto;
	}

	public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
		Page<Item> itemPage = itemRepository.getAdminItemPage(itemSearchDto, pageable);
		return itemPage;
	}

	public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
		Page<MainItemDto> itemPage = itemRepository.getMainItemPage(itemSearchDto, pageable);
		return itemPage;
	}
}
