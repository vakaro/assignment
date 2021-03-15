package com.kpap.dialecticaAssignment.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.kpap.dialecticaAssignment.model.Consultant;
import com.kpap.dialecticaAssignment.model.Consultation;
import com.kpap.dialecticaAssignment.model.User;
import com.kpap.dialecticaAssignment.repository.ConsultantRepository;
import com.kpap.dialecticaAssignment.repository.ConsultationRepository;
import com.kpap.dialecticaAssignment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kpap.dialecticaAssignment.dto.ConsultationDTO;

/**
 * @author k.papageorgiou
 * Controller to handle requests for @Consultation related operations
 */
@RestController
@RequestMapping("api/consultation")
public class ConsultationContoller {

    @Autowired
    ConsultantRepository consultantRepo;
    @Autowired
    UserRepository userRepo;
    @Autowired
    ConsultationRepository consultationRepo;

    /**
     * Add a consultation
     *
     * @param consultation @ConsultationDTO
     * @param request
     * @return
     */
    @PostMapping("/add")
    public ResponseEntity<Object> add(@RequestBody ConsultationDTO consultation, HttpServletRequest request) {
        try {
            Consultation newConsultation = setUpConsultation(consultation, request);
            consultationRepo.save(newConsultation);
            consultation.setId(newConsultation.getId());
            consultation.setBadCall(newConsultation.isBadCall());
            return new ResponseEntity<>(consultation, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    /**
     * Update a consultation only owned by user in session
     *
     * @param consultation @ConsultationDTO
     * @param request
     * @return
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<Object> update(@RequestBody ConsultationDTO consultation, HttpServletRequest request,@PathVariable Long id) {
        try {
            Optional<Consultation> oldCons = consultationRepo.findById(id);
            if (oldCons.isEmpty()) throw new Exception("Invalid consultation");
            if (!oldCons.get().getUser().getEmail().equalsIgnoreCase(request.getUserPrincipal().getName())) throw new Exception("Not your consultation");
            Consultation updatedConsultation = oldCons.get();
            basicValidation(consultation);
            updatedConsultation.setDate(consultation.getDate());
            updatedConsultation.setUpdatedDate(new Date());
            updatedConsultation.setBadCall(isBadCall(consultation));
            updatedConsultation.setDuration(consultation.getDuration());
            updatedConsultation.setNote(consultation.getNote());
            setConsultant(consultation, updatedConsultation);
            consultationRepo.save(updatedConsultation);
            consultation.setBadCall(updatedConsultation.isBadCall());
            return new ResponseEntity<>(consultation, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    /***
     * Delete a consultation by id only if owned by the user
     * @param consultation
     * @param request
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@RequestBody ConsultationDTO consultation, HttpServletRequest request,@PathVariable Long id) {
        try {
            Optional<Consultation> oldCons = consultationRepo.findById(id);
            if (oldCons.isEmpty()) throw new Exception("Invalid consultation");
            if (!oldCons.get().getUser().getEmail().equalsIgnoreCase(request.getUserPrincipal().getName())) throw new Exception("Not your consultation");

            consultationRepo.delete(oldCons.get());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Request num of consultations created within a time period (based on created date and not consultation date)
     * @param fromDate
     * @param toDate
     * @param request
     * @return String containing either size or error message to be consumed by Analytics
     */
    @GetMapping("/consultationNum")
    public String getConsultations(@RequestParam(name = "from",required = true) String fromDate,@RequestParam(name = "to",required = true) String toDate,HttpServletRequest request) {
        try {

            Date dFrom=new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
            Date dTo=new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
            Principal principal = request.getUserPrincipal();
            User user = userRepo.findByEmail(principal.getName());
            Collection<Consultation> consultations = consultationRepo.findByUserId(user.getId());
            if (consultations==null || consultations.size() == 0) return "0";
            List<Consultation> filtered = consultations.stream().filter(consultation -> consultation.getCreatedDate().after(dFrom) && consultation.getCreatedDate().before(dTo)).collect(Collectors.toList());
            return String.valueOf(filtered.size());
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /***
     * Request the average payable_amount in total OR per consultant (consultations that are set as bad_call should not be included)
     * @param consultantId
     * @param request
     * @return
     */
    @GetMapping("/amountsAvg")
    public String amountsAvg(@RequestParam(name = "consultantId",required = false) Long consultantId,HttpServletRequest request) {
        try {
            if (consultantId!=null) {
                return String.valueOf(consultationRepo.getTotalPayableAmountPerConsultant(consultantId) == null ? 0 : consultationRepo.getTotalPayableAmountPerConsultant(consultantId));
            } else {
                return String.valueOf(consultationRepo.getTotalPayableAmount() == null ? 0 : consultationRepo.getTotalPayableAmount());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /***
     * Request the average payable_amount in total OR per consultant,
     * for the consultations that a specific user has created (consultations that are set as bad_call should not be included)
     * @param consultantId
     * @param request
     * @return
     */
    @GetMapping("/amountsAvgPerUser")
    public String amountsAvgPerUser(@RequestParam(name = "consultantId",required = false) Long consultantId,HttpServletRequest request) {
        try {
            Principal principal = request.getUserPrincipal();
            User user = userRepo.findByEmail(principal.getName());
            if (consultantId!=null) {
                return String.valueOf(consultationRepo.getTotalPayableAmountPerUserAndConsultant(consultantId, user.getId()) == null ? 0 : consultationRepo.getTotalPayableAmountPerUserAndConsultant(consultantId, user.getId()));
            } else {
                return String.valueOf(consultationRepo.getTotalPayableAmountPerUser(user.getId()) == null ? 0 : consultationRepo.getTotalPayableAmountPerUser(user.getId()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /***
     * Retrieve unique number of consultants that set up a consultation
     * @param request
     * @return
     */
    @GetMapping("/uniqueConsultants")
    public String getUniqueConsultants(HttpServletRequest request) {
        try {
            Principal principal = request.getUserPrincipal();
            User user = userRepo.findByEmail(principal.getName());
            Collection<String> consultants = consultationRepo.distinctConsultants(user.getId());
            if (consultants==null) return "0";
            return String.valueOf(consultants.size());
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    private Consultation setUpConsultation(ConsultationDTO consultation, HttpServletRequest request) throws Exception {
        try {
            basicValidation(consultation);
            boolean badCall = isBadCall(consultation);
            Principal principal = request.getUserPrincipal();
            Consultation newConsultation = new Consultation();
            newConsultation.setCreatedDate(new Date());
            newConsultation.setDate(consultation.getDate());
            newConsultation.setUpdatedDate(newConsultation.getCreatedDate());
            newConsultation.setDuration(consultation.getDuration());
            newConsultation.setNote(consultation.getNote());
            newConsultation.setPayableAmount(consultation.getPayableAmount());
            newConsultation.setStatus(consultation.getStatus().toUpperCase());
            newConsultation.setTitle(consultation.getTitle());
            newConsultation.setUser(userRepo.findByEmail(principal.getName()));
            newConsultation.setBadCall(badCall);
            setConsultant(consultation, newConsultation);
            return newConsultation;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void setConsultant(ConsultationDTO consultation, Consultation newConsultation) throws Exception {
        Optional<Consultant> consultant = consultantRepo.findById(consultation.getConsultantId());
        if (consultant.isPresent()) {
            newConsultation.setConsultant(consultant.get());
        } else {
            throw new Exception("Consultant not found");
        }
    }

    private boolean isBadCall(ConsultationDTO consultation) {
        boolean badCall = consultation.isBadCall();
        if (consultation.getDuration() > 0 &&  consultation.getDuration() < 30) {
            badCall = true;
        }
        return badCall;
    }

    private void basicValidation(ConsultationDTO consultation) throws Exception {
        List<String> eligibleStatuses = Arrays.asList("PENDING", "ACTIVE", "COMPLETED");
        if (eligibleStatuses.contains(consultation.getStatus().toUpperCase()) == false ) throw new Exception("Error in selected status");
        if ("COMPLETED".equalsIgnoreCase(consultation.getStatus()) && consultation.getDuration() <=0) throw new Exception("You need a duration");
    }
}
