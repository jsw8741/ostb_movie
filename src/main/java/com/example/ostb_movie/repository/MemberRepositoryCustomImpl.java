package com.example.ostb_movie.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import com.example.ostb_movie.constant.Role;
import com.example.ostb_movie.dto.MemberSearchDto;
import com.example.ostb_movie.entity.Member;
import com.example.ostb_movie.entity.QMember;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {
	private JPAQueryFactory queryFactory;

	public MemberRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	private BooleanExpression searchRoleEq(Role role) {
		return role == null ? null : QMember.member.role.eq(role);
	}

	private BooleanExpression searchByLike(String searchBy, String searchQuery) {
		if (StringUtils.equals("name", searchBy)) {
			// 등록자 검색시
			return QMember.member.name.like("%" + searchQuery + "%"); // item_nm like %검색어%
		}
		return null;
	}

	public Page<Member> getAdminMemberPage(MemberSearchDto memberSearchDto, Pageable pageable) {

		List<Member> content = queryFactory.selectFrom(QMember.member)
				.where(searchRoleEq(memberSearchDto.getRole()),
						searchByLike(memberSearchDto.getSearchBy(), memberSearchDto.getSearchQuery()))
				.orderBy(QMember.member.id.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();
		long total = queryFactory.select(Wildcard.count).from(QMember.member)
				.where(searchRoleEq(memberSearchDto.getRole()),
						searchByLike(memberSearchDto.getSearchBy(), memberSearchDto.getSearchQuery()))
				.fetchOne();

		return new PageImpl<>(content, pageable, total);
	}
}
