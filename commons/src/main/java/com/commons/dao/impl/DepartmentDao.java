package com.commons.dao.impl;

import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeAction;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.commons.dao.AbstractDao;
import com.commons.dao.IDepartmentDao;
import com.commons.model.CustomerService;
import com.commons.model.Department;
import com.commons.request.UpdateServiceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class DepartmentDao extends AbstractDao<Department> implements IDepartmentDao {

    @Autowired
    public DepartmentDao(DynamoDbFactory dynamoDbFactory) {
        super(dynamoDbFactory, Department.class, "department");
    }

    @Override
    public void updateService(UpdateServiceRequest request) {
        String departmentId = request.getDepartmentId();
        Department department = getDepartmentById(departmentId);
        String serviceName = request.getServiceName();
        CustomerService newService = request.getService();
        CustomerService oldService = department.getServices().stream()
                .filter(s -> serviceName.equals(s.getName()))
                .findFirst()
                .orElse(null);
        if (oldService == null) {
            throw new IllegalArgumentException(String.format("Service with name %s not found in department", serviceName));
        }
        oldService.setName(newService.getName());
        oldService.setPrice(newService.getPrice());
        oldService.setDuration(newService.getDuration());
        overwriteItem(department);
    }

    @Override
    public boolean updateDepartment(Department department) {
        Map<String, AttributeValueUpdate> updates = new HashMap<>();
        AttributeValueUpdate startWork = new AttributeValueUpdate(
                new AttributeValue().withN(String.valueOf(department.getStartWork())), AttributeAction.PUT);
        updates.put("sw", startWork);
        AttributeValueUpdate endWork = new AttributeValueUpdate(
                new AttributeValue().withN(String.valueOf(18)), AttributeAction.PUT);
        updates.put("ew", endWork);
        List<AttributeValue> nonWorkingDays = department.getNonWorkingDays().stream()
                .map(d -> new AttributeValue().withN(String.valueOf(d)))
                .collect(Collectors.toList());
        AttributeValueUpdate nonWorkingDaysUpdate = new AttributeValueUpdate(
                new AttributeValue().withL(nonWorkingDays), AttributeAction.PUT);
        updates.put("nwd", nonWorkingDaysUpdate);
        AttributeValueUpdate zone = new AttributeValueUpdate(
                new AttributeValue().withS(department.getZone()), AttributeAction.PUT);
        updates.put("zone", zone);
        UpdateItemRequest request = new UpdateItemRequest()
                .withTableName(Department.TABLE_NAME)
                .withKey(Map.of(
                        "c", new AttributeValue(department.getCustomer()),
                        "n", new AttributeValue(department.getName())))
                .withAttributeUpdates(updates);
        updateItem(request);
        return true;
    }

    @Override
    public Department getDepartmentById(String departmentId) {
        QuerySpec spec = new QuerySpec()
                .withKeyConditionExpression("#id = :id")
                .withNameMap(new NameMap()
                        .with("#id", "id"))
                .withValueMap(new ValueMap()
                        .withString(":id", departmentId));
        return getItemByIndexQuery(spec, Department.INDEX_NAME);
    }

    @Override
    public void addNewService(String email, String departmentName, CustomerService service) {
        Department department = new Department();
        department.setCustomer(email);
        department.setName(departmentName);

        Department departmentFromDb = getItem(department);
        List<CustomerService> services = departmentFromDb.getServices();
        CustomerService existingService = services.stream()
                .filter(s -> service.getName().equals(s.getName()))
                .findFirst()
                .orElse(null);
        if (existingService != null) {
            throw new IllegalArgumentException(String.format("Service with name %s already exists", service.getName()));
        }
        String listAttributeName = "s";
        AttributeValue newService = new AttributeValue().withM(Map.of(
                "name", new AttributeValue(service.getName()),
                "duration", new AttributeValue().withN(String.valueOf(service.getDuration())),
                "price", new AttributeValue().withN(String.valueOf(service.getPrice()))));

        UpdateItemRequest request = new UpdateItemRequest()
                .withTableName(Department.TABLE_NAME)
                .withKey(Map.of(
                        "c", new AttributeValue(email),
                        "n", new AttributeValue(departmentName)))
                .withUpdateExpression("SET " + listAttributeName + " = list_append(if_not_exists(" + listAttributeName + ", :empty_list), :newServiceList)")
                .withExpressionAttributeValues(Map.of(
                        ":newServiceList", new AttributeValue().withL(newService),
                        ":empty_list", new AttributeValue().withL()));

        updateItem(request);
    }
}
