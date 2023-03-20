package com.gec.service.impl;

import com.gec.bean.Dept;
import com.gec.bean.Employee;
import com.gec.bean.Job;
import com.gec.bean.PageModel;
import com.gec.dao.EmpDao;
import com.gec.dao.impl.EmpDaoImpl;
import com.gec.service.EmpService;

import java.util.List;

/**
 * @ClassName EmpServiceImpl
 * @Description TODO
 * @Author 钟岩龙
 * @Date 2023/3/3 15:54
 * @Version 1.0
 */

public class EmpServiceImpl implements EmpService {

    EmpDao dao=new EmpDaoImpl();

    @Override
    public int getTotalEmpCount(Employee employee) {
        return dao.getTotalEmpCount(employee);
    }

    @Override
    public List<Employee> findEmpByPage(Employee employee, PageModel model) {
        return dao.findEmpByPage(employee,model);
    }

    @Override
    public void deleteEmpById(int id) {
        dao.deleteEmpById(id);
    }

    @Override
    public void addEmp(Employee employee) {
        dao.addEmp(employee);
    }

    @Override
    public void updateEmp(Employee employee) {
        dao.updateEmp(employee);
    }

    @Override
    public Employee findEmpById(int id) {
        return dao.findEmpById(id);
    }

    @Override
    public List<Dept> getdeptsList() {
        return dao.getdeptsList();
    }

    @Override
    public List<Job> getjobsList() {
        return dao.getjobsList();
    }
}
