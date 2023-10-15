package dev.wms.pwrapi.utils.common;


public record PageRequest(int limit, int offset) {

    public static PageRequest of(int limit, int offset){
        return new PageRequest(limit, offset);
    }

}
