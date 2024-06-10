package com.app.mapper;

public interface Mapper<F, T> {

    T map (F from);
}
