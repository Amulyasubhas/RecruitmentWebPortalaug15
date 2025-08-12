
// package com.recruit.springboot.RecruitmentWebPortal.serviceimpl;

// import com.recruit.springboot.RecruitmentWebPortal.DTO.CandidateDetailsAndStatusTrackerDTO;
// import com.recruit.springboot.RecruitmentWebPortal.entity.CandidateDetailsAndStatusTracker;
// import com.recruit.springboot.RecruitmentWebPortal.repository.CandidateDetailsAndStatusTrackerRepository;
// import com.recruit.springboot.RecruitmentWebPortal.service.CandidateDetailsAndStatusTrackerService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import java.util.List;
// import java.util.stream.Collectors;

// @Service
// public class CandidateDetailsAndStatusTrackerServiceImpl implements CandidateDetailsAndStatusTrackerService {

//     @Autowired
//     private CandidateDetailsAndStatusTrackerRepository repo;

//     @Override
//     public CandidateDetailsAndStatusTrackerDTO create(CandidateDetailsAndStatusTrackerDTO dto, String userEmail) {
//         CandidateDetailsAndStatusTracker entity = mapToEntity(dto);
//         entity.setCreatedBy(userEmail);
//         return mapToDTO(repo.save(entity));
//     }

//     @Override
//     public List<CandidateDetailsAndStatusTrackerDTO> getAll() {
//         return repo.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
//     }

//     @Override
//     public CandidateDetailsAndStatusTrackerDTO getById(Long id) {
//         CandidateDetailsAndStatusTracker entity = repo.findById(id)
//             .orElseThrow(() -> new RuntimeException("Candidate not found with ID: " + id));
//         return mapToDTO(entity);
//     }

//     @Override
//     public CandidateDetailsAndStatusTrackerDTO update(Long id, CandidateDetailsAndStatusTrackerDTO dto, String userEmail, List<String> roles) {
//         CandidateDetailsAndStatusTracker existing = repo.findById(id)
//             .orElseThrow(() -> new RuntimeException("Candidate not found with ID: " + id));

//         boolean isAdmin = roles.contains("ROLE_ADMIN");
//         boolean isRecruiter = roles.contains("ROLE_RECRUITER");

//         // Recruiters cannot update
//         if (isRecruiter && !isAdmin) {
//             throw new SecurityException("Recruiters are not allowed to update candidate records.");
//         }

//         // Non-admins can update only their own records
//         if (!isAdmin && !existing.getCreatedBy().equals(userEmail)) {
//             throw new SecurityException("You are not allowed to update this record.");
//         }

//         CandidateDetailsAndStatusTracker updated = mapToEntity(dto);
//         updated.setId(id);
//         updated.setCreatedBy(existing.getCreatedBy()); // Preserve original creator
//         return mapToDTO(repo.save(updated));
//     }

//     @Override
//     public void delete(Long id, String userEmail, List<String> roles) {
//         CandidateDetailsAndStatusTracker existing = repo.findById(id)
//             .orElseThrow(() -> new RuntimeException("Candidate not found with ID: " + id));

//         boolean isAdmin = roles.contains("ROLE_ADMIN");
//         boolean isRecruiter = roles.contains("ROLE_RECRUITER");

//         // Recruiters cannot delete
//         if (isRecruiter && !isAdmin) {
//             throw new SecurityException("Recruiters are not allowed to delete candidate records.");
//         }

//         // Non-admins can delete only their own records
//         if (!isAdmin && !existing.getCreatedBy().equals(userEmail)) {
//             throw new SecurityException("You are not allowed to delete this record.");
//         }

//         repo.deleteById(id);
//     }

//     private CandidateDetailsAndStatusTracker mapToEntity(CandidateDetailsAndStatusTrackerDTO dto) {
//         CandidateDetailsAndStatusTracker e = new CandidateDetailsAndStatusTracker();
//         e.setDate(dto.getDate());
//         e.setCandidateName(dto.getCandidateName());
//         e.setSkill(dto.getSkill());
//         e.setContactNumber(dto.getContactNumber());
//         e.setEmailId(dto.getEmailId());
//         e.setExperience(dto.getExperience());
//         e.setRelevantExp(dto.getRelevantExp());
//         e.setCtc(dto.getCtc());
//         e.setEctc(dto.getEctc());
//         e.setNoticePeriod(dto.getNoticePeriod());
//         e.setCurrentLocation(dto.getCurrentLocation());
//         e.setPreferredLocation(dto.getPreferredLocation());
//         e.setVendor(dto.getVendor());
//         e.setRecruiter(dto.getRecruiter());
//         e.setSource(dto.getSource());
//         e.setStatus(dto.getStatus());
//         e.setComment(dto.getComment());
//         return e;
//     }

//     private CandidateDetailsAndStatusTrackerDTO mapToDTO(CandidateDetailsAndStatusTracker e) {
//         CandidateDetailsAndStatusTrackerDTO dto = new CandidateDetailsAndStatusTrackerDTO();
//         dto.setId(e.getId());
//         dto.setDate(e.getDate());
//         dto.setCandidateName(e.getCandidateName());
//         dto.setSkill(e.getSkill());
//         dto.setContactNumber(e.getContactNumber());
//         dto.setEmailId(e.getEmailId());
//         dto.setExperience(e.getExperience());
//         dto.setRelevantExp(e.getRelevantExp());
//         dto.setCtc(e.getCtc());
//         dto.setEctc(e.getEctc());
//         dto.setNoticePeriod(e.getNoticePeriod());
//         dto.setCurrentLocation(e.getCurrentLocation());
//         dto.setPreferredLocation(e.getPreferredLocation());
//         dto.setVendor(e.getVendor());
//         dto.setRecruiter(e.getRecruiter());
//         dto.setSource(e.getSource());
//         dto.setStatus(e.getStatus());
//         dto.setComment(e.getComment());
//         return dto;
//     }
// }


package com.recruit.springboot.RecruitmentWebPortal.serviceimpl;

import com.recruit.springboot.RecruitmentWebPortal.DTO.CandidateDetailsAndStatusTrackerDTO;
import com.recruit.springboot.RecruitmentWebPortal.entity.CandidateDetailsAndStatusTracker;
import com.recruit.springboot.RecruitmentWebPortal.repository.CandidateDetailsAndStatusTrackerRepository;
import com.recruit.springboot.RecruitmentWebPortal.service.CandidateDetailsAndStatusTrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CandidateDetailsAndStatusTrackerServiceImpl implements CandidateDetailsAndStatusTrackerService {

    @Autowired
    private CandidateDetailsAndStatusTrackerRepository repo;

    @Override
    public CandidateDetailsAndStatusTrackerDTO create(CandidateDetailsAndStatusTrackerDTO dto, String userEmail) {
        CandidateDetailsAndStatusTracker entity = mapToEntity(dto);
        entity.setCreatedBy(userEmail);
        return mapToDTO(repo.save(entity));
    }

    @Override
    public List<CandidateDetailsAndStatusTrackerDTO> getAll() {
        return repo.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public CandidateDetailsAndStatusTrackerDTO getById(Long id) {
        CandidateDetailsAndStatusTracker entity = repo.findById(id)
            .orElseThrow(() -> new RuntimeException("Candidate not found with ID: " + id));
        return mapToDTO(entity);
    }

    @Override
    public CandidateDetailsAndStatusTrackerDTO update(Long id, CandidateDetailsAndStatusTrackerDTO dto, String userEmail, List<String> roles) {
        CandidateDetailsAndStatusTracker existing = repo.findById(id)
            .orElseThrow(() -> new RuntimeException("Candidate not found with ID: " + id));

        boolean isAdmin = roles.contains("ROLE_ADMIN");
        boolean isRecruiter = roles.contains("ROLE_RECRUITER");

        // Recruiters cannot update
        if (isRecruiter && !isAdmin) {
            throw new SecurityException("Recruiters are not allowed to update candidate records.");
        }

        // Non-admins can update only their own records
        if (!isAdmin && !existing.getCreatedBy().equals(userEmail)) {
            throw new SecurityException("You are not allowed to update this record.");
        }

        CandidateDetailsAndStatusTracker updated = mapToEntity(dto);
        updated.setId(id);
        updated.setCreatedBy(existing.getCreatedBy()); // Preserve original creator
        return mapToDTO(repo.save(updated));
    }

    @Override
    public void delete(Long id, String userEmail, List<String> roles) {
        CandidateDetailsAndStatusTracker existing = repo.findById(id)
            .orElseThrow(() -> new RuntimeException("Candidate not found with ID: " + id));

        boolean isAdmin = roles.contains("ROLE_ADMIN");
        boolean isRecruiter = roles.contains("ROLE_RECRUITER");

        // Recruiters cannot delete
        if (isRecruiter && !isAdmin) {
            throw new SecurityException("Recruiters are not allowed to delete candidate records.");
        }

        // Non-admins can delete only their own records
        if (!isAdmin && !existing.getCreatedBy().equals(userEmail)) {
            throw new SecurityException("You are not allowed to delete this record.");
        }

        repo.deleteById(id);
    }

    // New method for paged + searchable results
    @Override
    public Page<CandidateDetailsAndStatusTrackerDTO> searchCandidates(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        if (keyword == null || keyword.trim().isEmpty()) {
            return repo.findAll(pageable).map(this::mapToDTO);
        }

        return repo.searchByKeyword(keyword.trim(), pageable).map(this::mapToDTO);
    }

    private CandidateDetailsAndStatusTracker mapToEntity(CandidateDetailsAndStatusTrackerDTO dto) {
        CandidateDetailsAndStatusTracker e = new CandidateDetailsAndStatusTracker();
        e.setDate(dto.getDate());
        e.setCandidateName(dto.getCandidateName());
        e.setSkill(dto.getSkill());
        e.setContactNumber(dto.getContactNumber());
        e.setEmailId(dto.getEmailId());
        e.setExperience(dto.getExperience());
        e.setRelevantExp(dto.getRelevantExp());
        e.setCtc(dto.getCtc());
        e.setEctc(dto.getEctc());
        e.setNoticePeriod(dto.getNoticePeriod());
        e.setCurrentLocation(dto.getCurrentLocation());
        e.setPreferredLocation(dto.getPreferredLocation());
        e.setVendor(dto.getVendor());
        e.setRecruiter(dto.getRecruiter());
        e.setSource(dto.getSource());
        e.setStatus(dto.getStatus());
        e.setComment(dto.getComment());
        return e;
    }

    private CandidateDetailsAndStatusTrackerDTO mapToDTO(CandidateDetailsAndStatusTracker e) {
        CandidateDetailsAndStatusTrackerDTO dto = new CandidateDetailsAndStatusTrackerDTO();
        dto.setId(e.getId());
        dto.setDate(e.getDate());
        dto.setCandidateName(e.getCandidateName());
        dto.setSkill(e.getSkill());
        dto.setContactNumber(e.getContactNumber());
        dto.setEmailId(e.getEmailId());
        dto.setExperience(e.getExperience());
        dto.setRelevantExp(e.getRelevantExp());
        dto.setCtc(e.getCtc());
        dto.setEctc(e.getEctc());
        dto.setNoticePeriod(e.getNoticePeriod());
        dto.setCurrentLocation(e.getCurrentLocation());
        dto.setPreferredLocation(e.getPreferredLocation());
        dto.setVendor(e.getVendor());
        dto.setRecruiter(e.getRecruiter());
        dto.setSource(e.getSource());
        dto.setStatus(e.getStatus());
        dto.setComment(e.getComment());
        return dto;
    }
}
