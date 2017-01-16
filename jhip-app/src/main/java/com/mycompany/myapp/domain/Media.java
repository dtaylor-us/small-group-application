package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Media.
 */
@Entity
@Table(name = "media")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Media implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "media_name")
    private String mediaName;

    @OneToMany(mappedBy = "media")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<MediaFeature> mailboxCalendars = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMediaName() {
        return mediaName;
    }

    public Media mediaName(String mediaName) {
        this.mediaName = mediaName;
        return this;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public Set<MediaFeature> getMailboxCalendars() {
        return mailboxCalendars;
    }

    public Media mailboxCalendars(Set<MediaFeature> mediaFeatures) {
        this.mailboxCalendars = mediaFeatures;
        return this;
    }

    public Media addMailboxCalendar(MediaFeature mediaFeature) {
        mailboxCalendars.add(mediaFeature);
        mediaFeature.setMedia(this);
        return this;
    }

    public Media removeMailboxCalendar(MediaFeature mediaFeature) {
        mailboxCalendars.remove(mediaFeature);
        mediaFeature.setMedia(null);
        return this;
    }

    public void setMailboxCalendars(Set<MediaFeature> mediaFeatures) {
        this.mailboxCalendars = mediaFeatures;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Media media = (Media) o;
        if (media.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, media.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Media{" +
            "id=" + id +
            ", mediaName='" + mediaName + "'" +
            '}';
    }
}
