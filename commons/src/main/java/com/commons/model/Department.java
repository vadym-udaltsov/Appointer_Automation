package com.commons.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.commons.converter.ListServicesConverter;
import com.commons.converter.ListSpecialistsConverter;
import com.commons.utils.JsonUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import org.telegram.telegrambots.meta.api.objects.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Jacksonized
@DynamoDBTable(tableName = "department")
@Builder
@NoArgsConstructor()
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Department extends DynamoDbEntity {
    private static final String HASH_KEY = "c";
    private static final String RANGE_KEY = "n";
    public static final String INDEX_NAME = "id-index";
    public static final String TABLE_NAME = "department";

    @DynamoDBHashKey(attributeName = HASH_KEY)
    @JsonProperty("c")
    private String customer;

    @DynamoDBRangeKey(attributeName = RANGE_KEY)
    @JsonProperty("n")
    private String name;

    @DynamoDBIndexHashKey(globalSecondaryIndexName = INDEX_NAME)
    private String id;

    @DynamoDBTypeConvertedEnum
    @JsonProperty("tp")
    @DynamoDBAttribute(attributeName = "tp")
    private DepartmentType type;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, value = "tn")
    private String token;

    @JsonProperty("as")
    @DynamoDBAttribute(attributeName = "as")
    @DynamoDBTypeConverted(converter = ListSpecialistsConverter.class)
    private List<Specialist> availableSpecialists = new ArrayList<>();

    @JsonProperty("s")
    @DynamoDBAttribute(attributeName = "s")
    @DynamoDBTypeConverted(converter = ListServicesConverter.class)
    private List<CustomerService> services = new ArrayList<>();

    @JsonProperty("sw")
    @DynamoDBAttribute(attributeName = "sw")
    private int startWork;

    @JsonProperty("ew")
    @DynamoDBAttribute(attributeName = "ew")
    private int endWork;

    @JsonProperty("nwd")
    @DynamoDBAttribute(attributeName = "nwd")
    private List<Integer> nonWorkingDays = new ArrayList<>();

    @JsonProperty("zone")
    private String zone;

    @JsonProperty("adm")
    @DynamoDBAttribute(attributeName = "adm")
    private List<String> admins;

    @JsonProperty("dof")
    @DynamoDBAttribute(attributeName = "dof")
    private Map<String, List<BusySlot>> daysOff;

    @JsonProperty("bun")
    private String botUserName;

    @JsonProperty("bn")
    private String botName;

    @JsonProperty("al")
    private int appointmentsLimit;

    @JsonProperty("loc")
    private Location location;

    @JsonProperty("desc")
    private String description;

    @JsonProperty("sml")
    @DynamoDBAttribute(attributeName = "sml")
    private Map<String, String> links = new HashMap<>();

    @JsonProperty("cd")
    private long creationDate;

    @Override
    @JsonIgnore
    public PrimaryKey getPrimaryKey() {
        return new PrimaryKey(HASH_KEY, customer, RANGE_KEY, name);
    }

    @Override
    public Item toItem() {
        return new Item()
                .with(HASH_KEY, customer)
                .with(RANGE_KEY, name)
                .with("id", id)
                .with("tp", type.name())
                .withString("tn", token)
                .withNumber("sw", startWork)
                .withNumber("ew", endWork)
                .withString("zone", zone == null ? "" : zone)
                .withString("bun", botUserName == null ? "" : botUserName)
                .withString("bn", botName == null ? "" : botName)
                .withList("as", availableSpecialists.stream().map(JsonUtils::parseObjectToMap).collect(Collectors.toList()))
                .withList("nwd", nonWorkingDays == null ? new ArrayList<>() : nonWorkingDays)
                .withList("adm", admins == null ? new ArrayList<>() : admins)
                .withMap("dof", daysOff == null ? new HashMap<>() : daysOff)
                .withMap("loc", Map.of(
                        "longitude", location == null ? 0 : location.getLongitude(),
                        "latitude", location == null ? 0 : location.getLatitude()
                ))
                .withNumber("al", appointmentsLimit)
                .withNumber("cd", creationDate)
                .withString("desc", description == null ? "" : description)
                .withList("s", services.stream().map(JsonUtils::parseObjectToMap).collect(Collectors.toList()))
                .withMap("sml", links == null ? new HashMap<>() : links);
    }

    @Override
    @JsonIgnore
    public String getCondition() {
        return String.format("attribute_not_exists(%s)", RANGE_KEY);
    }
}
