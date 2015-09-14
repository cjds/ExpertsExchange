package com.carlsaldanha.expertsexchange;

import java.sql.Time;

/**
 * Created by Carl Saldanha on 9/10/2015.
 */
public class StepRecorder {

    int user_id;
    int task_id;
    int expert_user_id;
    long current_step_id;
    long start_time;
    long end_time;

    StepRecorder(int user_id,int task_id,int expert_user_id,long current_step_id){

        record_start_time();
        this.user_id=user_id;
        this.task_id=task_id;
        this.expert_user_id=expert_user_id;
        this.current_step_id=current_step_id;
    }

    public void record_start_time(){
        start_time=System.currentTimeMillis();
    }

    public void record_end_time(){
        end_time=System.currentTimeMillis();
    }

    public void set_step_id(int step_id){
        current_step_id=step_id;
    }
}
