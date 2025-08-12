// package com.recruit.springboot.RecruitmentWebPortal.repository;

// import com.recruit.springboot.RecruitmentWebPortal.entity.CandidateDetailsAndStatusTracker;
// import org.springframework.data.jpa.repository.JpaRepository;
// import java.util.List;

// public interface CandidateDetailsAndStatusTrackerRepository extends JpaRepository<CandidateDetailsAndStatusTracker, Long> {
//     List<CandidateDetailsAndStatusTracker> findByCreatedBy(String createdBy);
// }


package com.recruit.springboot.RecruitmentWebPortal.repository;

import com.recruit.springboot.RecruitmentWebPortal.entity.CandidateDetailsAndStatusTracker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CandidateDetailsAndStatusTrackerRepository extends JpaRepository<CandidateDetailsAndStatusTracker, Long> {

    // Optional: fetch by creator if you want for some filtering
    // List<CandidateDetailsAndStatusTracker> findByCreatedBy(String createdBy);

    // Search by candidateName OR skill OR emailId (case-insensitive) with pagination
    @Query("SELECT c FROM CandidateDetailsAndStatusTracker c " +
           "WHERE LOWER(c.candidateName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(c.skill) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(c.emailId) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<CandidateDetailsAndStatusTracker> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
