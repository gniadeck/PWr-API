package dev.wms.pwrapi.utils.forum.exceptions;

public class ReviewNotFoundException extends RuntimeException {

    public ReviewNotFoundException(int reviewId){
        super("Opinia o id: " + reviewId + " nie istnieje.");
    }
}
