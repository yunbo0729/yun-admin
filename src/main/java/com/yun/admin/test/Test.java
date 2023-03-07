package com.yun.admin.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class Test {
    public static void main(String[] args) {
    //public void test(){
        //ThreadLocal <CopyOnWriteArrayList<String>> threadLocal = new ThreadLocal<>();
        CopyOnWriteArrayList list = new CopyOnWriteArrayList();
        for(int i = 0; i<10000; i++){
            list.add("string" + i);
        }
        ThreadLocal<CopyOnWriteArrayList<String>> threadLocal = ThreadLocal.withInitial(() ->list);
        log.info(threadLocal.get().toString());

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (threadLocal.get().size() > 0) {
                        //String content = (String) list.get(list.size() - 1);
                        String content = (String) threadLocal.get().get(threadLocal.get().size()-1);
                        log.info("第"+threadLocal.get().size()+"---"+ content);
                    }else {
                        break;
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if(threadLocal.get().size() <= 0){
                        break;
                    }
                    threadLocal.get().remove(0);
                    log.info("删第"+threadLocal.get().size()+"---"+ threadLocal.get().get(0));
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        log.info("后"+threadLocal.get().toString());
    }
}
