package com.gec.dao;

import com.gec.bean.Notice;
import com.gec.bean.PageModel;

import java.util.List;

/**
 * @ClassName NoticeDao
 * @Description TODO
 * @Author 钟岩龙
 * @Date 2023/3/7 15:33
 * @Version 1.0
 */

public interface NoticeDao {
    int getTotalNoticeCount(Notice notice);

    List<Notice> findNoticeByPage(Notice notice, PageModel model);


    void deleteNoticeById(int id);

    void addNotice(Notice notice);

    void updateNotice(Notice notice);

    Notice findNoticeById(int id);
}
