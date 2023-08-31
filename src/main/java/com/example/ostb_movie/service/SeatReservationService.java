package com.example.ostb_movie.service;

import org.springframework.stereotype.Service;

import com.example.ostb_movie.dto.SeatDto;
import com.example.ostb_movie.entity.*;
import com.example.ostb_movie.repository.*;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/*
 * @Service
 * 
 * @Transactional
 * 
 * @RequiredArgsConstructor public class SeatReservationService { private final
 * BookRepository bookRepository; private final TheaterRepository
 * theaterRepository; private final SeatRepository seatReservRepo;
 * 
 * // public void insertBook(Book book) { // bookRepository.save(book); // }
 * 
 * public Long saveSeatReservation(SeatDto seatDto) throws Exception { Seat
 * seatReservation = seatDto.createSeat(); seatReservRepo.save(seatReservation);
 * return seatReservation.getId(); } }
 */