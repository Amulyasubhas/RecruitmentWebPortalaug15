// package com.recruit.springboot.RecruitmentWebPortal.service;

// import com.recruit.springboot.RecruitmentWebPortal.DTO.OpenRequirementDTO;

// import java.util.List;

// public interface OpenRequirementService {
//     OpenRequirementDTO create(OpenRequirementDTO dto);
//     OpenRequirementDTO update(Long id, OpenRequirementDTO dto);
//     void delete(Long id);
//     // String delete(Long id);

//     OpenRequirementDTO getById(Long id);
//     List<OpenRequirementDTO> getAll();
// }


package com.recruit.springboot.RecruitmentWebPortal.service;

import com.recruit.springboot.RecruitmentWebPortal.DTO.OpenRequirementDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OpenRequirementService {
    OpenRequirementDTO create(OpenRequirementDTO dto);
    OpenRequirementDTO update(Long id, OpenRequirementDTO dto);
    void delete(Long id);
    OpenRequirementDTO getById(Long id);

    // Remove old getAll() or keep if you want, but recommended paged version
    // List<OpenRequirementDTO> getAll();

    // New paginated + searchable method
    Page<OpenRequirementDTO> searchOpenRequirements(String keyword, int page, int size);
}
