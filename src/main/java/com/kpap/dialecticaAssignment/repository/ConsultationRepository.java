package com.kpap.dialecticaAssignment.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.kpap.dialecticaAssignment.model.Consultation;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

/**
 * @author k.papageorgiou
 *
 */
public interface ConsultationRepository extends CrudRepository<Consultation,Long>{
    @Query("SELECT c FROM Consultation c WHERE c.user.id = :userId ")
    Collection<Consultation> findByUserId(@Param("userId") Long userId);

    @Query("SELECT DISTINCT c.consultant.id FROM Consultation c WHERE c.user.id = :userId ")
    Collection<String> distinctConsultants(@Param("userId") Long userId);

    @Query("SELECT SUM(c.payableAmount) from Consultation c where c.badCall = false ")
    Double getTotalPayableAmount();

    @Query("SELECT SUM(c.payableAmount) from Consultation c where c.badCall = false and c.consultant.id=:cId")
    Double getTotalPayableAmountPerConsultant(@Param("cId") Long cId);


    @Query("SELECT SUM(c.payableAmount) from Consultation c where c.badCall = false and c.user.id = :userId")
    Double getTotalPayableAmountPerUser(@Param("userId") Long userId);

    @Query("SELECT SUM(c.payableAmount) from Consultation c where c.badCall = false and c.consultant.id=:cId and c.user.id = :userId")
    Double getTotalPayableAmountPerUserAndConsultant(@Param("cId") Long cId, @Param("userId") Long userId);

}
