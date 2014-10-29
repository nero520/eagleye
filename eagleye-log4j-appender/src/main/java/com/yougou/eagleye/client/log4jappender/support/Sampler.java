package com.yougou.eagleye.client.log4jappender.support;


import java.util.concurrent.atomic.AtomicLong;

/**
 * 采样率实现
 * 每秒采集50为上线，过了50按百分之10%采集
  */
public class Sampler{
    private AtomicLong count = new AtomicLong();
    private int baseNumber = 50;
    private Long lastTime = -1L;

    public Sampler(){
        lastTime = System.currentTimeMillis();
    }

    public boolean isSample(){
       boolean isSample = true;
       long n = count.incrementAndGet();
       if(System.currentTimeMillis() - lastTime  < 1000){
           if(n > baseNumber){
               n = n%10;
               if(n != 0){
                   isSample = false;
               }
           }
       }else{
           count.getAndSet(0);
           lastTime = System.currentTimeMillis();//
       }
       return isSample;
    }
}
