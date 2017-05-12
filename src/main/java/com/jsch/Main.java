package com.jsch;

import java.util.ArrayList;
import java.util.List;

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

//		String locateTomcat = "cd /opt/tomcats/apache-tomcat-member-center-api/bin";
//		String startCatalina = "./catalina.sh start";

		ShellUtil shellUtil = new ShellUtil(host, port, user, password);
		List<String> commands = new ArrayList<>();
		commands.add(psCommand);
		shellUtil.executeCommands(commands);
		System.out.println(shellUtil.getResponse());
		shellUtil.disconnect();
	}

}
