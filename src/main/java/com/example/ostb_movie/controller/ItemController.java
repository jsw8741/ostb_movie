package com.example.ostb_movie.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.ostb_movie.constant.Categori;
import com.example.ostb_movie.dto.ItemFormDto;
import com.example.ostb_movie.dto.ItemSearchDto;
import com.example.ostb_movie.dto.OrderDto;
import com.example.ostb_movie.entity.Item;
import com.example.ostb_movie.entity.Itemimg;
import com.example.ostb_movie.service.ItemImgService;
import com.example.ostb_movie.service.ItemService;
import com.example.ostb_movie.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ItemController {
	private final ItemService itemService;
	private final OrderService orderService;
	private final ItemImgService itemimgService;

	// 상품 리스트
	@GetMapping(value = "/item/items")
	public String itemShopList(Model model) {
		List<Itemimg> items = itemimgService.allItemList();
		model.addAttribute("items", items);
		
		return "item/itemList";
	}

	@GetMapping(value = "/item/items/{category}")
	public String itemShopList(Model model, @PathVariable("category") Categori categori) {
		List<Itemimg> items = itemimgService.ItemList(categori);
		if (categori.toString() == "VOUCHER") {
			model.addAttribute("categori", "상품권");
		} else if (categori.toString() == "POPCON") {
			model.addAttribute("categori", "팝콘");
		} else if (categori.toString() == "SNACK") {
			model.addAttribute("categori", "스낵");
		} else if (categori.toString() == "GOODS") {
			model.addAttribute("categori", "굿즈");
		} else if (categori.toString() == "DRINK") {
			model.addAttribute("categori", "음료");
		}	else if (categori.toString() == "TICKET") {
			model.addAttribute("categori", "영화관람권");
		}
		model.addAttribute("categoryName", categori);
		model.addAttribute("items", items);
		return "item/itemCategory";
	}

	// 상품 등록페이지
	@GetMapping("/admin/itemform")
	public String loginForm(Model model) {
		model.addAttribute("itemFormDto", new ItemFormDto());
		return "item/itemForm";
	}

	// 상품 등록하기
	@PostMapping("/admin/item/new")
	public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model,
			@RequestParam("itemImgFile") MultipartFile itemImgfile) {
		if (bindingResult.hasErrors()) {
			return "item/itemForm";
		}
		if (itemImgfile.isEmpty()) {
			model.addAttribute("errorMessage", "상품 이미지는 필수 입니다.");
			return "item/itemForm";
		}
		try {
			itemService.saveItem(itemFormDto, itemImgfile);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "상품 등록 중 에러가 발생했습니다.");
			return "item/itemForm";
		}
		return "redirect:/";
	}

	// 상품 상세 페이지
	@GetMapping(value = "/item/{itemId}")
	public String itemDtl(Model model, @PathVariable("itemId") Long itemId) {
		ItemFormDto item = itemService.getItemDtl(itemId);
		model.addAttribute("item", item);
		model.addAttribute("OrderDto", new OrderDto());

		return "item/itemDtl";
	}

	// 상품 관리 창
	@GetMapping(value = { "/admin/items", "/admin/items/{page}" })
	public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model) {
		System.err.println("ssss" + page.toString());
		System.err.println("ssss" + itemSearchDto.getSearchBy() + itemSearchDto.getSearchQuery());
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);

		Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);
		model.addAttribute("items", items);
		model.addAttribute("itemSearchDto", itemSearchDto);
		model.addAttribute("maxPage", 5);

		return "item/itemMng";
	}

	//상품 수정 창
	@GetMapping(value = "/admin/item/{itemId}")
	public String itemDtl(@PathVariable("itemId") Long itemid, Model model) {

		try {
			ItemFormDto itemFormDto = itemService.getItemDtl(itemid);
			model.addAttribute("itemFormDto", itemFormDto);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "상품 수정정보를 불러오는중 에러가 발생했습니다.");
			model.addAttribute("itemFormDto", new ItemFormDto());
			return "item/itemForm";
		}
		return "item/itemModifyForm";
	}

	// 상품 수정
	@PostMapping(value = "/admin/item/{itemId}")
	public String itemUpdate(@Valid ItemFormDto itemFormDto, Model model, BindingResult bindingResult,
			@RequestParam("itemImgFile") MultipartFile itemImgFiles, @PathVariable("itemId") Long itemId) {
		if (bindingResult.hasErrors()) {
			return "redirect:/admin/item/" + itemId;
		}
		if (itemImgFiles.isEmpty() && itemFormDto.getId() == null) {
			model.addAttribute("errorMessage", "상품 이미지는 필수입니다.");
			return "redirect:/admin/item/" + itemId;
		}
		try {
			itemService.updateItem(itemFormDto, itemImgFiles);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errormessage", "상품 수정 중 에러가 발생했습니다.");
			return "redirect:/admin/item/" + itemId;

		}
		return "redirect:/";
	}

	// 아이템 삭제
	@GetMapping(value = "/admin/item/delete/{itemId}")
	public String Facilitiesdelete(@PathVariable("itemId") Long itemId, Model model) {
		orderService.deleteItems(itemId);
		itemService.deleteByitemIdByNative(itemId);
		return "redirect:/";
	}
}
