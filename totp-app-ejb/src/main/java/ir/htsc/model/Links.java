package ir.htsc.model;

import ir.htsc.serializer.GsonModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Links extends GsonModel {

    /**
     *
     */
    private String href;

    /**
     *
     */
    private String rel;

    /**
     *
     */
    private HyperSchema targetSchema;

    /**
     *
     */
    private String method;

    /**
     *
     */
    private String enctype;

    /**
     *
     */
    private HyperSchema schema;

    /**
     * Default Constructor
     */
    public Links() {
    }

    /**
     * Parameterized Constructor
     */
    public Links(String href, String rel) {
        this.href = href;
        this.rel = rel;
    }
}
