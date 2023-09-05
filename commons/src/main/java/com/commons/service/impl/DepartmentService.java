package com.commons.service.impl;

import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.commons.dao.IDepartmentDao;
import com.commons.model.CustomerService;
import com.commons.model.Department;
import com.commons.model.Specialist;
import com.commons.request.admin.AdminRequest;
import com.commons.request.service.UpdateServiceRequest;
import com.commons.request.specialist.CreateSpecialistRequest;
import com.commons.request.specialist.DeleteSpecialistRequest;
import com.commons.request.specialist.UpdateSpecialistRequest;
import com.commons.service.IDepartmentService;
import com.commons.utils.Constants;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService implements IDepartmentService {

    private IDepartmentDao departmentDao;

    @Autowired
    public DepartmentService(IDepartmentDao departmentDao) {
        this.departmentDao = departmentDao;
    }

    @Override
    public boolean updateDepartment(Department department) {
        return departmentDao.updateDepartment(department);
    }

    @Override
    public void updateToken(String departmentName, String customer, String token) {
        departmentDao.updateToken(departmentName, customer, token);
    }

    @Override
    public void updateSpecialist(UpdateSpecialistRequest request) {
        departmentDao.updateSpecialist(request);
    }

    @Override
    public void deleteSpecialist(DeleteSpecialistRequest request) {
        if (Constants.OWNER.equalsIgnoreCase(request.getSpecialistName())) {
            throw new IllegalArgumentException("Owner can not be deleted");
        }
        departmentDao.deleteSpecialist(request);
    }

    @Override
    public void addSpecialist(CreateSpecialistRequest request) {
        Specialist specialist = request.getSpecialist();
        String name = specialist.getName();
        if ("".equals(name)) {
            throw new IllegalArgumentException("Specialist name should not be empty");
        }
        specialist.setId(RandomStringUtils.randomAlphabetic(4));
        departmentDao.addSpecialist(request);
    }

    @Override
    public void updateCustomerService(UpdateServiceRequest request) {
        departmentDao.updateService(request);
    }

    @Override
    public void deleteCustomerService(UpdateServiceRequest request) {
        departmentDao.deleteCustomerService(request);
    }

    @Override
    public Department getDepartmentById(String departmentId) {
        return departmentDao.getDepartmentById(departmentId);
    }

    @Override
    public void addAdmin(AdminRequest request) {
        departmentDao.addAdmin(request);
    }

    @Override
    public void deleteAdmin(AdminRequest request) {
        departmentDao.deleteAdmin(request);
    }

    @Override
    public void addCustomerService(String email, String departmentName, CustomerService service) {
        if (service == null || "".equals(service.getName())) {
            throw new IllegalArgumentException("Service name can not be null or empty");
        }
        departmentDao.addNewService(email, departmentName, service);
    }

    @Override
    public List<Department> getCustomerDepartments(String customer) {
        QuerySpec querySpec = new QuerySpec().withHashKey("c", customer);
        return departmentDao.findAllByQuery(querySpec);
    }

    @Override
    public boolean createDepartment(Department department) {
        return departmentDao.createItem(department);
    }

}
