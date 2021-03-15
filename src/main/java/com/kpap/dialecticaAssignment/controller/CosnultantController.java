package com.kpap.dialecticaAssignment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kpap.dialecticaAssignment.dto.ConsultantDTO;
import com.kpap.dialecticaAssignment.model.Consultant;
import com.kpap.dialecticaAssignment.repository.ConsultantRepository;

/**
 * Controller for @Consultant related operations
 * @author k.papageorgiou
 *
 */
@RestController
@RequestMapping("api/consultant")
public class CosnultantController {

	@Autowired
	ConsultantRepository consultantRepo;

	/**
	 * @param consultant
	 * @return
	 */
	@PostMapping("/add")
	public ResponseEntity<Object> add(@RequestBody ConsultantDTO consultant) {
		try {
			Consultant newConsultant = consultantRepo
					.save(new Consultant(consultant.getFullName(), consultant.getEmail(), consultant.getDescription()));
			consultant.setId(newConsultant.getId()); //
			return new ResponseEntity<>(consultant, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * @param csDto
	 * @param id
	 * @return
	 */
	@PutMapping("/update/{id}")
	public HttpEntity<? extends Object> update(@RequestBody ConsultantDTO csDto, @PathVariable Long id) {
		try {
			return consultantRepo.findById(id).map(consultant -> {
				consultant.setEmail(csDto.getEmail());
				consultant.setFullName(csDto.getFullName());
				consultant.setDescription(csDto.getDescription());
				consultantRepo.save(consultant);
				return new ResponseEntity<>(csDto, HttpStatus.OK);
			}).orElseThrow(() -> new Exception("Consultant not found"));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * @param id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity update(@PathVariable Long id) {
		try {
			return consultantRepo.findById(id).map(consultant -> {
				consultantRepo.delete(consultant);
				return new ResponseEntity<>(HttpStatus.OK);
			}).orElseThrow(() -> new Exception("Consultant not found"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
}
