package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Calendar;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Calendar entity.
 */
@SuppressWarnings("unused")
public interface CalendarRepository extends JpaRepository<Calendar,Long> {

}
