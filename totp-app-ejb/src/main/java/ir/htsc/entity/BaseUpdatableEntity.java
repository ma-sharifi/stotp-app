package ir.htsc.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.Date;

/**
 * Created by me-sharifi on 12/18/2016.
 */
@MappedSuperclass
@XmlAccessorType(XmlAccessType.FIELD)
@EqualsAndHashCode(callSuper = true)
@Setter
@Getter
public abstract class BaseUpdatableEntity extends BaseEntity{

    // ======================================
    // =             Attributes             =
    // ======================================

    @Column(name = "UPDATE_AT")
    @XmlElement(name = "update_at")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date updateAt;

    @XmlElement(name = "enabled")
    @Column(name = "ENABLED")
    private Boolean enabled;

    @Column(name = "VERSION")
    protected long version;

    // ======================================
    // =     Lifecycle Callback Methods     =
    // ======================================

    @PreUpdate
    private void onPreUpdate(){
        setUpdateAt(new Date());
        setVersion(version++);//TODO
    }

    // ======================================
    // =          Getters & Setters         =
    // ======================================


    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

}
