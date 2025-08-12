




// package com.recruit.springboot.RecruitmentWebPortal.serviceimpl;

// import com.recruit.springboot.RecruitmentWebPortal.DTO.OpenRequirementDTO;
// import com.recruit.springboot.RecruitmentWebPortal.entity.OpenRequirement;
// import com.recruit.springboot.RecruitmentWebPortal.entity.WorkTimings;
// import com.recruit.springboot.RecruitmentWebPortal.repository.OpenRequirementRepository;
// import com.recruit.springboot.RecruitmentWebPortal.service.OpenRequirementService;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.stereotype.Service;

// import java.util.List;
// import java.util.concurrent.atomic.AtomicLong;
// import java.util.stream.Collectors;

// @Service
// public class OpenRequirementServiceImpl implements OpenRequirementService {

//     private final OpenRequirementRepository repository;
//     private final AtomicLong serialCounter = new AtomicLong(1);

//     public OpenRequirementServiceImpl(OpenRequirementRepository repository) {
//         this.repository = repository;
//     }

//     private String getCurrentUsername() {
//         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//         return (authentication != null) ? authentication.getName() : null;
//     }

//     private boolean isAdmin() {
//         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//         if (authentication != null) {
//             return authentication.getAuthorities().stream()
//                     .map(GrantedAuthority::getAuthority)
//                     .anyMatch(role -> role.equalsIgnoreCase("ROLE_ADMIN"));
//         }
//         return false;
//     }

//     private OpenRequirementDTO mapToDTO(OpenRequirement entity) {
//         OpenRequirementDTO dto = new OpenRequirementDTO();
//         dto.setId(entity.getId());
//         dto.setSerialNo(entity.getSerialNo());
//         dto.setRole(entity.getRole());
//         dto.setNoOfPositions(entity.getNoOfPositions());
//         dto.setSkills(entity.getSkills());

//         if (entity.getYearsOfExperience() != null) {
//             dto.setYearsOfExperience(entity.getYearsOfExperience() + " years");
//         }

//         dto.setClientName(entity.getClientName());
//         dto.setBudget(entity.getBudget());
//         dto.setLocation(entity.getLocation());
//         dto.setWorkTimings(entity.getWorkTimings() != null ? entity.getWorkTimings().getDisplayName() : null);
//         dto.setPriority(entity.getPriority());
//         dto.setPositionStatus(entity.getPositionStatus());
//         dto.setCreatedBy(entity.getCreatedBy());
//         dto.setModeOfWork(entity.getModeOfWork());
//         return dto;
//     }

//     private OpenRequirement mapToEntity(OpenRequirementDTO dto) {
//         OpenRequirement entity = new OpenRequirement();
//         entity.setSerialNo(dto.getSerialNo() != null ? dto.getSerialNo() : serialCounter.getAndIncrement());
//         entity.setRole(dto.getRole());
//         entity.setNoOfPositions(dto.getNoOfPositions());
//         entity.setSkills(dto.getSkills());

//         if (dto.getYearsOfExperience() != null && !dto.getYearsOfExperience().isBlank()) {
//             try {
//                 entity.setYearsOfExperience(Double.parseDouble(dto.getYearsOfExperience().replaceAll("[^0-9.]", "")));
//             } catch (NumberFormatException e) {
//                 throw new RuntimeException("Invalid yearsOfExperience format: " + dto.getYearsOfExperience());
//             }
//         }

//         entity.setClientName(dto.getClientName());
//         entity.setBudget(dto.getBudget());
//         entity.setLocation(dto.getLocation());
//         entity.setWorkTimings(dto.getWorkTimings() != null ? WorkTimings.fromDisplayName(dto.getWorkTimings()) : null);
//         entity.setPriority(dto.getPriority());
//         entity.setPositionStatus(dto.getPositionStatus());

//         if (dto.getModeOfWork() != null) {
//             String normalized = dto.getModeOfWork().trim();
//             if (normalized.equalsIgnoreCase("Work From Office") || normalized.equalsIgnoreCase("WorkFromOffice") || normalized.equalsIgnoreCase("WFO")) {
//                 entity.setModeOfWork("WorkFromOffice");
//             } else if (normalized.equalsIgnoreCase("Remote")) {
//                 entity.setModeOfWork("Remote");
//             } else if (normalized.equalsIgnoreCase("Hybrid")) {
//                 entity.setModeOfWork("Hybrid");
//             } else {
//                 entity.setModeOfWork(normalized);
//             }
//         }
//         return entity;
//     }

//     @Override
//     public OpenRequirementDTO create(OpenRequirementDTO dto) {
//         if (!isAdmin()) {
//             throw new RuntimeException("Only admin can create open requirements.");
//         }
//         OpenRequirement entity = mapToEntity(dto);
//         entity.setCreatedBy(getCurrentUsername());
//         return mapToDTO(repository.save(entity));
//     }

//     @Override
//     public OpenRequirementDTO update(Long id, OpenRequirementDTO dto) {
//         if (!isAdmin()) {
//             throw new RuntimeException("Only admin can update open requirements.");
//         }

//         OpenRequirement existing = repository.findById(id)
//                 .orElseThrow(() -> new RuntimeException("OpenRequirement not found with ID: " + id));

//         existing.setRole(dto.getRole());
//         existing.setNoOfPositions(dto.getNoOfPositions());
//         existing.setSkills(dto.getSkills());

//         if (dto.getYearsOfExperience() != null && !dto.getYearsOfExperience().isBlank()) {
//             try {
//                 existing.setYearsOfExperience(Double.parseDouble(dto.getYearsOfExperience().replaceAll("[^0-9.]", "")));
//             } catch (NumberFormatException e) {
//                 throw new RuntimeException("Invalid yearsOfExperience format: " + dto.getYearsOfExperience());
//             }
//         }

//         existing.setClientName(dto.getClientName());
//         existing.setBudget(dto.getBudget());
//         existing.setLocation(dto.getLocation());
//         existing.setWorkTimings(dto.getWorkTimings() != null ? WorkTimings.fromDisplayName(dto.getWorkTimings()) : null);
//         existing.setPriority(dto.getPriority());
//         existing.setPositionStatus(dto.getPositionStatus());

//         if (dto.getModeOfWork() != null) {
//             String normalized = dto.getModeOfWork().trim();
//             if (normalized.equalsIgnoreCase("Work From Office") || normalized.equalsIgnoreCase("WorkFromOffice") || normalized.equalsIgnoreCase("WFO")) {
//                 existing.setModeOfWork("WorkFromOffice");
//             } else if (normalized.equalsIgnoreCase("Remote")) {
//                 existing.setModeOfWork("Remote");
//             } else if (normalized.equalsIgnoreCase("Hybrid")) {
//                 existing.setModeOfWork("Hybrid");
//             } else {
//                 existing.setModeOfWork(normalized);
//             }
//         }

//         return mapToDTO(repository.save(existing));
//     }

//     @Override
//     public void delete(Long id) {
//         if (!isAdmin()) {
//             throw new RuntimeException("Only admin can delete open requirements.");
//         }

//         OpenRequirement existing = repository.findById(id)
//                 .orElseThrow(() -> new RuntimeException("OpenRequirement not found with ID: " + id));

//         repository.deleteById(id);
//     }

//     @Override
//     public OpenRequirementDTO getById(Long id) {
//         OpenRequirement entity = repository.findById(id)
//                 .orElseThrow(() -> new RuntimeException("OpenRequirement not found with ID: " + id));
//         return mapToDTO(entity);
//     }

//     @Override
//     public List<OpenRequirementDTO> getAll() {
//         return repository.findAll().stream()
//                 .map(this::mapToDTO)
//                 .collect(Collectors.toList());
//     }
// }

package com.recruit.springboot.RecruitmentWebPortal.serviceimpl;

import com.recruit.springboot.RecruitmentWebPortal.DTO.OpenRequirementDTO;
import com.recruit.springboot.RecruitmentWebPortal.entity.OpenRequirement;
import com.recruit.springboot.RecruitmentWebPortal.entity.WorkTimings;
import com.recruit.springboot.RecruitmentWebPortal.repository.OpenRequirementRepository;
import com.recruit.springboot.RecruitmentWebPortal.service.OpenRequirementService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class OpenRequirementServiceImpl implements OpenRequirementService {

    private final OpenRequirementRepository repository;
    private final AtomicLong serialCounter = new AtomicLong(1);

    public OpenRequirementServiceImpl(OpenRequirementRepository repository) {
        this.repository = repository;
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (authentication != null) ? authentication.getName() : null;
    }

    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(role -> role.equalsIgnoreCase("ROLE_ADMIN"));
        }
        return false;
    }

    private OpenRequirementDTO mapToDTO(OpenRequirement entity) {
        OpenRequirementDTO dto = new OpenRequirementDTO();
        dto.setId(entity.getId());
        dto.setSerialNo(entity.getSerialNo());
        dto.setRole(entity.getRole());
        dto.setNoOfPositions(entity.getNoOfPositions());
        dto.setSkills(entity.getSkills());

        if (entity.getYearsOfExperience() != null) {
            dto.setYearsOfExperience(entity.getYearsOfExperience() + " years");
        }

        dto.setClientName(entity.getClientName());
        dto.setBudget(entity.getBudget());
        dto.setLocation(entity.getLocation());
        dto.setWorkTimings(entity.getWorkTimings() != null ? entity.getWorkTimings().getDisplayName() : null);
        dto.setPriority(entity.getPriority());
        dto.setPositionStatus(entity.getPositionStatus());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setModeOfWork(entity.getModeOfWork());
        return dto;
    }

    private OpenRequirement mapToEntity(OpenRequirementDTO dto) {
        OpenRequirement entity = new OpenRequirement();
        entity.setSerialNo(dto.getSerialNo() != null ? dto.getSerialNo() : serialCounter.getAndIncrement());
        entity.setRole(dto.getRole());
        entity.setNoOfPositions(dto.getNoOfPositions());
        entity.setSkills(dto.getSkills());

        if (dto.getYearsOfExperience() != null && !dto.getYearsOfExperience().isBlank()) {
            try {
                entity.setYearsOfExperience(Double.parseDouble(dto.getYearsOfExperience().replaceAll("[^0-9.]", "")));
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid yearsOfExperience format: " + dto.getYearsOfExperience());
            }
        }

        entity.setClientName(dto.getClientName());
        entity.setBudget(dto.getBudget());
        entity.setLocation(dto.getLocation());
        entity.setWorkTimings(dto.getWorkTimings() != null ? WorkTimings.fromDisplayName(dto.getWorkTimings()) : null);
        entity.setPriority(dto.getPriority());
        entity.setPositionStatus(dto.getPositionStatus());

        if (dto.getModeOfWork() != null) {
            String normalized = dto.getModeOfWork().trim();
            if (normalized.equalsIgnoreCase("Work From Office") || normalized.equalsIgnoreCase("WorkFromOffice") || normalized.equalsIgnoreCase("WFO")) {
                entity.setModeOfWork("WorkFromOffice");
            } else if (normalized.equalsIgnoreCase("Remote")) {
                entity.setModeOfWork("Remote");
            } else if (normalized.equalsIgnoreCase("Hybrid")) {
                entity.setModeOfWork("Hybrid");
            } else {
                entity.setModeOfWork(normalized);
            }
        }
        return entity;
    }

    @Override
    public OpenRequirementDTO create(OpenRequirementDTO dto) {
        if (!isAdmin()) {
            throw new RuntimeException("Only admin can create open requirements.");
        }
        OpenRequirement entity = mapToEntity(dto);
        entity.setCreatedBy(getCurrentUsername());
        return mapToDTO(repository.save(entity));
    }

    @Override
    public OpenRequirementDTO update(Long id, OpenRequirementDTO dto) {
        if (!isAdmin()) {
            throw new RuntimeException("Only admin can update open requirements.");
        }

        OpenRequirement existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("OpenRequirement not found with ID: " + id));

        existing.setRole(dto.getRole());
        existing.setNoOfPositions(dto.getNoOfPositions());
        existing.setSkills(dto.getSkills());

        if (dto.getYearsOfExperience() != null && !dto.getYearsOfExperience().isBlank()) {
            try {
                existing.setYearsOfExperience(Double.parseDouble(dto.getYearsOfExperience().replaceAll("[^0-9.]", "")));
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid yearsOfExperience format: " + dto.getYearsOfExperience());
            }
        }

        existing.setClientName(dto.getClientName());
        existing.setBudget(dto.getBudget());
        existing.setLocation(dto.getLocation());
        existing.setWorkTimings(dto.getWorkTimings() != null ? WorkTimings.fromDisplayName(dto.getWorkTimings()) : null);
        existing.setPriority(dto.getPriority());
        existing.setPositionStatus(dto.getPositionStatus());

        if (dto.getModeOfWork() != null) {
            String normalized = dto.getModeOfWork().trim();
            if (normalized.equalsIgnoreCase("Work From Office") || normalized.equalsIgnoreCase("WorkFromOffice") || normalized.equalsIgnoreCase("WFO")) {
                existing.setModeOfWork("WorkFromOffice");
            } else if (normalized.equalsIgnoreCase("Remote")) {
                existing.setModeOfWork("Remote");
            } else if (normalized.equalsIgnoreCase("Hybrid")) {
                existing.setModeOfWork("Hybrid");
            } else {
                existing.setModeOfWork(normalized);
            }
        }

        return mapToDTO(repository.save(existing));
    }

    @Override
    public void delete(Long id) {
        if (!isAdmin()) {
            throw new RuntimeException("Only admin can delete open requirements.");
        }

        OpenRequirement existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("OpenRequirement not found with ID: " + id));

        repository.deleteById(id);
    }

    @Override
    public OpenRequirementDTO getById(Long id) {
        OpenRequirement entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("OpenRequirement not found with ID: " + id));
        return mapToDTO(entity);
    }

    // Removed old getAll()

    @Override
    public Page<OpenRequirementDTO> searchOpenRequirements(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("serialNo").descending());

        if (keyword == null || keyword.trim().isEmpty()) {
            return repository.findAll(pageable)
                    .map(this::mapToDTO);
        }

        return repository.searchByKeyword(keyword.trim(), pageable)
                .map(this::mapToDTO);
    }
}
