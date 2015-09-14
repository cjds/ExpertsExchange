package com.carlsaldanha.expertsexchange;

/**
 * Created by Carl Saldanha on 9/7/2015.
 */
public class Step {
    public static final String TABLE_NAME="step";
    public static final String COLUMN__NAME_STEP="text";
    public static final String COLUMN__NAME_TYPE="type";
    public static final String COLUMN__NAME_LINK="link";

    public String link;
    public String type;
    public String text;
    public  long id;

    public Step(String link,String type, String text, long id){
        this.link=link;
        this.type=type;
        this.id=id;
        this.text=text.substring(0,1).toUpperCase()+text.substring(1);
    }


}
