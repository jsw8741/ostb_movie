package com.example.ostb_movie.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ostb_movie.entity.Member;


public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmailAndProvider(String email, String provider);
    
    Member findByEmail(String email);
}
