package com.gec.service;
import com.gec.bean.Job;
import com.gec.bean.Notice;
import com.gec.bean.PageModel;

import java.util.List;

/**
 * @ClassName NoticeService
 * @Description TODO
 * @Author 钟岩龙
 * @Date 2023/3/7 15:27
 * @Version 1.0
 */

public interface NoticeService {
    int getTotalNoticeCount(Notice notice);

    List<Notice> findNoticeByPage(Notice notice, PageModel model);


    void deleteNoticeById(int id);

    void addNotice(Notice notice);

    void updateNotice(Notice notice);

    Notice findNoticeById(int id);

}
