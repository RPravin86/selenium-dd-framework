package com.demo.qa.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a business-level framework method as a reportable test step.
 *
 * <p>The annotation is intentionally independent of the reporting library.
 * Step execution and reporting are handled by the framework's step aspect.</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Step {

    /**
     * Human-readable business step displayed in the execution report.
     *
     * @return step description
     */
    String value();
}
