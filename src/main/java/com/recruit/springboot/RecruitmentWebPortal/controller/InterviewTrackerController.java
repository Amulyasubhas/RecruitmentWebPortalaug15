// package com.recruit.springboot.RecruitmentWebPortal.controller;

// import com.recruit.springboot.RecruitmentWebPortal.DTO.InterviewTrackerDTO;
// import com.recruit.springboot.RecruitmentWebPortal.service.InterviewTrackerService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;

// @RestController
// @RequestMapping("/api/interview-tracker")
// public class InterviewTrackerController {

//     @Autowired
//     private InterviewTrackerService service;

//     @PostMapping
//     @PreAuthorize("hasRole('ADMIN')")
//     public InterviewTrackerDTO create(@RequestBody InterviewTrackerDTO dto) {
//         return service.save(dto);
//     }

//     @GetMapping
//     @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
//     public List<InterviewTrackerDTO> getAll() {
//         return service.getAll();
//     }
    
//     @GetMapping("/{id}")
//     @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
//     public InterviewTrackerDTO getById(@PathVariable Long id) {
//         return service.getById(id);
//     }

//     @PutMapping("/{id}")
//     @PreAuthorize("hasRole('ADMIN')")
//     public InterviewTrackerDTO update(@PathVariable Long id, @RequestBody InterviewTrackerDTO dto) {
//         return service.update(id, dto);
//     }

//     @DeleteMapping("/{id}")
//     @PreAuthorize("hasRole('ADMIN')")
//     public ResponseEntity<String> delete(@PathVariable Long id) {
//         service.delete(id);
//         return ResponseEntity.ok("Interview tracker entry deleted successfully.");
//     }
// }



package com.recruit.springboot.RecruitmentWebPortal.controller;

import com.recruit.springboot.RecruitmentWebPortal.DTO.InterviewTrackerDTO;
import com.recruit.springboot.RecruitmentWebPortal.service.InterviewTrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interview-tracker")
public class InterviewTrackerController {

    @Autowired
    private InterviewTrackerService service;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public InterviewTrackerDTO create(@RequestBody InterviewTrackerDTO dto) {
        return service.save(dto);
    }

    @PostMapping("/{candidateId}")
    @PreAuthorize("hasRole('ADMIN')")
    public InterviewTrackerDTO createForCandidate(@PathVariable Long candidateId, @RequestBody InterviewTrackerDTO dto) {
        dto.setId(candidateId); // Set the ID from path variable
        return service.save(dto);
    }

    // UPDATED: support pagination & search
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public ResponseEntity<Page<InterviewTrackerDTO>> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<InterviewTrackerDTO> pageResult = service.searchInterviewTrackers(search, page, size);
        return ResponseEntity.ok(pageResult);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
    public InterviewTrackerDTO getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public InterviewTrackerDTO update(@PathVariable Long id, @RequestBody InterviewTrackerDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Interview tracker entry deleted successfully.");
    }
    
}
