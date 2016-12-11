package com.nex3z.examples.dagger2.internal.component;

import com.nex3z.examples.dagger2.internal.PerActivity;
import com.nex3z.examples.dagger2.internal.module.RestModule;
import com.nex3z.examples.dagger2.ui.fragment.MovieGridFragment;

import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class, modules = RestModule.class)
public interface RestComponent {
    void inject(MovieGridFragment fragment);
}
