package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.SgApp;

import com.mycompany.myapp.domain.MediaFeature;
import com.mycompany.myapp.repository.MediaFeatureRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.domain.enumeration.FeatureType;
/**
 * Test class for the MediaFeatureResource REST controller.
 *
 * @see MediaFeatureResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SgApp.class)
public class MediaFeatureResourceIntTest {

    private static final FeatureType DEFAULT_MEDIA_TYPE = FeatureType.DOCUMENTS;
    private static final FeatureType UPDATED_MEDIA_TYPE = FeatureType.VIDEO;

    private static final String DEFAULT_MEDIA_DESC = "AAAAAAAAAA";
    private static final String UPDATED_MEDIA_DESC = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_MODIFIED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_MODIFIED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Inject
    private MediaFeatureRepository mediaFeatureRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restMediaFeatureMockMvc;

    private MediaFeature mediaFeature;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MediaFeatureResource mediaFeatureResource = new MediaFeatureResource();
        ReflectionTestUtils.setField(mediaFeatureResource, "mediaFeatureRepository", mediaFeatureRepository);
        this.restMediaFeatureMockMvc = MockMvcBuilders.standaloneSetup(mediaFeatureResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MediaFeature createEntity(EntityManager em) {
        MediaFeature mediaFeature = new MediaFeature()
                .featureType(DEFAULT_MEDIA_TYPE)
                .mediaDesc(DEFAULT_MEDIA_DESC)
                .createDate(DEFAULT_CREATE_DATE)
                .dateModified(DEFAULT_DATE_MODIFIED);
        return mediaFeature;
    }

    @Before
    public void initTest() {
        mediaFeature = createEntity(em);
    }

    @Test
    @Transactional
    public void createMediaFeature() throws Exception {
        int databaseSizeBeforeCreate = mediaFeatureRepository.findAll().size();

        // Create the MediaFeature

        restMediaFeatureMockMvc.perform(post("/api/media-features")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mediaFeature)))
            .andExpect(status().isCreated());

        // Validate the MediaFeature in the database
        List<MediaFeature> mediaFeatureList = mediaFeatureRepository.findAll();
        assertThat(mediaFeatureList).hasSize(databaseSizeBeforeCreate + 1);
        MediaFeature testMediaFeature = mediaFeatureList.get(mediaFeatureList.size() - 1);
        assertThat(testMediaFeature.getFeatureType()).isEqualTo(DEFAULT_MEDIA_TYPE);
        assertThat(testMediaFeature.getMediaDesc()).isEqualTo(DEFAULT_MEDIA_DESC);
        assertThat(testMediaFeature.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testMediaFeature.getDateModified()).isEqualTo(DEFAULT_DATE_MODIFIED);
    }

    @Test
    @Transactional
    public void createMediaFeatureWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = mediaFeatureRepository.findAll().size();

        // Create the MediaFeature with an existing ID
        MediaFeature existingMediaFeature = new MediaFeature();
        existingMediaFeature.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMediaFeatureMockMvc.perform(post("/api/media-features")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingMediaFeature)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<MediaFeature> mediaFeatureList = mediaFeatureRepository.findAll();
        assertThat(mediaFeatureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMediaFeatures() throws Exception {
        // Initialize the database
        mediaFeatureRepository.saveAndFlush(mediaFeature);

        // Get all the mediaFeatureList
        restMediaFeatureMockMvc.perform(get("/api/media-features?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mediaFeature.getId().intValue())))
            .andExpect(jsonPath("$.[*].mediaType").value(hasItem(DEFAULT_MEDIA_TYPE.toString())))
            .andExpect(jsonPath("$.[*].mediaDesc").value(hasItem(DEFAULT_MEDIA_DESC.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(sameInstant(DEFAULT_CREATE_DATE))))
            .andExpect(jsonPath("$.[*].dateModified").value(hasItem(sameInstant(DEFAULT_DATE_MODIFIED))));
    }

    @Test
    @Transactional
    public void getMediaFeature() throws Exception {
        // Initialize the database
        mediaFeatureRepository.saveAndFlush(mediaFeature);

        // Get the mediaFeature
        restMediaFeatureMockMvc.perform(get("/api/media-features/{id}", mediaFeature.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(mediaFeature.getId().intValue()))
            .andExpect(jsonPath("$.mediaType").value(DEFAULT_MEDIA_TYPE.toString()))
            .andExpect(jsonPath("$.mediaDesc").value(DEFAULT_MEDIA_DESC.toString()))
            .andExpect(jsonPath("$.createDate").value(sameInstant(DEFAULT_CREATE_DATE)))
            .andExpect(jsonPath("$.dateModified").value(sameInstant(DEFAULT_DATE_MODIFIED)));
    }

    @Test
    @Transactional
    public void getNonExistingMediaFeature() throws Exception {
        // Get the mediaFeature
        restMediaFeatureMockMvc.perform(get("/api/media-features/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMediaFeature() throws Exception {
        // Initialize the database
        mediaFeatureRepository.saveAndFlush(mediaFeature);
        int databaseSizeBeforeUpdate = mediaFeatureRepository.findAll().size();

        // Update the mediaFeature
        MediaFeature updatedMediaFeature = mediaFeatureRepository.findOne(mediaFeature.getId());
        updatedMediaFeature
                .featureType(UPDATED_MEDIA_TYPE)
                .mediaDesc(UPDATED_MEDIA_DESC)
                .createDate(UPDATED_CREATE_DATE)
                .dateModified(UPDATED_DATE_MODIFIED);

        restMediaFeatureMockMvc.perform(put("/api/media-features")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMediaFeature)))
            .andExpect(status().isOk());

        // Validate the MediaFeature in the database
        List<MediaFeature> mediaFeatureList = mediaFeatureRepository.findAll();
        assertThat(mediaFeatureList).hasSize(databaseSizeBeforeUpdate);
        MediaFeature testMediaFeature = mediaFeatureList.get(mediaFeatureList.size() - 1);
        assertThat(testMediaFeature.getFeatureType()).isEqualTo(UPDATED_MEDIA_TYPE);
        assertThat(testMediaFeature.getMediaDesc()).isEqualTo(UPDATED_MEDIA_DESC);
        assertThat(testMediaFeature.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testMediaFeature.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
    }

    @Test
    @Transactional
    public void updateNonExistingMediaFeature() throws Exception {
        int databaseSizeBeforeUpdate = mediaFeatureRepository.findAll().size();

        // Create the MediaFeature

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMediaFeatureMockMvc.perform(put("/api/media-features")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mediaFeature)))
            .andExpect(status().isCreated());

        // Validate the MediaFeature in the database
        List<MediaFeature> mediaFeatureList = mediaFeatureRepository.findAll();
        assertThat(mediaFeatureList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMediaFeature() throws Exception {
        // Initialize the database
        mediaFeatureRepository.saveAndFlush(mediaFeature);
        int databaseSizeBeforeDelete = mediaFeatureRepository.findAll().size();

        // Get the mediaFeature
        restMediaFeatureMockMvc.perform(delete("/api/media-features/{id}", mediaFeature.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MediaFeature> mediaFeatureList = mediaFeatureRepository.findAll();
        assertThat(mediaFeatureList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
