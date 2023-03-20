package com.gec.service;

import com.gec.bean.Dept;
import com.gec.bean.Job;
import com.gec.bean.PageModel;

import java.util.List;

/**
 * @ClassName JobService
 * @Description TODO
 * @Author 钟岩龙
 * @Date 2023/3/2 19:17
 * @Version 1.0
 */

public interface JobService {
    int getTotalJobCount(Job job);

    List<Job> findJobByPage(Job job, PageModel model);


    void deleteJobById(int id);

    void addJob(Job job);

    void updateJob(Job job);

    Job findJobById(int id);
}
