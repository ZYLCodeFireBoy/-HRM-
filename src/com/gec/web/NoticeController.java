package com.gec.web;

import com.gec.bean.Notice;
import com.gec.bean.PageModel;

import com.gec.bean.Type;
import com.gec.bean.User;
import com.gec.service.NoticeService;
import com.gec.service.impl.NoticeServiceImpl;
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
 * @ClassName NoticeController
 * @Description TODO
 * @Author 钟岩龙
 * @Date 2023/3/7 15:25
 * @Version 1.0
 */
@WebServlet(urlPatterns = {"/findNoticeByPage", "/saveNotice",
        "/deleteNotice","/updateNotice","/findNoticeById"})
public class NoticeController extends HttpServlet {

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
        NoticeService service=new NoticeServiceImpl();

        if(servletPath.equals("/findNoticeByPage")){

            //获得当前页码
            String pageIndex = req.getParameter("pageIndex");
            int page = !"".equals(pageIndex) && pageIndex != null ? Integer.parseInt(pageIndex) : 1;
            //设置到PageModel
            PageModel model = new PageModel();
            model.setPageIndex(page);
            //查询用户列表
            List<Notice> notices = null;
            //提供部门名称查询
            String Nname = req.getParameter("Nname");
            Notice notice = new Notice();
            notice.setName(Nname);
            //总记录数
            int count=service.getTotalNoticeCount(notice);
            model.setTotalRecoreSum(count);
            notices=service.findNoticeByPage(notice,model);
            Map<String,Object> map=new HashMap<>();
            map.put("model",model);
            map.put("notices",notices);
            //使用json返回
            Gson gson =new Gson();
            String s = gson.toJson(map);
            //响应前台
            resp.getWriter().write(s);
        }else if(servletPath.equals("/deleteNotice")){
            int id = Integer.parseInt(req.getParameter("id"));
            //service
            service.deleteNoticeById(id);
            Map<String,Object> map = new HashMap<>();
            map.put("code",200);
            map.put("message","delete success");
            //Gson
            Gson gson = new Gson();
            String s = gson.toJson(map);
            resp.getWriter().write(s);
        }else if(servletPath.equals("/saveNotice")|| servletPath.equals("/updateNotice")){
            String id = req.getParameter("id");
            System.out.println(id);
            String name = req.getParameter("name");
            String content = req.getParameter("content");
            String userId = req.getParameter("userId");
            String typeId = req.getParameter("typeId");


            //组装用户对象
            Notice notice = new Notice();
            User user = new User();
            Type type = new Type();
            notice.setCreateDate(new Date());
            notice.setName(name);
            type.setId(Integer.parseInt(typeId));
            user.setId(Integer.parseInt(userId));
            notice.setUser(user);
            notice.setType(type);
            notice.setContent(content);

            //调用保存方法
            if(id.equals("0")){
                service.addNotice(notice);
            }else{
                //修改
                int uid= Integer.parseInt(id);
                notice.setId(uid);
                service.updateNotice(notice);
            }
            Map<Object, Object> map = new HashMap<>();
            map.put("code",200);
            map.put("message","success");

            //Gson包装
            Gson gson=new Gson();
            String json=gson.toJson(map);
            resp.getWriter().write(json);
        }else if(servletPath.equals("/findNoticeById")){
            int id = Integer.parseInt(req.getParameter("id"));
            //service
            Notice notice = service.findNoticeById(id);
            Map<String,Object> map = new HashMap<>();
            map.put("code",200);
            map.put("notices",notice);

            //Gson
            Gson gson = new Gson();
            String s = gson.toJson(map);
            resp.getWriter().write(s);
        }
    }

}
