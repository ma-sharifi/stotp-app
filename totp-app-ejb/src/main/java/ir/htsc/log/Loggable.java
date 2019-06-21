package ir.htsc.log;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.*;

/**
 * Created by me-sharifi on 2/13/2017.
 */
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
//@Inherited
public @interface Loggable {
    TargetType value() default TargetType.DB;

}
