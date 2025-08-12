package com.recruit.springboot.RecruitmentWebPortal.repository;

import com.recruit.springboot.RecruitmentWebPortal.entity.InterviewTracker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface InterviewTrackerRepository extends JpaRepository<InterviewTracker, Long> {

    @Query("SELECT MAX(i.slNo) FROM InterviewTracker i")
    Optional<Integer> findMaxSlNo();

    // Search by candidateName OR skill OR recruiter (case-insensitive)
    @Query("SELECT i FROM InterviewTracker i " +
           "WHERE LOWER(i.candidateName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(i.skill) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(i.recruiter) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<InterviewTracker> searchByKeyword(String keyword, Pageable pageable);
}
