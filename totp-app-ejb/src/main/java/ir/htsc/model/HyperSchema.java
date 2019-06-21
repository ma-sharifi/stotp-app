package ir.htsc.model;

import ir.htsc.serializer.GsonModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class HyperSchema extends GsonModel {

    /**
     *
     */
    private List<Links> links;

    /**
     *
     */
    private String fragmentResolution;

    /**
     *
     */
    private Boolean readonly;

    /**
     *
     */
    private String contentEncoding;

    /**
     *
     */
    private String pathStart;

    /**
     *
     */
    private String mediaType;

    /**
     * Default Constructor
     */
    public HyperSchema() {
    }
}
