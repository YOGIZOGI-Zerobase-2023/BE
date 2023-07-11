package com.zerobase.yogizogi.accommodation.domain.document;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "accommodation", createIndex = true)
public class Accommodation {

    @Field(type = FieldType.Auto, value = "accommodationId")
    private Long id;

    @Field(type = FieldType.Integer, value = "category")
    private int category;

    @Field(type = FieldType.Text, value = "name")
    private String name;

    @Field(type = FieldType.Double, value = "rate")
    private double rate;

    @Field(type = FieldType.Text, value = "region")
    private String region;


}