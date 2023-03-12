package com.commons.dao;


import com.commons.model.Customer;
import com.commons.model.Department;

public interface ICustomerDao {

    void createItem(Customer customer);

    Customer getItemByHashKeyString(String email);

    void addCustomerDepartment(String email, Department department);
}
