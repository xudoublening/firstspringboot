package com.example.firstspringboot.controller;

import com.example.firstspringboot.dto.Ball;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/random")
public class BigHappyPrizeController {

    private static final Log log = LogFactory.getLog(BigHappyPrizeController.class);
    @RequestMapping("/randomgroup")
    public String getRandomGroupNumber(){

        String num = getFrontArea5(getLatelyF100(),35,5);
        num += getFrontArea5(getLatelyB100(),12,2);

        return num;
    }

    /**
     * 获取前区5个数(基于最近100期)
     * @param balls 前区球集合
     * @return
     */
    private String getFrontArea5(List<Ball> balls,int sum,int g){
        String number = "";
        Random ran = new Random();
        int r ;
        int qs = 100;
        int allYL = 0;
        int maxC = 0;
        double allAverageOmissionTime;
        double maxCP = 0;
        double allAverageLC = 0;
        for (int i = 0;i<balls.size();i++){
            Ball b = balls.get(i);
            double addUpProbability = operation(b.getAddUpTime(),100*g);//总出现概率
            log.info("取号["+b.getNumber()+"]==总出现几率:"+addUpProbability);
            balls.get(i).setProbability(addUpProbability);
            allYL += b.getMaxOmissionTime();
            allAverageLC += b.getMaxContinuityTime();
            if (b.getMaxContinuityTime()>maxC){
                maxC = b.getMaxContinuityTime();
            }
            if (i == 0){
                maxCP = b.getProbability();
            }else{
                if (compare(maxCP,b.getProbability())){
                    maxCP = b.getProbability();
                }
            }
        }

        allAverageOmissionTime = allYL/sum;//前区所有平均遗漏
        allAverageLC = allAverageLC/sum;//平均连出
        log.info("全号平均遗漏:"+allAverageOmissionTime+";最大连出:"+maxC);

        double allP = g/sum;
        while(balls.size() > (sum-g)){

            for (int j = 0; j<balls.size();j++){
                r = ran.nextInt(5)+1;
                log.info(j+"-随机数:"+r);
                Ball b1 = balls.get(j);
                if (r == 2 || r == 4){//取低概率出现的数    如何判断概率足够低?

                    double rd1 = ran.nextDouble();
                    if (compare(rd1,operationParaD(b1.getProbability()*allP,r))){
                        number += b1.getNumber()+", ";
                        balls.remove(j);
                        log.info("低概率直接取出--本次取出号码:"+b1.getNumber());
                        break;
                    }
                    if (compare(b1.getProbability(),allP)){//出现概率小于平均概率
                        if (compare(operation(b1.getMaxOmissionTime(),r),allAverageOmissionTime)||
                                b1.getMaxOmissionTime()==(int)allAverageOmissionTime){//最大遗漏小于平均遗漏
                            double rd = ran.nextDouble();
                            if (compare(rd,b1.getProbability())||compare(rd,maxCP)){
                                number += b1.getNumber()+", ";
                                balls.remove(j);
                                log.info("L-本次取出号码:"+b1.getNumber());
                                break;
                            }
                        }
                    }
                }else{//取高概率出现的数
                    double rd1 = ran.nextDouble();
                    if (compare(rd1,operationParaD(b1.getProbability()*allP,r))){
                        number += b1.getNumber()+", ";
                        balls.remove(j);
                        log.info("高概率直接取出--本次取出号码:"+b1.getNumber());
                        break;
                    }
                    if (!compare(b1.getProbability(),allP)){//出现概率小于平均概率
                        if (compare((double)b1.getMaxOmissionTime(),allAverageOmissionTime)){//最大遗漏大于平均遗漏
                            if (!(compare((double)b1.getMaxContinuityTime(),allAverageLC)||
                                    b1.getMaxContinuityTime()==(int)allAverageLC)){//最大连出大于平均连出
                                double rd = ran.nextDouble();
                                if (compare(rd,b1.getProbability())){
                                    number += b1.getNumber()+", ";
                                    balls.remove(j);
                                    log.info("H-本次取出号码:"+b1.getNumber());
                                    break;
                                }
                                //一半的概率 直接最大连号 出
                                else if (compare(0.5,rd)&&compare((double)b1.getMaxContinuityTime(),(double)maxC)){
                                    number += b1.getNumber()+", ";
                                    balls.remove(j);
                                    log.info("H-本次取出号码:"+b1.getNumber());
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        return number;
    }

    /**
     * double 类型比大小
     * @param val1 比较的第一个数
     * @param val2  比较的第二个数
     * @return  val1 是否比 val2 小  是true 否false
     */
    private Boolean compare(Double val1,Double val2){
        BigDecimal v1 = new BigDecimal(val1);
        BigDecimal v2 = new BigDecimal(val2);
        if (v1.compareTo(v2) < 0){
            return true;
        }
        return false;
    }

    /**
     * 运算-除法
     * @param val1  除数
     * @param val2  被除数
     * @return
     */
    private Double operation(int val1,int val2){
        BigDecimal v1 = new BigDecimal(val1);
        BigDecimal v2 = new BigDecimal(val2);

        return v1.divide(v2,7,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private Double operationParaD(double val1,double val2){
        BigDecimal v1 = new BigDecimal(val1);
        BigDecimal v2 = new BigDecimal(val2);

        return v1.divide(v2,7,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static void main(String[] args) {
        BigHappyPrizeController b = new BigHappyPrizeController();
        double d1 = 0.001;
        double d2 = 0.02;
        int i1 = 10;
        int i2 = 15;
        System.out.println(b.compare(d1,d2)+"d1*d2="+d1*d2/2+"----"+b.operation(i1,i2));
        System.out.println("d1*i1="+d1*i1);
    }

    private List<Ball> getLatelyF100(){
        List<Ball> frontArea = new ArrayList<>();
        Ball ball1 = new Ball(1,5,1,10,4);
        Ball ball2 = new Ball(2,1,1,32,16);
        Ball ball3 = new Ball(3,6,1,14,4);
        Ball ball4 = new Ball(4,5,2,8,4);
        Ball ball5 = new Ball(5,2,1,15,9);
        Ball ball6 = new Ball(6,5,1,16,4);
        Ball ball7 = new Ball(7,7,1,7,3);
        Ball ball8 = new Ball(8,5,1,8,5);
        Ball ball9 = new Ball(9,4,2,21,5);
        Ball ball10 = new Ball(10,3,1,14,9);
        Ball ball11 = new Ball(11,7,3,7,2);
        Ball ball12 = new Ball(12,6,1,9,4);
        Ball ball13 = new Ball(13,2,1,19,10);
        Ball ball14 = new Ball(14,4,1,14,5);
        Ball ball15 = new Ball(15,3,1,24,11);
        Ball ball16 = new Ball(16,5,1,10,4);
        Ball ball17 = new Ball(17,1,1,27,16);
        Ball ball18 = new Ball(18,3,2,14,7);
        Ball ball19 = new Ball(19,6,4,13,4);
        Ball ball20 = new Ball(20,4,2,18,8);
        Ball ball21 = new Ball(21,8,3,13,2);
        Ball ball22 = new Ball(22,5,1,8,4);
        Ball ball23 = new Ball(23,5,2,22,5);
        Ball ball24 = new Ball(24,4,1,29,7);
        Ball ball25 = new Ball(25,2,1,19,9);
        Ball ball26 = new Ball(26,3,1,13,8);
        Ball ball27 = new Ball(27,4,1,14,5);
        Ball ball28 = new Ball(28,3,2,23,8);
        Ball ball29 = new Ball(29,6,1,12,3);
        Ball ball30 = new Ball(30,3,3,27,6);
        Ball ball31 = new Ball(31,4,2,11,6);
        Ball ball32 = new Ball(32,2,1,18,12);
        Ball ball33 = new Ball(33,9,4,14,2);
        Ball ball34 = new Ball(34,4,1,9,5);
        Ball ball35 = new Ball(35,4,1,10,5);

        frontArea.add(ball1);
        frontArea.add(ball2);
        frontArea.add(ball3);
        frontArea.add(ball4);
        frontArea.add(ball5);
        frontArea.add(ball6);
        frontArea.add(ball7);
        frontArea.add(ball8);
        frontArea.add(ball9);
        frontArea.add(ball10);
        frontArea.add(ball11);
        frontArea.add(ball12);
        frontArea.add(ball13);
        frontArea.add(ball14);
        frontArea.add(ball15);
        frontArea.add(ball16);
        frontArea.add(ball17);
        frontArea.add(ball18);
        frontArea.add(ball19);
        frontArea.add(ball20);
        frontArea.add(ball21);
        frontArea.add(ball22);
        frontArea.add(ball23);
        frontArea.add(ball24);
        frontArea.add(ball25);
        frontArea.add(ball26);
        frontArea.add(ball27);
        frontArea.add(ball28);
        frontArea.add(ball29);
        frontArea.add(ball30);
        frontArea.add(ball31);
        frontArea.add(ball32);
        frontArea.add(ball33);
        frontArea.add(ball34);
        frontArea.add(ball35);

        System.out.println(frontArea.size());

        return frontArea;
    }
    private List<Ball> getLatelyB100(){
        List<Ball> backArea = new ArrayList<>();


        Ball ball1 = new Ball(1,6,2,10,9);
        Ball ball2 = new Ball(2,7,3,32,8);
        Ball ball3 = new Ball(3,4,2,14,11);
        Ball ball4 = new Ball(4,6,2,8,9);
        Ball ball5 = new Ball(5,4,1,15,11);
        Ball ball6 = new Ball(6,7,2,16,6);
        Ball ball7 = new Ball(7,1,1,7,27);
        Ball ball8 = new Ball(8,4,2,8,12);
        Ball ball9 = new Ball(9,4,1,21,10);
        Ball ball10 = new Ball(10,5,1,14,8);
        Ball ball11 = new Ball(11,8,2,7,13);
        Ball ball12 = new Ball(12,4,2,9,14);

        backArea.add(ball1);
        backArea.add(ball2);
        backArea.add(ball3);
        backArea.add(ball4);
        backArea.add(ball5);
        backArea.add(ball6);
        backArea.add(ball7);
        backArea.add(ball8);
        backArea.add(ball9);
        backArea.add(ball10);
        backArea.add(ball11);
        backArea.add(ball12);

        System.out.println(backArea.size());

        return backArea;
    }
}
