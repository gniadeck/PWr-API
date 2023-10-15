package dev.wms.pwrapi.utils.forum.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ReviewNotFoundException extends RuntimeException {

    public ReviewNotFoundException(Long reviewId){
        super(String.format("Opinia o id: %d nie istnieje.", reviewId));
    }
}
