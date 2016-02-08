package com.nex3z.examlpes.mvp.presentation.internal.component;

import android.content.Context;

import com.nex3z.examlpes.mvp.domain.executor.PostExecutionThread;
import com.nex3z.examlpes.mvp.domain.executor.ThreadExecutor;
import com.nex3z.examlpes.mvp.domain.repository.MovieRepository;
import com.nex3z.examlpes.mvp.presentation.internal.module.AppModule;
import com.nex3z.examlpes.mvp.presentation.view.activity.BaseActivity;
import com.nex3z.examlpes.mvp.presentation.view.activity.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(BaseActivity baseActivity);
    void inject(MainActivity activity);

    Context context();
    ThreadExecutor threadExecutor();
    PostExecutionThread postExecutionThread();
    MovieRepository movieRepository();

}
