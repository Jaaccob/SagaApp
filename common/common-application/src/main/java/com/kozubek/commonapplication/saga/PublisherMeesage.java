package com.kozubek.commonapplication.saga;

public interface PublisherMeesage<T> {
    void publish(T t);
}
