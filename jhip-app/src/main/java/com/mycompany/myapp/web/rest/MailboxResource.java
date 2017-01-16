package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Mailbox;

import com.mycompany.myapp.repository.MailboxRepository;
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
 * REST controller for managing Mailbox.
 */
@RestController
@RequestMapping("/api")
public class MailboxResource {

    private final Logger log = LoggerFactory.getLogger(MailboxResource.class);
        
    @Inject
    private MailboxRepository mailboxRepository;

    /**
     * POST  /mailboxes : Create a new mailbox.
     *
     * @param mailbox the mailbox to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mailbox, or with status 400 (Bad Request) if the mailbox has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/mailboxes")
    @Timed
    public ResponseEntity<Mailbox> createMailbox(@RequestBody Mailbox mailbox) throws URISyntaxException {
        log.debug("REST request to save Mailbox : {}", mailbox);
        if (mailbox.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("mailbox", "idexists", "A new mailbox cannot already have an ID")).body(null);
        }
        Mailbox result = mailboxRepository.save(mailbox);
        return ResponseEntity.created(new URI("/api/mailboxes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("mailbox", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mailboxes : Updates an existing mailbox.
     *
     * @param mailbox the mailbox to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mailbox,
     * or with status 400 (Bad Request) if the mailbox is not valid,
     * or with status 500 (Internal Server Error) if the mailbox couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/mailboxes")
    @Timed
    public ResponseEntity<Mailbox> updateMailbox(@RequestBody Mailbox mailbox) throws URISyntaxException {
        log.debug("REST request to update Mailbox : {}", mailbox);
        if (mailbox.getId() == null) {
            return createMailbox(mailbox);
        }
        Mailbox result = mailboxRepository.save(mailbox);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("mailbox", mailbox.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mailboxes : get all the mailboxes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of mailboxes in body
     */
    @GetMapping("/mailboxes")
    @Timed
    public List<Mailbox> getAllMailboxes() {
        log.debug("REST request to get all Mailboxes");
        List<Mailbox> mailboxes = mailboxRepository.findAll();
        return mailboxes;
    }

    /**
     * GET  /mailboxes/:id : get the "id" mailbox.
     *
     * @param id the id of the mailbox to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mailbox, or with status 404 (Not Found)
     */
    @GetMapping("/mailboxes/{id}")
    @Timed
    public ResponseEntity<Mailbox> getMailbox(@PathVariable Long id) {
        log.debug("REST request to get Mailbox : {}", id);
        Mailbox mailbox = mailboxRepository.findOne(id);
        return Optional.ofNullable(mailbox)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /mailboxes/:id : delete the "id" mailbox.
     *
     * @param id the id of the mailbox to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/mailboxes/{id}")
    @Timed
    public ResponseEntity<Void> deleteMailbox(@PathVariable Long id) {
        log.debug("REST request to delete Mailbox : {}", id);
        mailboxRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("mailbox", id.toString())).build();
    }

}
