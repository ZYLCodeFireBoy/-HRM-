package com.gec.service;

import com.gec.bean.Dept;
import com.gec.bean.Employee;
import com.gec.bean.Job;
import com.gec.bean.PageModel;

import java.util.List;

/**
 * @ClassName EmpService
 * @Description TODO
 * @Author 钟岩龙
 * @Date 2023/3/3 15:52
 * @Version 1.0
 */

public interface EmpService {

    int getTotalEmpCount(Employee employee);

    List<Employee> findEmpByPage(Employee employee, PageModel model);


    void deleteEmpById(int id);

    void addEmp(Employee employee);

    void updateEmp(Employee employee);

    Employee findEmpById(int id);

    List<Dept> getdeptsList();

    List<Job> getjobsList();

}
