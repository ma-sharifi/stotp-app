package ir.htsc.entity;

import ir.htsc.serializer.GsonModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by MSH on 5/27/2019.
 */
@MappedSuperclass
@XmlRootElement//Don't Care. Not Important
@XmlAccessorType(XmlAccessType.FIELD)
@Getter @Setter
@EqualsAndHashCode(callSuper = true)
public abstract class BaseEntity extends GsonModel implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================

    @Column(name = "CREATE_AT")
    @XmlElement(name = "create_at")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createAt;

    @Column(name = "STATUS")
    protected Integer status;

    @Column(name = "DESCRIPTION", length = 4000)
    protected String description;

}
