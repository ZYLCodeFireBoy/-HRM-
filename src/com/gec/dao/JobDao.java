package com.gec.dao;

import com.gec.bean.Dept;
import com.gec.bean.Job;
import com.gec.bean.PageModel;

import java.util.List;

/**
 * @ClassName JobDao
 * @Description TODO
 * @Author 钟岩龙
 * @Date 2023/3/2 19:20
 * @Version 1.0
 */

public interface JobDao {
    int getTotalJobCount(Job job);

    List<Job> findJobByPage(Job job, PageModel model);

    void deleteJobById(int id);

    void addJob(Job job);

    void updateJob(Job job);

    Job findJobById(int id);
}
