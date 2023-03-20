package com.gec.web;

import com.gec.bean.Dept;
import com.gec.bean.Document;
import com.gec.bean.PageModel;
import com.gec.bean.User;

import com.gec.service.DocService;

import com.gec.service.impl.DocServiceImpl;
import com.gec.util.CrossRegion;
import com.google.gson.Gson;

import javax.print.Doc;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.*;

/**
 * @ClassName DocController
 * @Description TODO
 * @Author 钟岩龙
 * @Date 2023/3/4 17:02
 * @Version 1.0
 */


@MultipartConfig
@WebServlet(urlPatterns = {"/upload","/download","/findDocsByPage","/findDocById","/updateDoc","/deleteDoc"})
public class DocController extends HttpServlet {
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
        DocService service=new DocServiceImpl();
        //请求的servlet 路径
        String servletPath = req.getServletPath();
            if(servletPath.equals("/upload")){
            //获取前台请求的所有文件(包含普通字段在内)
            Collection<Part> parts = req.getParts();
            System.out.println(parts.size());
            for (Part p : parts) {
                //普通字段封装为part对象数据为null
                if (p.getSubmittedFileName() != null) {
                    //设置请求编码
                    req.setCharacterEncoding("utf-8");
                    resp.setCharacterEncoding("utf-8");
                    InputStream inputStream = p.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(inputStream);
                    File file = new File("C://Program Files//Java//Java-idea-projects//第二阶段HRM人事管理系统//src//com//gec//File//upload", p.getSubmittedFileName());
                    FileOutputStream fos = new FileOutputStream(file);
                    byte[] b = new byte[1024000];
                    int len = 0;
                    while ((len = bis.read(b)) != -1) {
                        fos.write(b, 0, len);
                    }
                    Document document = new Document();
                    document.setTitle(req.getParameter("title"));
                    document.setFileName(p.getSubmittedFileName());
                    document.setFiletype(p.getContentType());
                    document.setFileBytes(b);
                    document.setRemark(req.getParameter("remark"));
                    document.setCreateDate(new Date());
                    User user = new User();
                    user.setId(Integer.parseInt(req.getParameter("userId")));
                    document.setUser(user);
                    service.addDoc(document);
                    Map<String,Object> map = new HashMap<>();
                    map.put("code",200);
                    map.put("message","upload success");
                    //Gson
                    Gson gson = new Gson();
                    String s = gson.toJson(map);
                    resp.getWriter().write(s);
                    bis.close();
                    fos.flush();
                    fos.close();
                }
            }
        }else if(servletPath.equals("/download")){
            //设置请求编码
            req.setCharacterEncoding("utf-8");
            resp.setCharacterEncoding("utf-8");
            Document document = service.findDocById(Integer.parseInt(req.getParameter("id")));

                byte[] filebytes=document.getFileBytes();

            //2.设置响应头
            resp.setHeader("content-disposition","attachment;filename=" + document.getFileName());
            //3.下载文件

            File file = new File("C://Program Files//Java//Java-idea-projects//第二阶段HRM人事管理系统//src//com//gec//File//download");
            try (ByteArrayInputStream bis = new ByteArrayInputStream(filebytes);
                 OutputStream os = resp.getOutputStream();
            ) {
                int len = 0;
                byte[] b = new byte[1024000];
                while ((len = bis.read(b)) > 0) {
                    os.write(b, 0, len);
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            Map<String,Object>map=new HashMap<>();
            map.put("document",document);
            //使用json返回
            Gson gson =new Gson();
            String s = gson.toJson(map);
            //响应前台
            resp.getWriter().write(s);
        }
        else if(servletPath.equals("/findDocsByPage")){
                //获得当前页码
                String pageIndex = req.getParameter("pageIndex");
                int page = !"".equals(pageIndex) && pageIndex != null ? Integer.parseInt(pageIndex) : 1;
                //设置到PageModel
                PageModel model = new PageModel();
                model.setPageIndex(page);
                //查询用户列表
                List<Document> documents = null;
                List<Dept> depts = null;
                //提供部门名称查询
                String title = req.getParameter("title");
                Document document = new Document();
                document.setTitle(title);
                //总记录数
                int count=service.getTotalDocCount(document);
                model.setTotalRecoreSum(count);
                documents=service.findDocsByPage(document,model);

                Map<String,Object>map=new HashMap<>();
                map.put("model",model);
                map.put("docs",documents);
                //使用json返回
                Gson gson =new Gson();
                String s = gson.toJson(map);
                //响应前台
                resp.getWriter().write(s);
        }else if(servletPath.equals("/findDocById")){
                int id = Integer.parseInt(req.getParameter("id"));
                //service
                Document document = service.findDocById(id);
                Map<String,Object> map = new HashMap<>();
                map.put("code",200);
                map.put("docs",document);
                //Gson
                Gson gson = new Gson();
                String s = gson.toJson(map);
                resp.getWriter().write(s);
        }else if(servletPath.equals("/updateDoc")){
                String id = req.getParameter("id");
                System.out.println(id);
                String title = req.getParameter("title");
                String remark = req.getParameter("remark");
                //组装用户对象
                Document document = new Document();
                document.setTitle(title);
                document.setRemark(remark);
                //修改
                int uid = Integer.parseInt(id);
                document.setId(uid);
                service.updateDoc(document);
                Map<Object, Object> map = new HashMap<>();
                map.put("code", 200);
                map.put("message", "update success");
                //Gson包装
                Gson gson = new Gson();
                String json = gson.toJson(map);
                resp.getWriter().write(json);
        }else if(servletPath.equals("/deleteDoc")){
            int id = Integer.parseInt(req.getParameter("id"));
            //service
            service.deleteDocById(id);
            Map<String,Object> map = new HashMap<>();
            map.put("code",200);
            map.put("message","delete success");
            //Gson
            Gson gson = new Gson();
            String s = gson.toJson(map);
            resp.getWriter().write(s);
        }
    }
}























