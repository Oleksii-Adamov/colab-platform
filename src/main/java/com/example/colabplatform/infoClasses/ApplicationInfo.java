package com.example.colabplatform.infoClasses;

import com.example.colabplatform.enitities.Application;

public class ApplicationInfo {
    private String userFullName;
    private Application application;

    public ApplicationInfo(String userFullName, Application application) {
        this.userFullName = userFullName;
        this.application = application;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }
}
