package com.embroidermodder.embroideryviewer;

public class EmbThread {
    private EmbColor color;
    private String description;
    private String catalogNumber;

    public void setColor(EmbColor value){
        color = value;
    }

    public EmbColor getColor(){
        return color;
    }

    public void setDescription(String value){
        description = value;
    }

    public String getDescription(){
        return description;
    }

    public void setCatalogNumber(String value){
        catalogNumber = value;
    }

    public String getCatalogNumber(){
        return catalogNumber;
    }
}