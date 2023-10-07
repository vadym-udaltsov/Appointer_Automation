package com.commons.dao.impl;

import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeAction;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.commons.DbItemUpdateException;
import com.commons.dao.AbstractDao;
import com.commons.dao.IDepartmentDao;
import com.commons.model.CustomerService;
import com.commons.model.Department;
import com.commons.model.Specialist;
import com.commons.request.admin.AdminRequest;
import com.commons.request.service.UpdateServiceRequest;
import com.commons.request.specialist.CreateSpecialistRequest;
import com.commons.request.specialist.DeleteSpecialistRequest;
import com.commons.request.specialist.UpdateSpecialistRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.telegram.telegrambots.meta.api.objects.Location;

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
    public void addAdmin(AdminRequest request) {
        String departmentName = request.getDepartmentName();
        String customerName = request.getCustomerName();
        String phoneNumber = request.getPhoneNumber();
        String listAttributeName = "adm";
        UpdateItemRequest updateItemRequest = new UpdateItemRequest()
                .withTableName(Department.TABLE_NAME)
                .withKey(Map.of(
                        "c", new AttributeValue(customerName),
                        "n", new AttributeValue(departmentName)))
                .withUpdateExpression("SET " + listAttributeName + " = list_append(if_not_exists(" + listAttributeName + ", :empty_list), :value)")
                .withConditionExpression("NOT contains(#adm, :duplicateValue)")
                .withExpressionAttributeNames(Map.of("#adm", "adm"))
                .withExpressionAttributeValues(Map.of(
                        ":value", new AttributeValue().withL(new AttributeValue(phoneNumber)),
                        ":duplicateValue", new AttributeValue(phoneNumber),
                        ":empty_list", new AttributeValue().withL()));
        try {
            updateItem(updateItemRequest);
        } catch (ConditionalCheckFailedException e) {
            throw new DbItemUpdateException(String.format("Admin with number: %s already exists in department: %s of " +
                    "customer: %s", phoneNumber, departmentName, customerName));
        }
    }

    @Override
    public void deleteAdmin(AdminRequest request) {
        Department item = Department.builder()
                .customer(request.getCustomerName())
                .name(request.getDepartmentName()).build();
        Department department = getItem(item);
        String phoneNumber = request.getPhoneNumber();
        List<String> admins = department.getAdmins();
        if (admins.size() < 2) {
            throw new DbItemUpdateException("Last admin can not be deleted from department");
        }
        boolean deleted = admins.removeIf(phoneNumber::equals);
        if (!deleted) {
            throw new DbItemUpdateException(String.format("Admin with phone number %s not found in department", phoneNumber));
        }
        Map<String, AttributeValueUpdate> updates = new HashMap<>();
        AttributeValueUpdate adminsUpdate = new AttributeValueUpdate(
                new AttributeValue().withL(admins.stream().map(AttributeValue::new).collect(Collectors.toList())), AttributeAction.PUT);
        updates.put("adm", adminsUpdate);
        UpdateItemRequest updateRequest = new UpdateItemRequest()
                .withTableName(Department.TABLE_NAME)
                .withKey(Map.of(
                        "c", new AttributeValue(department.getCustomer()),
                        "n", new AttributeValue(department.getName())))
                .withAttributeUpdates(updates);
        updateItem(updateRequest);
    }

    @Override
    public void addSpecialist(CreateSpecialistRequest request) {
        Specialist specialist = request.getSpecialist();
        String departmentId = request.getDepartmentId();
        Department department = getDepartmentById(departmentId);
        List<Specialist> specialists = department.getAvailableSpecialists();
        boolean specExists = specialists.stream().anyMatch(s -> specialist.getName().equals(s.getName()));
        if (specExists) {
            throw new DbItemUpdateException(String.format("Specialist with name %s already exists", specialist.getName()));
        }
        specialists.add(specialist);
        overwriteItem(department);
    }

    @Override
    public void updateSpecialist(UpdateSpecialistRequest request) {
        String oldName = request.getSpecialistName();
        Specialist specialist = request.getSpecialist();
        String departmentId = request.getDepartmentId();
        Department department = getDepartmentById(departmentId);
        List<Specialist> specialists = department.getAvailableSpecialists();
        Specialist oldSpec = specialists.stream()
                .filter(s -> oldName.equals(s.getName()))
                .findFirst()
                .orElseThrow(() -> new DbItemUpdateException(String.format("Specialist with name %s not found in department", specialist.getName())));

        oldSpec.setName(specialist.getName());
        oldSpec.setPhoneNumber(specialist.getPhoneNumber());
        overwriteItem(department);
    }

    @Override
    public void deleteSpecialist(DeleteSpecialistRequest request) {
        String departmentId = request.getDepartmentId();
        Department department = getDepartmentById(departmentId);
        String specialistName = request.getSpecialistName();
        List<Specialist> specialists = department.getAvailableSpecialists();
        boolean deleted = specialists.removeIf(s -> specialistName.equals(s.getName()));
        if (!deleted) {
            throw new DbItemUpdateException(String.format("Specialist with name %s not found in department", specialistName));
        }
        overwriteItem(department);
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
            throw new DbItemUpdateException(String.format("Service with name %s not found in department", serviceName));
        }
        oldService.setName(newService.getName());
        oldService.setPrice(newService.getPrice());
        oldService.setDuration(newService.getDuration());
        overwriteItem(department);
    }

    @Override
    public void deleteCustomerService(UpdateServiceRequest request) {
        String departmentId = request.getDepartmentId();
        Department department = getDepartmentById(departmentId);
        if (department.getServices().size() == 1) {
            throw new DbItemUpdateException("Last service can not be deleted");
        }
        String serviceName = request.getServiceName();
        boolean deleted = department.getServices().removeIf(s -> serviceName.equals(s.getName()));
        if (!deleted) {
            throw new DbItemUpdateException(String.format("Service with name %s not found in department", serviceName));
        }
        overwriteItem(department);
    }

    @Override
    public void updateToken(String departmentName, String customer, String token) {
        Map<String, AttributeValueUpdate> updates = new HashMap<>();
        AttributeValueUpdate tokenPar = new AttributeValueUpdate(new AttributeValue().withS(token), AttributeAction.PUT);
        updates.put("tn", tokenPar);

        UpdateItemRequest request = new UpdateItemRequest()
                .withTableName(Department.TABLE_NAME)
                .withKey(Map.of(
                        "c", new AttributeValue(customer),
                        "n", new AttributeValue(departmentName)))
                .withAttributeUpdates(updates);

        updateItem(request);
    }

    @Override
    public boolean updateDepartment(Department department) {
        Map<String, AttributeValueUpdate> updates = new HashMap<>();
        AttributeValueUpdate startWork = new AttributeValueUpdate(
                new AttributeValue().withN(String.valueOf(department.getStartWork())), AttributeAction.PUT);
        updates.put("sw", startWork);
        AttributeValueUpdate endWork = new AttributeValueUpdate(
                new AttributeValue().withN(String.valueOf(department.getEndWork())), AttributeAction.PUT);
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
        AttributeValueUpdate appLimitUpdate = new AttributeValueUpdate(new AttributeValue()
                .withN(String.valueOf(department.getAppointmentsLimit())), AttributeAction.PUT);
        updates.put("al", appLimitUpdate);
        AttributeValueUpdate currencyUpdate = new AttributeValueUpdate(new AttributeValue(department.getCurrency()),
                AttributeAction.PUT);
        updates.put("currency", currencyUpdate);
        String description = department.getDescription();
        if (description != null) {
            AttributeValueUpdate descriptionUpd = new AttributeValueUpdate(
                    new AttributeValue().withS(description), AttributeAction.PUT);
            updates.put("desc", descriptionUpd);
        }
        Location location = department.getLocation();
        if (location != null) {
            AttributeValueUpdate locationUpdate = new AttributeValueUpdate(new AttributeValue().withM(Map.of(
                    "longitude", new AttributeValue().withN(String.valueOf(location.getLongitude())),
                    "latitude", new AttributeValue().withN(String.valueOf(location.getLatitude())))),
                    AttributeAction.PUT);
            updates.put("loc", locationUpdate);
        }

        Map<String, String> links = department.getLinks();
        if (links != null) {
            Map<String, AttributeValue> attributeValueMap = links.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e->new AttributeValue().withS(e.getValue())));
            AttributeValueUpdate linksUpdate = new AttributeValueUpdate(new AttributeValue().withM(attributeValueMap),
                    AttributeAction.PUT);
            updates.put("sml", linksUpdate);
        }

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
            throw new DbItemUpdateException(String.format("Service with name %s already exists", service.getName()));
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
