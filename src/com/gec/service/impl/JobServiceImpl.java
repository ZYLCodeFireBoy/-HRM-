package com.gec.service.impl;

import com.gec.bean.Dept;
import com.gec.bean.Job;
import com.gec.bean.PageModel;
import com.gec.dao.JobDao;
import com.gec.dao.impl.JobDaoImpl;
import com.gec.service.JobService;

import javax.servlet.http.HttpServlet;
import java.util.List;

/**
 * @ClassName JobServiceImpl
 * @Description TODO
 * @Author 钟岩龙
 * @Date 2023/3/2 19:18
 * @Version 1.0
 */

public class JobServiceImpl implements JobService {

    JobDao dao=new JobDaoImpl();

    @Override
    public int getTotalJobCount(Job job) {
        return dao.getTotalJobCount(job);
    }

    @Override
    public List<Job> findJobByPage(Job job, PageModel model) {
        return dao.findJobByPage(job,model);
    }

    @Override
    public void deleteJobById(int id) {
        dao.deleteJobById(id);
    }

    @Override
    public void addJob(Job job) {
        dao.addJob(job);
    }

    @Override
    public void updateJob(Job job) {
        dao.updateJob(job);
    }

    @Override
    public Job findJobById(int id) {
        return dao.findJobById(id);
    }
}
