package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A GroupAccount.
 */
@Entity
@Table(name = "group_account")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GroupAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "group_desc")
    private String groupDesc;

    @Column(name = "group_rules")
    private String groupRules;

    @Column(name = "group_email")
    private String groupEmail;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    private ZonedDateTime endDate;

    @OneToOne
    @JoinColumn(unique = true)
    private Calendar groupCalendar;

    @OneToOne
    @JoinColumn(unique = true)
    private Mailbox groupMailbox;

    @OneToOne
    @JoinColumn(unique = true)
    private Media groupMedia;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public GroupAccount groupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public GroupAccount groupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
        return this;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public String getGroupRules() {
        return groupRules;
    }

    public GroupAccount groupRules(String groupRules) {
        this.groupRules = groupRules;
        return this;
    }

    public void setGroupRules(String groupRules) {
        this.groupRules = groupRules;
    }

    public String getGroupEmail() {
        return groupEmail;
    }

    public GroupAccount groupEmail(String groupEmail) {
        this.groupEmail = groupEmail;
        return this;
    }

    public void setGroupEmail(String groupEmail) {
        this.groupEmail = groupEmail;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public GroupAccount startDate(ZonedDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public GroupAccount endDate(ZonedDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public Calendar getGroupCalendar() {
        return groupCalendar;
    }

    public GroupAccount groupCalendar(Calendar calendar) {
        this.groupCalendar = calendar;
        return this;
    }

    public void setGroupCalendar(Calendar calendar) {
        this.groupCalendar = calendar;
    }

    public Mailbox getGroupMailbox() {
        return groupMailbox;
    }

    public GroupAccount groupMailbox(Mailbox mailbox) {
        this.groupMailbox = mailbox;
        return this;
    }

    public void setGroupMailbox(Mailbox mailbox) {
        this.groupMailbox = mailbox;
    }

    public Media getGroupMedia() {
        return groupMedia;
    }

    public GroupAccount groupMedia(Media media) {
        this.groupMedia = media;
        return this;
    }

    public void setGroupMedia(Media media) {
        this.groupMedia = media;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GroupAccount groupAccount = (GroupAccount) o;
        if (groupAccount.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, groupAccount.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "GroupAccount{" +
            "id=" + id +
            ", groupName='" + groupName + "'" +
            ", groupDesc='" + groupDesc + "'" +
            ", groupRules='" + groupRules + "'" +
            ", groupEmail='" + groupEmail + "'" +
            ", startDate='" + startDate + "'" +
            ", endDate='" + endDate + "'" +
            '}';
    }
}
