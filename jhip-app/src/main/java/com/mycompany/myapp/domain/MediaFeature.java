package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.mycompany.myapp.domain.enumeration.FeatureType;

/**
 * A MediaFeature.
 */
@Entity
@Table(name = "media_feature")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MediaFeature implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "media_type")
    private FeatureType featureType;

    @Column(name = "media_desc")
    private String mediaDesc;

    @Column(name = "create_date")
    private ZonedDateTime createDate;

    @Column(name = "date_modified")
    private ZonedDateTime dateModified;

    @ManyToOne
    private Media media;

    @OneToOne
    @JoinColumn(unique = true)
    private File mediaFeatureFile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FeatureType getFeatureType() {
        return featureType;
    }

    public MediaFeature featureType(FeatureType featureType) {
        this.featureType = featureType;
        return this;
    }

    public void setFeatureType(FeatureType featureType) {
        this.featureType = featureType;
    }

    public String getMediaDesc() {
        return mediaDesc;
    }

    public MediaFeature mediaDesc(String mediaDesc) {
        this.mediaDesc = mediaDesc;
        return this;
    }

    public void setMediaDesc(String mediaDesc) {
        this.mediaDesc = mediaDesc;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public MediaFeature createDate(ZonedDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public ZonedDateTime getDateModified() {
        return dateModified;
    }

    public MediaFeature dateModified(ZonedDateTime dateModified) {
        this.dateModified = dateModified;
        return this;
    }

    public void setDateModified(ZonedDateTime dateModified) {
        this.dateModified = dateModified;
    }

    public Media getMedia() {
        return media;
    }

    public MediaFeature media(Media media) {
        this.media = media;
        return this;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public File getMediaFeatureFile() {
        return mediaFeatureFile;
    }

    public MediaFeature mediaFeatureFile(File file) {
        this.mediaFeatureFile = file;
        return this;
    }

    public void setMediaFeatureFile(File file) {
        this.mediaFeatureFile = file;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MediaFeature mediaFeature = (MediaFeature) o;
        if (mediaFeature.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, mediaFeature.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MediaFeature{" +
            "id=" + id +
            ", featureType='" + featureType + "'" +
            ", mediaDesc='" + mediaDesc + "'" +
            ", createDate='" + createDate + "'" +
            ", dateModified='" + dateModified + "'" +
            '}';
    }
}
