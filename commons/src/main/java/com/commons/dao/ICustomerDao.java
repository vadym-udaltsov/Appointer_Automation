package com.commons.dao;


import com.commons.model.Customer;
import com.commons.model.Department;

public interface ICustomerDao {

    boolean createItem(Customer customer);

    Customer getItemByHashKey(Object email);

    void registerCustomer(String email);

    void addCustomerDepartment(String email, Department department);
}
