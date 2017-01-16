package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.GroupAccount;

import com.mycompany.myapp.repository.GroupAccountRepository;
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
 * REST controller for managing GroupAccount.
 */
@RestController
@RequestMapping("/api")
public class GroupAccountResource {

    private final Logger log = LoggerFactory.getLogger(GroupAccountResource.class);
        
    @Inject
    private GroupAccountRepository groupAccountRepository;

    /**
     * POST  /group-accounts : Create a new groupAccount.
     *
     * @param groupAccount the groupAccount to create
     * @return the ResponseEntity with status 201 (Created) and with body the new groupAccount, or with status 400 (Bad Request) if the groupAccount has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/group-accounts")
    @Timed
    public ResponseEntity<GroupAccount> createGroupAccount(@RequestBody GroupAccount groupAccount) throws URISyntaxException {
        log.debug("REST request to save GroupAccount : {}", groupAccount);
        if (groupAccount.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("groupAccount", "idexists", "A new groupAccount cannot already have an ID")).body(null);
        }
        GroupAccount result = groupAccountRepository.save(groupAccount);
        return ResponseEntity.created(new URI("/api/group-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("groupAccount", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /group-accounts : Updates an existing groupAccount.
     *
     * @param groupAccount the groupAccount to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated groupAccount,
     * or with status 400 (Bad Request) if the groupAccount is not valid,
     * or with status 500 (Internal Server Error) if the groupAccount couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/group-accounts")
    @Timed
    public ResponseEntity<GroupAccount> updateGroupAccount(@RequestBody GroupAccount groupAccount) throws URISyntaxException {
        log.debug("REST request to update GroupAccount : {}", groupAccount);
        if (groupAccount.getId() == null) {
            return createGroupAccount(groupAccount);
        }
        GroupAccount result = groupAccountRepository.save(groupAccount);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("groupAccount", groupAccount.getId().toString()))
            .body(result);
    }

    /**
     * GET  /group-accounts : get all the groupAccounts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of groupAccounts in body
     */
    @GetMapping("/group-accounts")
    @Timed
    public List<GroupAccount> getAllGroupAccounts() {
        log.debug("REST request to get all GroupAccounts");
        List<GroupAccount> groupAccounts = groupAccountRepository.findAll();
        return groupAccounts;
    }

    /**
     * GET  /group-accounts/:id : get the "id" groupAccount.
     *
     * @param id the id of the groupAccount to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the groupAccount, or with status 404 (Not Found)
     */
    @GetMapping("/group-accounts/{id}")
    @Timed
    public ResponseEntity<GroupAccount> getGroupAccount(@PathVariable Long id) {
        log.debug("REST request to get GroupAccount : {}", id);
        GroupAccount groupAccount = groupAccountRepository.findOne(id);
        return Optional.ofNullable(groupAccount)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /group-accounts/:id : delete the "id" groupAccount.
     *
     * @param id the id of the groupAccount to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/group-accounts/{id}")
    @Timed
    public ResponseEntity<Void> deleteGroupAccount(@PathVariable Long id) {
        log.debug("REST request to delete GroupAccount : {}", id);
        groupAccountRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("groupAccount", id.toString())).build();
    }

}
