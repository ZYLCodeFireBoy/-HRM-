package com.gec.dao;

import com.gec.bean.Dept;
import com.gec.bean.PageModel;
import com.gec.bean.User;

import java.util.List;

/**
 * @ClassName DeptDao
 * @Description TODO
 * @Author 钟岩龙
 * @Date 2023/3/2 10:15
 * @Version 1.0
 */

public interface DeptDao {


    int getTotalDeptCount(Dept dept);

    List<Dept> findDeptsByPage(Dept dept, PageModel model);

    void deleteDeptById(int id);

    void addDept(Dept dept);

    void updateDept(Dept dept);

    Dept findDeptById(int id);
}
