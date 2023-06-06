package com.example.tiremileage.repository;

import android.app.Application;

public class RepositoryManager {
    static private Repository repository = null;

    public static Repository getRepository() {
        if ( repository != null) {
            return repository;
        } else {
            throw new RuntimeException("repository is null");
        }
    }
    public static void init(Application application){
        if (repository == null){
            repository = new Repository(application);
            repository.initOkHTTPModule();
        }
    }
}
