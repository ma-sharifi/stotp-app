package ir.htsc.serializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by me-sharifi on 10/21/2017.
 */
//https://howtoprogram.xyz/2016/10/16/ignore-or-exclude-field-in-gson/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GsonExcludeField {
}
