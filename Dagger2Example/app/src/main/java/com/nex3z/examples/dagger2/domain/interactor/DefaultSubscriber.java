package com.nex3z.examples.dagger2.domain.interactor;

public class DefaultSubscriber<T> extends rx.Subscriber<T> {

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
    }

    @Override
    public void onNext(T t) {
    }

}

