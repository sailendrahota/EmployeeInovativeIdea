
package com.fiserve.zelle.repository;

import java.sql.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fiserve.zelle.model.EmployeeDetail;


/**
 * @author syedmohamedyusuf
 *
 */
@Repository
public interface AccountRepository extends JpaRepository<EmployeeDetail, Long> {
	
	
	
	  @Query(value ="select salary from account WHERE employee_id = ?1", nativeQuery = true) 
	  Double getSalaryByEmployeeId( long userId);
	
	  @Query(value ="select doj from account WHERE employee_id = ?1", nativeQuery = true) 
	  Date getDojByEmployeeId( long userId);
	/*
	 * @Query("select accountBalance from AccountSchema where user_id=?1") double
	 * getAccBalanceByUserId(Long userId);
	 * 
	 * @Modifying
	 * 
	 * @Query("update AccountSchema set accountBalance=?1 where user_id=?2") void
	 * creditBeneficiaryAccBalance(double beneficiary_balance, Long
	 * beneficiary_CusId);
	 * 
	 * @Modifying
	 * 
	 * @Query("update AccountSchema set accountBalance=?1 where user_id=?2") void
	 * debitPayerAccBalance(double payer_balance, Long userId);
	 * 
	 * @Query("select COUNT(user_id) from AccountSchema where user_id=?1") int
	 * findByUserId(Long userId);
	 */

}
