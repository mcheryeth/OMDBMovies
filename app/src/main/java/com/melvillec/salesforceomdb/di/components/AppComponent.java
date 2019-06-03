package com.melvillec.salesforceomdb.di.components;

import android.app.Application;

import com.melvillec.salesforceomdb.MoviesApplication;
import com.melvillec.salesforceomdb.di.modules.ActivityModule;
import com.melvillec.salesforceomdb.di.modules.ApiModule;
import com.melvillec.salesforceomdb.di.modules.DbModule;
import com.melvillec.salesforceomdb.di.modules.FragmentModule;
import com.melvillec.salesforceomdb.di.modules.ViewModelModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {DbModule.class, ApiModule.class, ViewModelModule.class,
        ActivityModule.class, FragmentModule.class, AndroidSupportInjectionModule.class})
public interface AppComponent {

    /* Called from our custom Application class.
     * This will set our application object to the AppComponent.
     * So inside the AppComponent the application instance is available.
     * So this application instance can be accessed by our modules
     * such as ApiModule when needed
     * */
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        @BindsInstance
        Builder apiModule(ApiModule apiModule);

        @BindsInstance
        Builder dbModule(DbModule dbModule);

        AppComponent build();
    }

    // This is our custom Application class
    void inject(MoviesApplication application);

}
