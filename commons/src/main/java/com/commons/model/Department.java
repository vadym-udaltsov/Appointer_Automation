package com.commons.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import lombok.Data;

import java.util.List;

@Data
@DynamoDBDocument
public class Department {

    private String name;

    @DynamoDBTypeConvertedEnum
    private DepartmentType type;

    private List<String> availableSpecialists;
}
