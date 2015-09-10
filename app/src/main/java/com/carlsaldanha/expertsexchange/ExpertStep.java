package com.carlsaldanha.expertsexchange;

/**
 * Created by Carl Saldanha on 9/9/2015.
 */
public class ExpertStep {
    long _id;
    long id_order;
    String instruction;
    String media_path;
    int media_type;
    int user_id;
    int task_id;

    String[] columns={"_id","id_order","instruction","media_path","media_type","user_id","task_id"};
}
