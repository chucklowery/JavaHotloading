package app.interfaces;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EventGateway {

    String DEFAULT_RESOURCE = "Undefined Resource";

    String resource() default DEFAULT_RESOURCE;
}
