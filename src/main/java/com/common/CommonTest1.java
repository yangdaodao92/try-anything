package com.common;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author yangnx
 * @date 2018/5/22
 */
public class CommonTest1 {

	public static void main(String[] args) {
		CommonTest1 commonTest1 = new CommonTest1();
		ExecutorService executorService = Executors.newCachedThreadPool();
//		ExecutorService executorService2 = new ScheduledThreadPoolExecutor(1, new BasicThreadFactory.Builder().namingPattern("").daemon(true).build());
//		executorService2.submit(() -> commonTest1.test00("1"));
//		executorService2.submit(() -> commonTest1.test00("2"));
//		executorService2.

	}

	public synchronized void test00(String name) {
		for (int i = 0; i < 100; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(name + i);
		}
	}

}
