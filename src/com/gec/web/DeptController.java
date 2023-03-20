package com.gec.web;

import com.gec.bean.Dept;
import com.gec.bean.PageModel;
import com.gec.bean.User;
import com.gec.service.DeptService;
import com.gec.service.UserService;
import com.gec.service.impl.DeptServiceImpl;
import com.gec.service.impl.UserServiceImpl;
import com.gec.util.CrossRegion;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName DepartmentController
 * @Description TODO
 * @Author 钟岩龙
 * @Date 2023/3/2 9:47
 * @Version 1.0
 */
@WebServlet(urlPatterns = {"/findDeptByPage", "/saveDept",
        "/deleteDept","/updateDept","/findDeptById"})
public class DeptController extends HttpServlet {

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
        DeptService service=new DeptServiceImpl();
        if(servletPath.equals("/findDeptByPage")){
            //获得当前页码
            String pageIndex = req.getParameter("pageIndex");
            int page = !"".equals(pageIndex) && pageIndex != null ? Integer.parseInt(pageIndex) : 1;
            //设置到PageModel
            PageModel model = new PageModel();
            model.setPageIndex(page);
            //查询用户列表
            List<Dept> depts = null;
            //提供部门名称查询
            String dname = req.getParameter("Dname");
            Dept dept = new Dept();
            dept.setName(dname);
            //总记录数
            int count=service.getTotalDeptCount(dept);
            model.setTotalRecoreSum(count);
            depts=service.findDeptsByPage(dept,model);
            Map<String,Object>map=new HashMap<>();
            map.put("model",model);
            map.put("depts",depts);
            //使用json返回
            Gson gson =new Gson();
            String s = gson.toJson(map);
            //响应前台
            resp.getWriter().write(s);
        }else if(servletPath.equals("/deleteDept")){
            int id = Integer.parseInt(req.getParameter("id"));
            //service
            service.deleteDeptById(id);
            Map<String,Object> map = new HashMap<>();
            map.put("code",200);
            map.put("message","delete success");
            //Gson
            Gson gson = new Gson();
            String s = gson.toJson(map);
            resp.getWriter().write(s);
        }else if(servletPath.equals("/saveDept")|| servletPath.equals("/updateDept")) {
            String id = req.getParameter("id");
            System.out.println(id);
            String name = req.getParameter("name");
            String remark = req.getParameter("remark");
            //组装用户对象
            Dept dept = new Dept();
            dept.setName(name);
            dept.setRemark(remark);
            dept.setStatus(0);

            //调用保存方法
            if (id.equals("0")) {
                service.addDept(dept);
            } else {
                //修改
                int uid = Integer.parseInt(id);
                dept.setId(uid);
                service.updateDept(dept);
            }
            Map<Object, Object> map = new HashMap<>();
            map.put("code", 200);
            map.put("message", "success");
            //Gson包装
            Gson gson = new Gson();
            String json = gson.toJson(map);
            resp.getWriter().write(json);
        }else if(servletPath.equals("/findDeptById")){
            int id = Integer.parseInt(req.getParameter("id"));
            //service
            Dept dept = service.findDeptById(id);
            Map<String,Object> map = new HashMap<>();
            map.put("code",200);
            map.put("depts",dept);
            //Gson
            Gson gson = new Gson();
            String s = gson.toJson(map);
            resp.getWriter().write(s);
        }

    }
}
