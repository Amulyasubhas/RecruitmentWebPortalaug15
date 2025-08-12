// package com.recruit.springboot.RecruitmentWebPortal.service;

// import java.util.List;

// import com.recruit.springboot.RecruitmentWebPortal.DTO.InterviewTrackerDTO;

// public interface InterviewTrackerService {
//     InterviewTrackerDTO save(InterviewTrackerDTO dto);
//     List<InterviewTrackerDTO> getAll();
//     InterviewTrackerDTO getById(Long id);
//     InterviewTrackerDTO update(Long id, InterviewTrackerDTO dto);
//     void delete(Long id);
// }


package com.recruit.springboot.RecruitmentWebPortal.service;

import com.recruit.springboot.RecruitmentWebPortal.DTO.InterviewTrackerDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InterviewTrackerService {
    InterviewTrackerDTO save(InterviewTrackerDTO dto);
    List<InterviewTrackerDTO> getAll();
    InterviewTrackerDTO getById(Long id);
    InterviewTrackerDTO update(Long id, InterviewTrackerDTO dto);
    void delete(Long id);

    // New paged search method
    Page<InterviewTrackerDTO> searchInterviewTrackers(String keyword, int page, int size);
}
