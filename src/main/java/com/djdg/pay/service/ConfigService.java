package com.djdg.pay.service;

import com.djdg.pay.common.Result;
import com.djdg.pay.db.entity.Config;
import com.djdg.pay.db.repo.ConfigRepository;
import com.djdg.pay.model.dto.ConfigDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * Description:TODO
 * ConfigService:刘敏华 shadow.liu@hey900.com
 * Date: 2018/1/5
 * Time: 16:04
 */
@Component
public class ConfigService {
    
    @Resource
    private ConfigRepository configRepository;

    public Result register(ConfigDto configDto){
        Config config = configRepository.findByBusinessAppId(configDto.getBusinessAppId());
        if (config == null) {
            config = new Config();
            BeanUtils.copyProperties(configDto,config);
            config = configRepository.saveAndFlush(config);
        }else{
            BeanUtils.copyProperties(configDto,config,"id");
            config = configRepository.saveAndFlush(config);
        }
        PayService.clearConfig(configDto.getBusinessAppId());
        return Result.success(config);
    }


    
    
}
