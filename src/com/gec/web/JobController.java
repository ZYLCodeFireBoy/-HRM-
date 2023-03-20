package com.gec.web;

import com.gec.bean.Dept;
import com.gec.bean.Job;
import com.gec.bean.PageModel;
import com.gec.service.DeptService;
import com.gec.service.JobService;
import com.gec.service.impl.DeptServiceImpl;
import com.gec.service.impl.JobServiceImpl;
import com.gec.util.CrossRegion;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName JobController
 * @Description TODO
 * @Author 钟岩龙
 * @Date 2023/3/2 19:16
 * @Version 1.0
 */
@WebServlet(urlPatterns = {"/findJobByPage", "/saveJob",
        "/deleteJob","/updateJob","/findJobById"})
public class JobController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置请求编码
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");

        //跨域设置
        CrossRegion.setCross(resp);
        //如果是get,设置200ok
        if ("OPTIONS".equals(req.getMethod())) {
            resp.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        //请求的servlet 路径
        String servletPath = req.getServletPath();
        //service对象
        JobService service=new JobServiceImpl();

        if(servletPath.equals("/findJobByPage")){
            //获得当前页码
            String pageIndex = req.getParameter("pageIndex");
            int page = !"".equals(pageIndex) && pageIndex != null ? Integer.parseInt(pageIndex) : 1;
            //设置到PageModel
            PageModel model = new PageModel();
            model.setPageIndex(page);
            //查询用户列表
            List<Job> jobs = null;
            //提供部门名称查询
            String Jname = req.getParameter("Jname");
            Job job = new Job();
            job.setName(Jname);
            //总记录数
            int count=service.getTotalJobCount(job);
            model.setTotalRecoreSum(count);
            jobs=service.findJobByPage(job,model);
            Map<String,Object> map=new HashMap<>();
            map.put("model",model);
            map.put("jobs",jobs);
            //使用json返回
            Gson gson =new Gson();
            String s = gson.toJson(map);
            //响应前台
            resp.getWriter().write(s);
        }else if(servletPath.equals("/deleteJob")){
            int id = Integer.parseInt(req.getParameter("id"));
            //service
            service.deleteJobById(id);
            Map<String,Object> map = new HashMap<>();
            map.put("code",200);
            map.put("message","delete success");
            //Gson
            Gson gson = new Gson();
            String s = gson.toJson(map);
            resp.getWriter().write(s);
        }else if(servletPath.equals("/saveJob")|| servletPath.equals("/updateJob")){
            String id = req.getParameter("id");
            System.out.println(id);
            String name = req.getParameter("name");
            String remark = req.getParameter("remark");
            //组装用户对象
            Job job = new Job();
            job.setName(name);
            job.setRemark(remark);
            job.setState(0);

            //调用保存方法
            if(id.equals("0")){
                service.addJob(job);
            }else{
                //修改
                int uid= Integer.parseInt(id);
                job.setId(uid);
                service.updateJob(job);
            }
            Map<Object, Object> map = new HashMap<>();
            map.put("code",200);
            map.put("message","success");

            //Gson包装
            Gson gson=new Gson();
            String json=gson.toJson(map);
            resp.getWriter().write(json);
        }else if(servletPath.equals("/findJobById")){
            int id = Integer.parseInt(req.getParameter("id"));
            //service
            Job job = service.findJobById(id);
            Map<String,Object> map = new HashMap<>();
            map.put("code",200);
            map.put("jobs",job);

            //Gson
            Gson gson = new Gson();
            String s = gson.toJson(map);
            resp.getWriter().write(s);
        }
    }

}
