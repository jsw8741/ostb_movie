package com.example.ostb_movie.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.example.ostb_movie.dto.MypageFormDto;
import com.example.ostb_movie.entity.Member;
import com.example.ostb_movie.repository.MemberRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberImgService {
	@Value("${profileImgLocation}")
	private String memberImgLocation;
//	private String memberImgLocation = "C:/movie/profile";
	private final MemberRepository memberRepository;
	private final FileService fileService;
	
	private void saveMemberProfileImg(Member member, MultipartFile memberImgFile) throws Exception {
		String oriImgName = memberImgFile.getOriginalFilename();
		String imgName = "";
		String memberImg = "";
		
		if(!StringUtils.isEmpty(oriImgName)) {
			memberImg = memberImg(member, memberImgFile);
		}
		
		member.updateMemberImg(imgName, memberImg);
		memberRepository.save(member);
	}
	
	public String memberImg(Member member, MultipartFile memberImgFile) throws Exception {
		String oriImgName = memberImgFile.getOriginalFilename();
		String imgName = "";
		String memberImg = "";
		if(!StringUtils.isEmpty(oriImgName)) {
			imgName = fileService.profileUploadFile(
					memberImgLocation, oriImgName, memberImgFile.getBytes());
			memberImg = "/images/profile/" + imgName;
		}
		
		
		
		return memberImg;
	}
	
	public void UpdateMemberImg(MypageFormDto mypageFormDto, Long memberId, MultipartFile memberImgFile)
			throws Exception {
		String imgName = null;
		String memberImg = null;
		Member saveMemberImg = memberRepository.findById(memberId)
				.orElseThrow(EntityNotFoundException::new);
		if(!memberImgFile.isEmpty()) {
			if(!StringUtils.isEmpty(saveMemberImg.getMemberImg())) {
				fileService.deleteFile(memberImgLocation + "/" + saveMemberImg.getImgName());
			}
			
			
			if(memberImgFile.getOriginalFilename().isEmpty()) {
				
			}else {
				String oriImgName = memberImgFile.getOriginalFilename();
				imgName = fileService.profileUploadFile(memberImgLocation, oriImgName
						, memberImgFile.getBytes());
				memberImg = "/images/profile/" + imgName;
			}
			
			saveMemberImg.updateMemberImg(imgName, memberImg);
		}else {
			saveMemberImg.setMemberImg(mypageFormDto.getImgUrl());
			return;
		}
	}
}
