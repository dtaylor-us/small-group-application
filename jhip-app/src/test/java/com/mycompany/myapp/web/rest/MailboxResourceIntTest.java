package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.SgApp;

import com.mycompany.myapp.domain.Mailbox;
import com.mycompany.myapp.repository.MailboxRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MailboxResource REST controller.
 *
 * @see MailboxResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SgApp.class)
public class MailboxResourceIntTest {

    private static final String DEFAULT_MAILBOX_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MAILBOX_NAME = "BBBBBBBBBB";

    @Inject
    private MailboxRepository mailboxRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restMailboxMockMvc;

    private Mailbox mailbox;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MailboxResource mailboxResource = new MailboxResource();
        ReflectionTestUtils.setField(mailboxResource, "mailboxRepository", mailboxRepository);
        this.restMailboxMockMvc = MockMvcBuilders.standaloneSetup(mailboxResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mailbox createEntity(EntityManager em) {
        Mailbox mailbox = new Mailbox()
                .mailboxName(DEFAULT_MAILBOX_NAME);
        return mailbox;
    }

    @Before
    public void initTest() {
        mailbox = createEntity(em);
    }

    @Test
    @Transactional
    public void createMailbox() throws Exception {
        int databaseSizeBeforeCreate = mailboxRepository.findAll().size();

        // Create the Mailbox

        restMailboxMockMvc.perform(post("/api/mailboxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mailbox)))
            .andExpect(status().isCreated());

        // Validate the Mailbox in the database
        List<Mailbox> mailboxList = mailboxRepository.findAll();
        assertThat(mailboxList).hasSize(databaseSizeBeforeCreate + 1);
        Mailbox testMailbox = mailboxList.get(mailboxList.size() - 1);
        assertThat(testMailbox.getMailboxName()).isEqualTo(DEFAULT_MAILBOX_NAME);
    }

    @Test
    @Transactional
    public void createMailboxWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = mailboxRepository.findAll().size();

        // Create the Mailbox with an existing ID
        Mailbox existingMailbox = new Mailbox();
        existingMailbox.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMailboxMockMvc.perform(post("/api/mailboxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingMailbox)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Mailbox> mailboxList = mailboxRepository.findAll();
        assertThat(mailboxList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMailboxes() throws Exception {
        // Initialize the database
        mailboxRepository.saveAndFlush(mailbox);

        // Get all the mailboxList
        restMailboxMockMvc.perform(get("/api/mailboxes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mailbox.getId().intValue())))
            .andExpect(jsonPath("$.[*].mailboxName").value(hasItem(DEFAULT_MAILBOX_NAME.toString())));
    }

    @Test
    @Transactional
    public void getMailbox() throws Exception {
        // Initialize the database
        mailboxRepository.saveAndFlush(mailbox);

        // Get the mailbox
        restMailboxMockMvc.perform(get("/api/mailboxes/{id}", mailbox.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(mailbox.getId().intValue()))
            .andExpect(jsonPath("$.mailboxName").value(DEFAULT_MAILBOX_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMailbox() throws Exception {
        // Get the mailbox
        restMailboxMockMvc.perform(get("/api/mailboxes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMailbox() throws Exception {
        // Initialize the database
        mailboxRepository.saveAndFlush(mailbox);
        int databaseSizeBeforeUpdate = mailboxRepository.findAll().size();

        // Update the mailbox
        Mailbox updatedMailbox = mailboxRepository.findOne(mailbox.getId());
        updatedMailbox
                .mailboxName(UPDATED_MAILBOX_NAME);

        restMailboxMockMvc.perform(put("/api/mailboxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMailbox)))
            .andExpect(status().isOk());

        // Validate the Mailbox in the database
        List<Mailbox> mailboxList = mailboxRepository.findAll();
        assertThat(mailboxList).hasSize(databaseSizeBeforeUpdate);
        Mailbox testMailbox = mailboxList.get(mailboxList.size() - 1);
        assertThat(testMailbox.getMailboxName()).isEqualTo(UPDATED_MAILBOX_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingMailbox() throws Exception {
        int databaseSizeBeforeUpdate = mailboxRepository.findAll().size();

        // Create the Mailbox

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMailboxMockMvc.perform(put("/api/mailboxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mailbox)))
            .andExpect(status().isCreated());

        // Validate the Mailbox in the database
        List<Mailbox> mailboxList = mailboxRepository.findAll();
        assertThat(mailboxList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMailbox() throws Exception {
        // Initialize the database
        mailboxRepository.saveAndFlush(mailbox);
        int databaseSizeBeforeDelete = mailboxRepository.findAll().size();

        // Get the mailbox
        restMailboxMockMvc.perform(delete("/api/mailboxes/{id}", mailbox.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Mailbox> mailboxList = mailboxRepository.findAll();
        assertThat(mailboxList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
