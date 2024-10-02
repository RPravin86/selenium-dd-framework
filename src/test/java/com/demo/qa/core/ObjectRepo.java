package com.demo.qa.core;

import com.demo.qa.pageobjects.HomePage;
import com.demo.qa.pageobjects.PerfumePage;

public final class ObjectRepo {
    private static final ThreadLocal<ObjectRepo> threadInstance = new ThreadLocal<>();
    HomePage homePage;
    PerfumePage perfumePage;

    private ObjectRepo(){

    }

    public static ObjectRepo getInstance(){
        if(threadInstance.get() == null){
            threadInstance.set(new ObjectRepo());
        }
        return threadInstance.get();
    }

    public HomePage getHomePage(){
        return (homePage == null) ? new HomePage(DriverManager.getDriver()) : homePage;
    }
    public PerfumePage getPerfumePage(){
        return (perfumePage == null) ? new PerfumePage(DriverManager.getDriver()) : perfumePage;
    }

}
