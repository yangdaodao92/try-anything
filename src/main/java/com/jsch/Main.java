package com.jsch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yangnx on 2017/5/12.
 */
public class Main {

	public static void main(String[] args) {
		int port = 22;
		String host = "192.168.133.177";
		String user = "root";
		String password = "123qwe!@#";
		String psCommand = "ps -ef|grep apache-tomcat-member-center-api";
		String startCatalina = "/opt/tomcats/apache-tomcat-member-center-api/bin/catalina.sh start";

		ShellUtil shellUtil = new ShellUtil(host, port, user, password);
		shellUtil.executeCommands(psCommand);
		shellUtil.executeCommands(killTomcatCommand(shellUtil.getResponse()));
		shellUtil.executeCommands(startCatalina);

		shellUtil.disconnect();
	}

	public static String killTomcatCommand(String response) {
		Pattern pattern = Pattern.compile("\nroot\\s*(\\d+)\\s.*?/opt/tomcats/.*?apache-tomcat-member-center-api.*");
		Matcher matcher = pattern.matcher(response);
		if (matcher.find()) {
			String tomcatPID = matcher.group(1);
			return "kill -9 " + tomcatPID;
		}
		return "fail";
	}

}
