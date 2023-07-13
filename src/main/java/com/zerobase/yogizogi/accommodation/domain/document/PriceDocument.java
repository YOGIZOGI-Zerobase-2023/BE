package com.zerobase.yogizogi.accommodation.domain.document;

import java.time.LocalDate;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "price", createIndex = true)
public class PriceDocument {

    @Field(type = FieldType.Auto, value = "priceId")
    private Long id;

    @Field(type = FieldType.Integer, value = "price")
    private Integer price;

    @Field(type = FieldType.Date, value = "date")
    private LocalDate date;

    @Field(type = FieldType.Integer, value = "roomCnt")
    private int roomCnt;


}
