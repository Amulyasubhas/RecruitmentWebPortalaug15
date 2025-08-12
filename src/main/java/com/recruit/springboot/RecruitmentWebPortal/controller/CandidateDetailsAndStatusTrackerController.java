

// package com.recruit.springboot.RecruitmentWebPortal.controller;

// import com.recruit.springboot.RecruitmentWebPortal.DTO.CandidateDetailsAndStatusTrackerDTO;
// import com.recruit.springboot.RecruitmentWebPortal.service.CandidateDetailsAndStatusTrackerService;

// import jakarta.validation.Valid;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;

// @RestController
// @RequestMapping("/api/candidates")
// public class CandidateDetailsAndStatusTrackerController {

//     @Autowired
//     private CandidateDetailsAndStatusTrackerService service;

//     @PostMapping
//     public CandidateDetailsAndStatusTrackerDTO create(@Valid @RequestBody CandidateDetailsAndStatusTrackerDTO dto) {
//         String email = getCurrentUserEmail();
//         return service.create(dto, email);
//     }

//     @GetMapping
//     public List<CandidateDetailsAndStatusTrackerDTO> getAll() {
//         return service.getAll();
//     }

//     @GetMapping("/{id}")
//     public CandidateDetailsAndStatusTrackerDTO getById(@PathVariable Long id) {
//         return service.getById(id);
//     }

//     @PutMapping("/{id}")
//     public CandidateDetailsAndStatusTrackerDTO update(@PathVariable Long id,
//                                                       @RequestBody CandidateDetailsAndStatusTrackerDTO dto) {
//         String email = getCurrentUserEmail();
//         List<String> roles = getCurrentUserRoles();
//         return service.update(id, dto, email, roles);
//     }

//     @DeleteMapping("/{id}")
//     public ResponseEntity<String> delete(@PathVariable Long id) {
//         String email = getCurrentUserEmail();
//         List<String> roles = getCurrentUserRoles();
//         service.delete(id, email, roles);
//         return ResponseEntity.ok("Deleted successfully.");
//     }

//     private String getCurrentUserEmail() {
//         Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

//         if (principal instanceof UserDetails) {
//             return ((UserDetails) principal).getUsername();
//         } else if (principal instanceof String) {
//             return (String) principal;
//         } else {
//             throw new RuntimeException("User is not authenticated or token is invalid.");
//         }
//     }

//     private List<String> getCurrentUserRoles() {
//         return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
//             .map(GrantedAuthority::getAuthority)
//             .toList();
//     }
// }


package com.recruit.springboot.RecruitmentWebPortal.controller;

import com.recruit.springboot.RecruitmentWebPortal.DTO.CandidateDetailsAndStatusTrackerDTO;
import com.recruit.springboot.RecruitmentWebPortal.service.CandidateDetailsAndStatusTrackerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
public class CandidateDetailsAndStatusTrackerController {

    @Autowired
    private CandidateDetailsAndStatusTrackerService service;

    @PostMapping
    public CandidateDetailsAndStatusTrackerDTO create(@Valid @RequestBody CandidateDetailsAndStatusTrackerDTO dto) {
        String email = getCurrentUserEmail();
        return service.create(dto, email);
    }

    // UPDATED: GET with pagination & search
    @GetMapping
    public ResponseEntity<Page<CandidateDetailsAndStatusTrackerDTO>> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<CandidateDetailsAndStatusTrackerDTO> pageResult = service.searchCandidates(search, page, size);
        return ResponseEntity.ok(pageResult);
    }

    @GetMapping("/{id}")
    public CandidateDetailsAndStatusTrackerDTO getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public CandidateDetailsAndStatusTrackerDTO update(@PathVariable Long id,
                                                      @RequestBody CandidateDetailsAndStatusTrackerDTO dto) {
        String email = getCurrentUserEmail();
        List<String> roles = getCurrentUserRoles();
        return service.update(id, dto, email, roles);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        String email = getCurrentUserEmail();
        List<String> roles = getCurrentUserRoles();
        service.delete(id, email, roles);
        return ResponseEntity.ok("Deleted successfully.");
    }

    private String getCurrentUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            return (String) principal;
        } else {
            throw new RuntimeException("User is not authenticated or token is invalid.");
        }
    }

    private List<String> getCurrentUserRoles() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .toList();
    }
}
