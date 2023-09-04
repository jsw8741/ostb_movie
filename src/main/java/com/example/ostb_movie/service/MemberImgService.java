package com.example.ostb_movie.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.example.ostb_movie.entity.Member;
import com.example.ostb_movie.repository.MemberRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberImgService {
	private String memberImgLocation = "C:/movie/profile";
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
	
	public void UpdateMemberImg(Long memberId, MultipartFile memberImgFile)
			throws Exception {
		if(!memberImgFile.isEmpty()) {
			
			Member saveMemberImg = memberRepository.findById(memberId)
												.orElseThrow(EntityNotFoundException::new);
			
			if(!StringUtils.isEmpty(saveMemberImg.getMemberImg())) {
				fileService.deleteFile(memberImgLocation + "/" + saveMemberImg.getImgName());
			}
			
			String oriImgName = memberImgFile.getOriginalFilename();
			String imgName = fileService.profileUploadFile(memberImgLocation, oriImgName
					, memberImgFile.getBytes());
			String memberImg = "/images/profile/" + imgName;
			
			saveMemberImg.updateMemberImg(imgName, memberImg);
		}
	}
}
