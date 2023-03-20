package com.gec.dao.impl;

import com.gec.bean.Dept;
import com.gec.bean.Job;
import com.gec.bean.PageModel;
import com.gec.dao.JobDao;
import com.gec.util.JDBCUtils;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName JobDaoImpl
 * @Description TODO
 * @Author 钟岩龙
 * @Date 2023/3/2 19:22
 * @Version 1.0
 */

public class JobDaoImpl implements JobDao {
    @Override
    public int getTotalJobCount(Job job) {
        // 1=1 恒等式 2=2 3=3 为了方便后续可以动态添加条件
        StringBuffer sql = new StringBuffer("select count(0) from job_inf where 1=1  and state=0  ");

        List<Object> objects = new ArrayList<>();
        ResultSet rs = null;
        try{
            // 防止空指针异常
            if(job != null && !"".equals(job.getName()) && job.getName() != null){
                sql.append(" and NAME like ?");
                objects.add("%" + job.getName() +"%");
            }

            //分页查询 limit 0,2
            //动态参数数组 把list转成数组
            Object[] params = objects.toArray(new Object[objects.size()]);
            rs = JDBCUtils.executeQuery(sql.toString(),params);

            while(rs.next()){
                //返回  统计数
                return rs.getInt(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeConn(JDBCUtils.getConnection(),JDBCUtils.getPsmt(),rs);
        }
        return 0;
    }

    @Override
    public List<Job> findJobByPage(Job job, PageModel model) {

        List<Job> jobs = new ArrayList<>();
        // 1=1 恒等式 2=2 3=3 为了方便后续可以动态添加条件
        StringBuffer sql = new StringBuffer("select * from job_inf where 1=1 and state=0");

        List<Object> objects = new ArrayList<>();
        System.out.println("objects:" + objects);

        ResultSet rs = null;
        try{
            // 防止空指针异常
            if(job != null && !"".equals(job.getName()) && job.getName() != null){
                sql.append(" and name like ?");
                objects.add("%" + job.getName() +"%");
            }

            //分页查询 limit 0,2
            sql.append(" limit ").append(model.getStartRow()).append(",").append(PageModel.pageSize);


            //动态参数数组 把list转成数组
            Object[] params = objects.toArray(new Object[objects.size()]);
            rs = JDBCUtils.executeQuery(sql.toString(),params);

            while(rs.next()){
                Job j = new Job();
                copyData(rs,j);
                jobs.add(j);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeConn(JDBCUtils.getConnection(),JDBCUtils.getPsmt(),rs);
        }
        return jobs;
    }

    @Override
    public void deleteJobById(int id) {
        //软删除，逻辑删除，将状态改为1
        String sql="update  job_inf  set state=1 where id = ?";
        //参数
        Object[]params={id};
        ResultSet rs=null;
        try{
            JDBCUtils.excuteUpdate(sql,params);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeConn(JDBCUtils.getConnection(), JDBCUtils.getPsmt(),rs);
        }
    }

    @Override
    public void addJob(Job job) {
        String sql="insert into job_inf (NAME,REMARK,state)values(?,?,?)";
        //参数
        Object[]params={job.getName(),job.getRemark(),job.getState()};

        ResultSet rs=null;
        try{
            JDBCUtils.excuteUpdate(sql,params);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeConn(JDBCUtils.getConnection(), JDBCUtils.getPsmt(),rs);
        }
    }

    @Override
    public void updateJob(Job job) {
        String sql="update  job_inf set NAME=?,REMARK=?,state=? where id =?";
        //参数
        Object[]params={job.getName(),job.getRemark(),job.getState(),job.getId()};
        ResultSet rs=null;
        try{
            JDBCUtils.excuteUpdate(sql,params);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeConn(JDBCUtils.getConnection(), JDBCUtils.getPsmt(),rs);
        }
    }

    @Override
    public Job findJobById(int id) {
        String sql="select * from job_inf where id=?";
        //参数
        Object[] params = {id};

        ResultSet rs = null;
        try{
            //有个领导会检查代码（团队空闲时）
            rs = JDBCUtils.executeQuery(sql, params);
            while (rs.next()){
                //封装数据
                Job job = new Job();
                copyData(rs,job);
                return job;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeConn(JDBCUtils.getConnection(),JDBCUtils.getPsmt(),rs);
        }
        return null;
    }

    private void copyData(ResultSet rs, Job job) {
        try{
            job.setId(rs.getInt("id"));
            job.setName(rs.getString("name"));
            job.setRemark(rs.getString("remark"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
