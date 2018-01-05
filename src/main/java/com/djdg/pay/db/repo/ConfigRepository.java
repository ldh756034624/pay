package com.djdg.pay.db.repo;


import com.djdg.pay.db.BaseRepository;
import com.djdg.pay.db.entity.Config;
import org.springframework.stereotype.Repository;

/**
 * @ClassName: ConfigRepository
 * @Description: Config 的查询
 * @author: shadow.liu
 * @date: 2016年6月27日 下午3:18:36
 */
@Repository
public interface ConfigRepository extends BaseRepository<Config> {

    Config findByAppId(String appId);


}
