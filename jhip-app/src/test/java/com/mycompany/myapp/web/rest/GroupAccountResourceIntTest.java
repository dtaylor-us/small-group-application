package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.SgApp;

import com.mycompany.myapp.domain.GroupAccount;
import com.mycompany.myapp.repository.GroupAccountRepository;

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

/**
 * Test class for the GroupAccountResource REST controller.
 *
 * @see GroupAccountResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SgApp.class)
public class GroupAccountResourceIntTest {

    private static final String DEFAULT_GROUP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GROUP_DESC = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_DESC = "BBBBBBBBBB";

    private static final String DEFAULT_GROUP_RULES = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_RULES = "BBBBBBBBBB";

    private static final String DEFAULT_GROUP_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_EMAIL = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Inject
    private GroupAccountRepository groupAccountRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restGroupAccountMockMvc;

    private GroupAccount groupAccount;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GroupAccountResource groupAccountResource = new GroupAccountResource();
        ReflectionTestUtils.setField(groupAccountResource, "groupAccountRepository", groupAccountRepository);
        this.restGroupAccountMockMvc = MockMvcBuilders.standaloneSetup(groupAccountResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GroupAccount createEntity(EntityManager em) {
        GroupAccount groupAccount = new GroupAccount()
                .groupName(DEFAULT_GROUP_NAME)
                .groupDesc(DEFAULT_GROUP_DESC)
                .groupRules(DEFAULT_GROUP_RULES)
                .groupEmail(DEFAULT_GROUP_EMAIL)
                .startDate(DEFAULT_START_DATE)
                .endDate(DEFAULT_END_DATE);
        return groupAccount;
    }

    @Before
    public void initTest() {
        groupAccount = createEntity(em);
    }

    @Test
    @Transactional
    public void createGroupAccount() throws Exception {
        int databaseSizeBeforeCreate = groupAccountRepository.findAll().size();

        // Create the GroupAccount

        restGroupAccountMockMvc.perform(post("/api/group-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(groupAccount)))
            .andExpect(status().isCreated());

        // Validate the GroupAccount in the database
        List<GroupAccount> groupAccountList = groupAccountRepository.findAll();
        assertThat(groupAccountList).hasSize(databaseSizeBeforeCreate + 1);
        GroupAccount testGroupAccount = groupAccountList.get(groupAccountList.size() - 1);
        assertThat(testGroupAccount.getGroupName()).isEqualTo(DEFAULT_GROUP_NAME);
        assertThat(testGroupAccount.getGroupDesc()).isEqualTo(DEFAULT_GROUP_DESC);
        assertThat(testGroupAccount.getGroupRules()).isEqualTo(DEFAULT_GROUP_RULES);
        assertThat(testGroupAccount.getGroupEmail()).isEqualTo(DEFAULT_GROUP_EMAIL);
        assertThat(testGroupAccount.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testGroupAccount.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    public void createGroupAccountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = groupAccountRepository.findAll().size();

        // Create the GroupAccount with an existing ID
        GroupAccount existingGroupAccount = new GroupAccount();
        existingGroupAccount.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGroupAccountMockMvc.perform(post("/api/group-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingGroupAccount)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<GroupAccount> groupAccountList = groupAccountRepository.findAll();
        assertThat(groupAccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllGroupAccounts() throws Exception {
        // Initialize the database
        groupAccountRepository.saveAndFlush(groupAccount);

        // Get all the groupAccountList
        restGroupAccountMockMvc.perform(get("/api/group-accounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(groupAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].groupName").value(hasItem(DEFAULT_GROUP_NAME.toString())))
            .andExpect(jsonPath("$.[*].groupDesc").value(hasItem(DEFAULT_GROUP_DESC.toString())))
            .andExpect(jsonPath("$.[*].groupRules").value(hasItem(DEFAULT_GROUP_RULES.toString())))
            .andExpect(jsonPath("$.[*].groupEmail").value(hasItem(DEFAULT_GROUP_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(sameInstant(DEFAULT_START_DATE))))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(sameInstant(DEFAULT_END_DATE))));
    }

    @Test
    @Transactional
    public void getGroupAccount() throws Exception {
        // Initialize the database
        groupAccountRepository.saveAndFlush(groupAccount);

        // Get the groupAccount
        restGroupAccountMockMvc.perform(get("/api/group-accounts/{id}", groupAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(groupAccount.getId().intValue()))
            .andExpect(jsonPath("$.groupName").value(DEFAULT_GROUP_NAME.toString()))
            .andExpect(jsonPath("$.groupDesc").value(DEFAULT_GROUP_DESC.toString()))
            .andExpect(jsonPath("$.groupRules").value(DEFAULT_GROUP_RULES.toString()))
            .andExpect(jsonPath("$.groupEmail").value(DEFAULT_GROUP_EMAIL.toString()))
            .andExpect(jsonPath("$.startDate").value(sameInstant(DEFAULT_START_DATE)))
            .andExpect(jsonPath("$.endDate").value(sameInstant(DEFAULT_END_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingGroupAccount() throws Exception {
        // Get the groupAccount
        restGroupAccountMockMvc.perform(get("/api/group-accounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGroupAccount() throws Exception {
        // Initialize the database
        groupAccountRepository.saveAndFlush(groupAccount);
        int databaseSizeBeforeUpdate = groupAccountRepository.findAll().size();

        // Update the groupAccount
        GroupAccount updatedGroupAccount = groupAccountRepository.findOne(groupAccount.getId());
        updatedGroupAccount
                .groupName(UPDATED_GROUP_NAME)
                .groupDesc(UPDATED_GROUP_DESC)
                .groupRules(UPDATED_GROUP_RULES)
                .groupEmail(UPDATED_GROUP_EMAIL)
                .startDate(UPDATED_START_DATE)
                .endDate(UPDATED_END_DATE);

        restGroupAccountMockMvc.perform(put("/api/group-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGroupAccount)))
            .andExpect(status().isOk());

        // Validate the GroupAccount in the database
        List<GroupAccount> groupAccountList = groupAccountRepository.findAll();
        assertThat(groupAccountList).hasSize(databaseSizeBeforeUpdate);
        GroupAccount testGroupAccount = groupAccountList.get(groupAccountList.size() - 1);
        assertThat(testGroupAccount.getGroupName()).isEqualTo(UPDATED_GROUP_NAME);
        assertThat(testGroupAccount.getGroupDesc()).isEqualTo(UPDATED_GROUP_DESC);
        assertThat(testGroupAccount.getGroupRules()).isEqualTo(UPDATED_GROUP_RULES);
        assertThat(testGroupAccount.getGroupEmail()).isEqualTo(UPDATED_GROUP_EMAIL);
        assertThat(testGroupAccount.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testGroupAccount.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingGroupAccount() throws Exception {
        int databaseSizeBeforeUpdate = groupAccountRepository.findAll().size();

        // Create the GroupAccount

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restGroupAccountMockMvc.perform(put("/api/group-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(groupAccount)))
            .andExpect(status().isCreated());

        // Validate the GroupAccount in the database
        List<GroupAccount> groupAccountList = groupAccountRepository.findAll();
        assertThat(groupAccountList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteGroupAccount() throws Exception {
        // Initialize the database
        groupAccountRepository.saveAndFlush(groupAccount);
        int databaseSizeBeforeDelete = groupAccountRepository.findAll().size();

        // Get the groupAccount
        restGroupAccountMockMvc.perform(delete("/api/group-accounts/{id}", groupAccount.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<GroupAccount> groupAccountList = groupAccountRepository.findAll();
        assertThat(groupAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
