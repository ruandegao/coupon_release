package com.shangan.trade.coupon.db.mappers;

import com.shangan.trade.coupon.db.model.Task;

import java.util.List;

public interface TaskMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Task record);

    int insertSelective(Task record);

    Task selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Task record);

    int updateByPrimaryKey(Task record);

    int updateByBiz(Task record);

    List<Task> queryFailedTasks();

    List<Task> queryRemindTasks();
}