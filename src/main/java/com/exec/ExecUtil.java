package com.exec;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ExecUtil {

	public static String executeCommands(String ...commands) {
		DefaultExecutor executor = new DefaultExecutor();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
		PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream,errorStream);
		executor.setStreamHandler(streamHandler);
		try {
			for (String command : commands) {
				executor.execute(new CommandLine(command));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			return outputStream.toString("gbk") + errorStream.toString("gbk");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "error:UnsupportedEncodingException";
		}
	}

}
