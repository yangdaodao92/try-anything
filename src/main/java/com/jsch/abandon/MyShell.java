package com.jsch.abandon;

import com.jcraft.jsch.*;
import com.jsch.abandon.JSchUtil;

public class MyShell {

	public static void main(String[] args) throws JSchException {
		int port = 22;
		String host = "192.168.133.177";
		String user = "root";
		String password = "123qwe!@#";
		String psCommand = "ps -ef|grep apache-tomcat-member-center-api";

		Session session = JSchUtil.createSession(host, port, user, password);
		ChannelExec channelExec = JSchUtil.openChannel(session);

		String resTomcat = JSchUtil.exeCommand(channelExec, psCommand);
//		System.out.println(killTomcat(resTomcat, channelExec));
//		System.out.println(startTomcat(channelExec));

		channelExec.disconnect();
		session.disconnect();
	}

//	public static String killTomcat(String response, ChannelExec channelExec) {
//		System.out.println(response);
//		String ps[] = response.split("\n");
//		if (ps.length == 3) {
//			Pattern pattern = Pattern.compile("root\\s*(\\d+)\\s.*");
//			Matcher matcher = pattern.matcher(ps[0]);
//			if (matcher.find()) {
//				String tomcatPID = matcher.group(1);
//				System.out.println(tomcatPID);
//				return JSchUtil.exeCommand(channelExec, "kill -9 " + tomcatPID);
//			}
//		}
//		return "fail";
//	}

	public static String startTomcat(ChannelExec channelExec) {
		JSchUtil.exeCommand(channelExec, "cd /opt/tomcats/apache-tomcat-member-center-api/bin");
		JSchUtil.exeCommand(channelExec, "./catalina.sh start");

//		String startCommand = "bash /opt/tomcats/apache-tomcat-member-center-api/bin/catalina.sh start";
//
//		return JSchUtil.exeCommand(channelExec, startCommand);
		return null;
	}

}
