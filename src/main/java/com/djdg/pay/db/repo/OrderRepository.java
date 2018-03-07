package com.djdg.pay.db.repo;


import com.djdg.pay.db.BaseRepository;
import com.djdg.pay.db.entity.Order;
import org.springframework.stereotype.Repository;

/**
 * @ClassName: OrderRepository
 * @Description: Order 的查询
 * @author: shadow.liu
 * @date: 2016年6月27日 下午3:18:36
 */
@Repository
public interface OrderRepository extends BaseRepository<Order> {

    Order findByOrderNo(String orderNo);

    Order findFirstByBusinessAppIdAndBusinessOrderIdOrderByCreateTimeDesc(String businessAppId, String businessOrderId);

    Order findByTransactionId(String id);
}
