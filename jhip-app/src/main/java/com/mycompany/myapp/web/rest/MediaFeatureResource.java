package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.MediaFeature;

import com.mycompany.myapp.repository.MediaFeatureRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing MediaFeature.
 */
@RestController
@RequestMapping("/api")
public class MediaFeatureResource {

    private final Logger log = LoggerFactory.getLogger(MediaFeatureResource.class);
        
    @Inject
    private MediaFeatureRepository mediaFeatureRepository;

    /**
     * POST  /media-features : Create a new mediaFeature.
     *
     * @param mediaFeature the mediaFeature to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mediaFeature, or with status 400 (Bad Request) if the mediaFeature has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/media-features")
    @Timed
    public ResponseEntity<MediaFeature> createMediaFeature(@RequestBody MediaFeature mediaFeature) throws URISyntaxException {
        log.debug("REST request to save MediaFeature : {}", mediaFeature);
        if (mediaFeature.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("mediaFeature", "idexists", "A new mediaFeature cannot already have an ID")).body(null);
        }
        MediaFeature result = mediaFeatureRepository.save(mediaFeature);
        return ResponseEntity.created(new URI("/api/media-features/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("mediaFeature", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /media-features : Updates an existing mediaFeature.
     *
     * @param mediaFeature the mediaFeature to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mediaFeature,
     * or with status 400 (Bad Request) if the mediaFeature is not valid,
     * or with status 500 (Internal Server Error) if the mediaFeature couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/media-features")
    @Timed
    public ResponseEntity<MediaFeature> updateMediaFeature(@RequestBody MediaFeature mediaFeature) throws URISyntaxException {
        log.debug("REST request to update MediaFeature : {}", mediaFeature);
        if (mediaFeature.getId() == null) {
            return createMediaFeature(mediaFeature);
        }
        MediaFeature result = mediaFeatureRepository.save(mediaFeature);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("mediaFeature", mediaFeature.getId().toString()))
            .body(result);
    }

    /**
     * GET  /media-features : get all the mediaFeatures.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of mediaFeatures in body
     */
    @GetMapping("/media-features")
    @Timed
    public List<MediaFeature> getAllMediaFeatures() {
        log.debug("REST request to get all MediaFeatures");
        List<MediaFeature> mediaFeatures = mediaFeatureRepository.findAll();
        return mediaFeatures;
    }

    /**
     * GET  /media-features/:id : get the "id" mediaFeature.
     *
     * @param id the id of the mediaFeature to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mediaFeature, or with status 404 (Not Found)
     */
    @GetMapping("/media-features/{id}")
    @Timed
    public ResponseEntity<MediaFeature> getMediaFeature(@PathVariable Long id) {
        log.debug("REST request to get MediaFeature : {}", id);
        MediaFeature mediaFeature = mediaFeatureRepository.findOne(id);
        return Optional.ofNullable(mediaFeature)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /media-features/:id : delete the "id" mediaFeature.
     *
     * @param id the id of the mediaFeature to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/media-features/{id}")
    @Timed
    public ResponseEntity<Void> deleteMediaFeature(@PathVariable Long id) {
        log.debug("REST request to delete MediaFeature : {}", id);
        mediaFeatureRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("mediaFeature", id.toString())).build();
    }

}
