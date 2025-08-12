package com.recruit.springboot.RecruitmentWebPortal.serviceimpl;

import com.recruit.springboot.RecruitmentWebPortal.DTO.InterviewTrackerDTO;
import com.recruit.springboot.RecruitmentWebPortal.entity.*;
import com.recruit.springboot.RecruitmentWebPortal.repository.CandidateDetailsAndStatusTrackerRepository;
import com.recruit.springboot.RecruitmentWebPortal.repository.InterviewTrackerRepository;
import com.recruit.springboot.RecruitmentWebPortal.service.InterviewTrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InterviewTrackerServiceImpl implements InterviewTrackerService {

    @Autowired
    private InterviewTrackerRepository repository;

    @Autowired
    private CandidateDetailsAndStatusTrackerRepository candidateRepo;

    @Override
    @Transactional
    public InterviewTrackerDTO save(InterviewTrackerDTO dto) {
        if (dto.getId() == null) {
            throw new RuntimeException("InterviewTracker requires an existing Candidate id in 'id'");
        }

        CandidateDetailsAndStatusTracker candidate = candidateRepo.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Candidate not found with ID: " + dto.getId()));

        // Check if interview tracker already exists for this candidate
        if (repository.existsById(dto.getId())) {
            throw new RuntimeException("Interview tracker already exists for candidate with ID: " + dto.getId());
        }

        // Auto-populate common fields from candidate to ensure data consistency
        dto.setCandidateName(candidate.getCandidateName());
        dto.setSkill(candidate.getSkill());
        dto.setVendor(candidate.getVendor());
        dto.setRecruiter(candidate.getRecruiter() != null ? candidate.getRecruiter().name() : null);

        int nextSlNo = repository.findMaxSlNo().orElse(0) + 1;
        dto.setSlNo(nextSlNo);

        InterviewTracker entity = mapToEntity(dto, candidate);
        InterviewTracker saved = repository.save(entity);
        return mapToDTO(saved);
    }

    @Override
    public List<InterviewTrackerDTO> getAll() {
        try {
            return repository.findAll().stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving interview tracker data: " + e.getMessage(), e);
        }
    }

    @Override
    public InterviewTrackerDTO getById(Long id) {
        InterviewTracker entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Interview Tracker entry not found with id: " + id));
        return mapToDTO(entity);
    }

    @Override
    @Transactional
    public InterviewTrackerDTO update(Long id, InterviewTrackerDTO dto) {
        int maxRetries = 3;
        int attempt = 0;
        
        while (attempt < maxRetries) {
            try {
                InterviewTracker existing = repository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Interview not found with ID: " + id));

                // Update the existing entity instead of creating a new one
                existing.setCandidateName(dto.getCandidateName());
                existing.setSkill(dto.getSkill());
                existing.setVendor(dto.getVendor());
                existing.setRecruiter(dto.getRecruiter());
                existing.setClient(parseEnum(Client.class, dto.getClient()));
                existing.setR1Status(parseEnum(R1Status.class, dto.getR1Status()));
                existing.setR2Status(parseEnum(R2Status.class, dto.getR2Status()));
                existing.setClient1Status(parseEnum(Client1Status.class, dto.getClient1Status()));
                existing.setClient2Status(parseEnum(Client2Status.class, dto.getClient2Status()));
                existing.setOnboarding(parseEnum(Onboarding.class, dto.getOnboarding()));
                existing.setDateOfInterview(dto.getDateOfInterview());
                existing.setTime(dto.getTime());
                existing.setInviteStatus(dto.getInviteStatus());
                existing.setInterviewedBy(dto.getInterviewedBy());
                existing.setOnboardingDate(dto.getOnboardingDate());

                // The entity is already managed by the persistence context, so save will update it
                InterviewTracker saved = repository.save(existing);
                return mapToDTO(saved);
                
            } catch (ObjectOptimisticLockingFailureException e) {
                attempt++;
                if (attempt >= maxRetries) {
                    System.err.println("Optimistic locking failed after " + maxRetries + " attempts for interview tracker ID " + id);
                    throw new RuntimeException("Failed to update interview tracker due to concurrent modification. Please try again.", e);
                }
                System.err.println("Optimistic locking failed, attempt " + attempt + " of " + maxRetries + " for interview tracker ID " + id);
                try {
                    Thread.sleep(1000); // Wait 1 second before retry
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Update operation was interrupted", ie);
                }
            } catch (Exception e) {
                System.err.println("Error updating interview tracker with ID " + id + ": " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Failed to update interview tracker: " + e.getMessage(), e);
            }
        }
        
        throw new RuntimeException("Failed to update interview tracker after " + maxRetries + " attempts");
    }

    @Override
    @Transactional
    public void delete(Long id) {
        try {
            System.out.println("Starting deletion of interview tracker with ID: " + id);
            
            // First, find the interview tracker to verify it exists
            InterviewTracker interviewTracker = repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Interview tracker not found with ID: " + id));
            
            System.out.println("Found interview tracker: " + interviewTracker.getCandidateName());
            
            // Clear the reference from the candidate side first
            CandidateDetailsAndStatusTracker candidate = interviewTracker.getCandidate();
            if (candidate != null) {
                System.out.println("Clearing interview tracker reference from candidate: " + candidate.getCandidateName());
                candidate.setInterviewTracker(null);
                candidateRepo.save(candidate);
                System.out.println("Successfully cleared candidate reference");
            }
            
            // Now delete the interview tracker
            System.out.println("Deleting interview tracker with ID: " + id);
            repository.deleteById(id);
            
            System.out.println("Successfully deleted interview tracker with ID: " + id);
            
        } catch (Exception e) {
            System.err.println("Error deleting interview tracker with ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to delete interview tracker: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<InterviewTrackerDTO> searchInterviewTrackers(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("slNo").descending());

        if (keyword == null || keyword.trim().isEmpty()) {
            return repository.findAll(pageable).map(this::mapToDTO);
        }
        return repository.searchByKeyword(keyword.trim(), pageable).map(this::mapToDTO);
    }

    // Mapping methods same as before
    private InterviewTracker mapToEntity(InterviewTrackerDTO dto, CandidateDetailsAndStatusTracker candidate) {
        try {
            InterviewTracker entity = new InterviewTracker();
            
            // Set the ID (this will be the primary key)
            entity.setId(dto.getId());
            
            // Set the candidate reference - this is crucial for the shared primary key relationship
            entity.setCandidate(candidate);

            entity.setSlNo(dto.getSlNo());
            entity.setCandidateName(dto.getCandidateName());
            entity.setSkill(dto.getSkill());
            entity.setVendor(dto.getVendor());
            entity.setRecruiter(dto.getRecruiter());

            entity.setClient(parseEnum(Client.class, dto.getClient()));
            entity.setR1Status(parseEnum(R1Status.class, dto.getR1Status()));
            entity.setR2Status(parseEnum(R2Status.class, dto.getR2Status()));
            entity.setClient1Status(parseEnum(Client1Status.class, dto.getClient1Status()));
            entity.setClient2Status(parseEnum(Client2Status.class, dto.getClient2Status()));
            entity.setOnboarding(parseEnum(Onboarding.class, dto.getOnboarding()));

            entity.setDateOfInterview(dto.getDateOfInterview());
            entity.setTime(dto.getTime());
            entity.setInviteStatus(dto.getInviteStatus());
            entity.setInterviewedBy(dto.getInterviewedBy());
            entity.setOnboardingDate(dto.getOnboardingDate());

            return entity;
        } catch (Exception e) {
            System.err.println("Error in mapToEntity: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error mapping DTO to entity: " + e.getMessage(), e);
        }
    }

    private InterviewTrackerDTO mapToDTO(InterviewTracker entity) {
        InterviewTrackerDTO dto = new InterviewTrackerDTO();
        dto.setId(entity.getId());
        dto.setSlNo(entity.getSlNo());
        dto.setCandidateName(entity.getCandidateName());
        dto.setSkill(entity.getSkill());
        dto.setVendor(entity.getVendor());
        dto.setRecruiter(entity.getRecruiter());

        dto.setClient(entity.getClient() != null ? entity.getClient().name() : null);
        dto.setR1Status(entity.getR1Status() != null ? entity.getR1Status().name() : null);
        dto.setR2Status(entity.getR2Status() != null ? entity.getR2Status().name() : null);
        dto.setClient1Status(entity.getClient1Status() != null ? entity.getClient1Status().name() : null);
        dto.setClient2Status(entity.getClient2Status() != null ? entity.getClient2Status().name() : null);
        dto.setOnboarding(entity.getOnboarding() != null ? entity.getOnboarding().name() : null);

        dto.setDateOfInterview(entity.getDateOfInterview());
        dto.setTime(entity.getTime());
        dto.setInviteStatus(entity.getInviteStatus());
        dto.setInterviewedBy(entity.getInterviewedBy());
        dto.setOnboardingDate(entity.getOnboardingDate());

        return dto;
    }

    @SuppressWarnings("unchecked")
    private <E extends Enum<E>> E parseEnum(Class<E> enumType, String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            // Try custom fromString() method if exists
            try {
                return (E) enumType.getMethod("fromString", String.class).invoke(null, value);
            } catch (NoSuchMethodException e) {
                // Fallback: convert to ENUM_NAME format
                String normalizedValue = value.trim().toUpperCase().replace(" ", "_").replace("-", "_");
                return Enum.valueOf(enumType, normalizedValue);
            }
        } catch (Exception e) {
            System.err.println("Warning: Could not parse enum value '" + value + "' for " + enumType.getSimpleName() + ": " + e.getMessage());
            return null;
        }
    }
}
