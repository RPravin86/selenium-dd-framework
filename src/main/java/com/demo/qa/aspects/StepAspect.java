package com.demo.qa.aspects;

import com.demo.qa.annotations.Step;
import com.demo.qa.reportmanager.Report;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * Intercepts business methods annotated with {@link Step} and records them as
 * child nodes beneath the current TestNG test in ExtentReports.
 */
@Aspect
public class StepAspect {

    @Around("execution(* *(..)) && @annotation(step)")
    public Object reportStep(
            ProceedingJoinPoint joinPoint,
            Step step) throws Throwable {

        Report.startStep(step.value());

        try {
            Object result = joinPoint.proceed();
            Report.passStep();
            return result;
        } catch (Throwable throwable) {
            Report.failStep(throwable);
            throw throwable;
        } finally {
            Report.endStep();
        }
    }
}
