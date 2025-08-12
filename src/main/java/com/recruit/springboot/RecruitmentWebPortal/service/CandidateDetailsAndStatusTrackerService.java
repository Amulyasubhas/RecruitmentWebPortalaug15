

// package com.recruit.springboot.RecruitmentWebPortal.service;

// import java.util.List;

// import com.recruit.springboot.RecruitmentWebPortal.DTO.CandidateDetailsAndStatusTrackerDTO;

// public interface CandidateDetailsAndStatusTrackerService {
//     CandidateDetailsAndStatusTrackerDTO create(CandidateDetailsAndStatusTrackerDTO dto, String currentUserEmail);
//     List<CandidateDetailsAndStatusTrackerDTO> getAll();
//     CandidateDetailsAndStatusTrackerDTO update(Long id, CandidateDetailsAndStatusTrackerDTO dto, String currentUserEmail, List<String> roles);
//     CandidateDetailsAndStatusTrackerDTO getById(Long id);
//     void delete(Long id, String currentUserEmail, List<String> roles);
// }


package com.recruit.springboot.RecruitmentWebPortal.service;

import com.recruit.springboot.RecruitmentWebPortal.DTO.CandidateDetailsAndStatusTrackerDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CandidateDetailsAndStatusTrackerService {
    CandidateDetailsAndStatusTrackerDTO create(CandidateDetailsAndStatusTrackerDTO dto, String currentUserEmail);
    List<CandidateDetailsAndStatusTrackerDTO> getAll();
    CandidateDetailsAndStatusTrackerDTO update(Long id, CandidateDetailsAndStatusTrackerDTO dto, String currentUserEmail, List<String> roles);
    CandidateDetailsAndStatusTrackerDTO getById(Long id);
    void delete(Long id, String currentUserEmail, List<String> roles);

    // New paged search method
    Page<CandidateDetailsAndStatusTrackerDTO> searchCandidates(String keyword, int page, int size);
}
