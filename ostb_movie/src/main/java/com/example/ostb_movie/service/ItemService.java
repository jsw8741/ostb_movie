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
		
		Itemimg itemImg = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
		
		Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
		
		ItemImgDto itemImgDto = ItemImgDto.of(itemImg);

		ItemFormDto itemFormDto = ItemFormDto.of(item);
		

		itemFormDto.setItemImgId(itemImg.getId());
		itemFormDto.setItemImgDto(itemImgDto);

		return itemFormDto;
	}

	public Long updateItem(ItemFormDto itemFormDto, MultipartFile itemImgFile) throws Exception {
		Item item = itemRepository.findById(itemFormDto.getId()).orElseThrow(EntityNotFoundException::new);
		item.updateItem(itemFormDto);
		Long itemImgId = itemFormDto.getItemImgId();
		itemImgService.updateItemImg(itemImgId,itemImgFile);
		return item.getId();
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
