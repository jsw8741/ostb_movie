package com.example.ostb_movie.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ostb_movie.entity.Member;


public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmailAndProvider(String email, String provider);
    
    Member findByEmail(String email);
    
    @Query("select m.email from Member m where m.name = :name and m.phone = :phone")
    String getMemberEmail(@Param("name") String name, @Param("phone") String phone);
    
    
}
