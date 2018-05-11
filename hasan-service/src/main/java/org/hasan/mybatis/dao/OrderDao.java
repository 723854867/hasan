package org.hasan.mybatis.dao;

import java.util.List;

import org.gatlin.dao.mybatis.DBDao;
import org.hasan.bean.entity.Order;
import org.hasan.bean.param.AssistantOrdersParam;

public interface OrderDao extends DBDao<String, Order> {

	List<Order> assistantList(AssistantOrdersParam param);
}
