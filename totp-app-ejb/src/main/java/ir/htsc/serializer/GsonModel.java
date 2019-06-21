package ir.htsc.serializer;

import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * Created by me-sharifi on 10/4/2017 copy from Paypal SDK
 */
//@EqualsAndHashCode(callSuper = true)
//@Accessors(chain = true)
@EqualsAndHashCode
public class GsonModel {

    /**
     * Returns a JSON string corresponding to object state
     *
     * @return JSON representation
     */
    public String toJSON() {
        return JSONFormatter.toJSON(this);
    }

    public String toJSONFull() {
        return JSONFormatter.toJSONFull(this);
    }

    public String toJSONElastic() {
        return JSONFormatter.toJSONElastic(this);
    }

    public String toJSONLog() {
        return JSONFormatter.toJSONLog(this);
    }

    public Map toMAP() {
        return JSONFormatter.toMap(this);
    }

    @Override
    public String toString() {
        return toJSON();
    }

}
