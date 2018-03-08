package com.djdg.pay.db.repo;


import com.djdg.pay.db.BaseRepository;
import com.djdg.pay.db.entity.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

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

    @Query("select o from Order o where o.businessAppId =?1 and o.businessOrderId in ?2")
    List<Order> findbyPayInfoIds(String bid, Collection ids);
}
