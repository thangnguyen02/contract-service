package com.fpt.servicecontract.utils;

public interface Constants {

    public static interface ResponseCode {

        final public static String SUCCESS = "00";
        final public static String FAILURE = "01";

        final public static String AccessDenied = "02";
        final public static String NOT_FOUND = "03";
        final public static String KAFKA_SEND_FAILURE = "04";
        final public static String CONNECTION_TIMEOUT = "05";
        final public static String REQUEST_TIMEOUT = "06";
        final public static String EXPIRED_JWT = "07";
        final public static String RESOURCE_ACCESS = "08";
    }
    public static interface STATUS {
        final public static String NEW = "NEW";
        final public static String SIGN_A = "SIGN_A";
        final public static String SIGN_B = "SIGN_B";
        final public static String REJECT_A = "REJECT_A";
        final public static String REJECT_B = "REJECT_B";
        final public static String UPDATE = "UPDATE";
        final public static String PROCESSING = "PROCESSING";
        final public static String SUCCESS = "SUCCESS";
    }

    public static interface POSITION {
        final public static String SALE = "SALE";
        final public static String LEADER_SALE = "LEADER_SALE";
    }

}
