package com.jsch;


import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShellDemo1 {

	public static void main(String[] args) throws IOException, JSchException {
		int port = 22;
		String host = "192.168.133.177";
		String user = "root";
		String password = "123qwe!@#";
		String command = "ps -ef|grep apache-tomcat-member-center-api";
		String res = exeCommand(host, port, user, password, command);
		System.out.println(res);
//		String ps[] = res.split("\n");
//		if (ps.length == 3) {
//			Pattern pattern = Pattern.compile("root\\s*(\\d+)\\s.*");
//			Matcher matcher = pattern.matcher(ps[0]);
//			if (matcher.find()) {
//				String tomcatPID = matcher.group(1);
//				System.out.println(tomcatPID);
//			}
//		}
//		System.out.println(ps.length);
//
//		System.out.println(ps[0]);
	}


	public static String exeCommand(String host, int port, String user, String password, String command) throws JSchException, IOException {
		JSch jsch = new JSch();
		Session session = jsch.getSession(user, host, port);
		session.setConfig("StrictHostKeyChecking", "no");
		//    java.util.Properties config = new java.util.Properties();
		//   config.put("StrictHostKeyChecking", "no");

		session.setPassword(password);
		session.connect();

		ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
		InputStream in = channelExec.getInputStream();
		channelExec.setCommand(command);
		channelExec.setErrStream(System.err);
		channelExec.connect();
		String out = IOUtils.toString(in, "UTF-8");

		channelExec.disconnect();
		session.disconnect();
		return out;
	}

}
