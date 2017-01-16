package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Media;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Media entity.
 */
@SuppressWarnings("unused")
public interface MediaRepository extends JpaRepository<Media,Long> {

}
