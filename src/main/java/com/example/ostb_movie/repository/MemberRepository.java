package com.example.ostb_movie.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ostb_movie.dto.MemberSearchDto;
import com.example.ostb_movie.entity.Member;


public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    Optional<Member> findByEmailAndProvider(String email, String provider);
    
    Member findByEmail(String email);
    
    @Query("select m from Member m where m.name = :name and m.phone = :phone")
    Member getMemberEmail(@Param("name") String name, @Param("phone") String phone);

	Optional<Member> findById(Member member);
	
	@Query("select m from Member m where m.role = 'ROLE_MASTER'")
	List<Member> getMasterList();
    
}
