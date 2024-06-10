package com.app.mapper;

import com.app.exception.RequestMappingException;
import jakarta.servlet.http.HttpServletRequest;

public interface ServletRequestMapper<T> {

    T mapFromRequest(HttpServletRequest httpServletRequest) throws RequestMappingException;
}
