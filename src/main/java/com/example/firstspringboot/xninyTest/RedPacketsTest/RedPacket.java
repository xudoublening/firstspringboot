package com.example.firstspringboot.xninyTest.RedPacketsTest;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Xnn on 2020/1/16 18:06
 */
public class RedPacket {
    /**
     * 总金额
     */
    private BigDecimal totalAmount;
    /**
     * 份数
     */
    private Integer number;
    /**
     * 类型
     */
    private String type;
    
    /**
     * 余额
     */
    private BigDecimal balance;
    /**
     * 信息列表
     */
    private Map<String,BigDecimal> infoList;
    
    public RedPacket(BigDecimal totalAmount, Integer number, String type) {
        
        this.totalAmount = totalAmount;
        this.number = number;
        this.type = type;
        this.balance = totalAmount;
        this.infoList = new HashMap<>();
    }
    
    public synchronized BigDecimal grabRedPacket(String name){
        BigDecimal grabAmount = null;
        if (this.infoList.size() < this.number){
            if ("1".equals(this.type)){ //  普通,平分
                grabAmount =  this.totalAmount.divide(BigDecimal.valueOf(this.number),BigDecimal.ROUND_HALF_UP);
            }else if ("2".equals(this.type)){   //  拼手气
                if (this.infoList.size() + 1 == this.number){
                    grabAmount = this.balance;
                }else{
                    BigDecimal maxAmount = this.balance.divide(BigDecimal.valueOf(this.number - this.infoList.size())
                            .divide(new BigDecimal(2),BigDecimal.ROUND_HALF_UP),BigDecimal.ROUND_HALF_UP);
                    grabAmount = maxAmount.multiply(BigDecimal.valueOf(Math.random())).setScale(2,BigDecimal.ROUND_HALF_UP);
                    //  保证剩余金额能满足剩余数量最小金额 0.01
                    // BigDecimal minAmount = BigDecimal.valueOf(0.01).
                    //         multiply(BigDecimal.valueOf(this.infoList.size()-1));
                }
            }
        }else{
            throw new RuntimeException("来慢了,已经被抢完了!");
        }
        this.infoList.put(name,grabAmount);
        this.balance = this.balance.subtract(grabAmount);
        return grabAmount;
    }
    
    public String selectInfoList() {
        
        return "RedPacket{" + "infoList=" + infoList + '}';
    }
    
    public BigDecimal getTotalAmount() { return totalAmount; }
    
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    
    public Integer getNumber() { return number; }
    
    public void setNumber(Integer number) { this.number = number; }
    
    public String getType() { return type; }
    
    public void setType(String type) { this.type = type; }
    
    public BigDecimal getBalance() { return balance; }
    
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    
    public Map<String, BigDecimal> getInfoList() { return infoList; }
    
    public void setInfoList(Map<String, BigDecimal> infoList) { this.infoList = infoList; }
    
}
