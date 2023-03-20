package com.gec.service;

import com.gec.bean.Dept;
import com.gec.bean.PageModel;
import com.gec.bean.User;

import java.util.List;

/**
 * @ClassName DeptService
 * @Description TODO
 * @Author 钟岩龙
 * @Date 2023/3/2 9:53
 * @Version 1.0
 */

public interface DeptService {

    int getTotalDeptCount(Dept dept);

    List<Dept> findDeptsByPage(Dept dept, PageModel model);


    void deleteDeptById(int id);

    void addDept(Dept dept);

    void updateDept(Dept dept);

    Dept findDeptById(int id);
}
