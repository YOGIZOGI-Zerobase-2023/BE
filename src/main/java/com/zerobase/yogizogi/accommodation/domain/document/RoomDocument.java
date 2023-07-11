package com.zerobase.yogizogi.accommodation.domain.document;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "room", createIndex = true)
public class RoomDocument {

    @Field(type = FieldType.Auto, value = "roomId")
    private Long id;

    @Field(type = FieldType.Text, value = "name")
    private String name;

    @Field(type = FieldType.Text, value = "checkIn")
    private String checkInTime;

    @Field(type = FieldType.Text, value = "checkOut")
    private String cheekOutTime;

    @Field(type = FieldType.Integer, value = "defaultPeople")
    private Integer defaultPeople;

    @Field(type = FieldType.Integer, value = "maxPeople")
    private Integer maxPeople;

    @Field(type = FieldType.Text, value = "conveniences")
    private String conveniences;

}
