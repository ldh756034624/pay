package com.djdg.pay.db.entity;

import com.djdg.pay.db.BaseEntity;
import lombok.Data;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created with IntelliJ IDEA.
 * Description:TODO
 * RefundOrder:刘敏华 shadow.liu@hey900.com
 * Date: 2018/1/18
 * Time: 15:47
 */
@Data
@Entity
@Table(name = "refundOrder")
public class RefundOrder extends BaseEntity {


    @Id
    @SequenceGenerator(name = "paySeq", sequenceName = "pay_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = IDENTITY, generator = "paySeq")
    private Long id;

    












}
