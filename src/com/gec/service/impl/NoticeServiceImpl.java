package com.gec.service.impl;

import com.gec.bean.Notice;
import com.gec.bean.PageModel;
import com.gec.dao.NoticeDao;
import com.gec.dao.impl.NoticeDaoImpl;
import com.gec.service.NoticeService;

import java.util.List;

/**
 * @ClassName NoticeServiceImpl
 * @Description TODO
 * @Author 钟岩龙
 * @Date 2023/3/7 15:27
 * @Version 1.0
 */

public class NoticeServiceImpl implements NoticeService {

    NoticeDao dao=new NoticeDaoImpl();
    @Override
    public int getTotalNoticeCount(Notice notice) {
        return dao.getTotalNoticeCount(notice);
    }

    @Override
    public List<Notice> findNoticeByPage(Notice notice, PageModel model) {
        return dao.findNoticeByPage(notice,model);
    }

    @Override
    public void deleteNoticeById(int id) {
        dao.deleteNoticeById(id);
    }

    @Override
    public void addNotice(Notice notice) {
        dao.addNotice(notice);
    }

    @Override
    public void updateNotice(Notice notice) {
        dao.updateNotice(notice);
    }

    @Override
    public Notice findNoticeById(int id) {
        return dao.findNoticeById(id);
    }
}
