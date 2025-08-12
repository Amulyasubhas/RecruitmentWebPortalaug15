// package com.recruit.springboot.RecruitmentWebPortal.repository;

// import com.recruit.springboot.RecruitmentWebPortal.entity.OpenRequirement;
// import org.springframework.data.jpa.repository.JpaRepository;

// public interface OpenRequirementRepository extends JpaRepository<OpenRequirement, Long> {
// }


package com.recruit.springboot.RecruitmentWebPortal.repository;

import com.recruit.springboot.RecruitmentWebPortal.entity.OpenRequirement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OpenRequirementRepository extends JpaRepository<OpenRequirement, Long> {

    @Query("SELECT o FROM OpenRequirement o " +
           "WHERE LOWER(o.role) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(o.skills) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(o.clientName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<OpenRequirement> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
