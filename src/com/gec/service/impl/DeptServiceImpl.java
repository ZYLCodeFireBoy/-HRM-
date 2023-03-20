package com.gec.service.impl;

import com.gec.bean.Dept;
import com.gec.bean.PageModel;

import com.gec.dao.DeptDao;
import com.gec.dao.impl.DeptDaoImpl;
import com.gec.service.DeptService;

import java.util.List;

/**
 * @ClassName DeptServiceImpl
 * @Description TODO
 * @Author 钟岩龙
 * @Date 2023/3/2 10:10
 * @Version 1.0
 */

public class DeptServiceImpl implements DeptService {

    DeptDao dao=new DeptDaoImpl();

    @Override
    public int getTotalDeptCount(Dept dept) {
        return dao.getTotalDeptCount(dept);
    }



    @Override
    public void deleteDeptById(int id) {
        dao.deleteDeptById(id);
    }

    @Override
    public void addDept(Dept dept) {
        dao.addDept(dept);
    }

    @Override
    public void updateDept(Dept dept) {
        dao.updateDept(dept);
    }

    @Override
    public Dept findDeptById(int id) {
        return dao.findDeptById(id);
    }

    @Override
    public List<Dept> findDeptsByPage(Dept dept, PageModel model) {
        return dao.findDeptsByPage(dept,model);
    }
}
