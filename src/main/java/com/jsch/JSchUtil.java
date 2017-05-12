package com.jsch;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * 连接ssh工具类
 */
public class JSchUtil {

	public static Session createSession(String host, int port, String user, String password) {
		try {
			JSch jsch = new JSch();
			Session session = jsch.getSession(user, host, port);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(password);
			session.connect();
			return session;
		} catch (JSchException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ChannelExec openChannel(Session session) {
		try {
			return (ChannelExec) session.openChannel("exec");
		} catch (JSchException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ChannelExec createSessionAndOpenChannel(String host, int port, String user, String password) {
		Session session = createSession(host, port, user, password);
		return openChannel(session);
	}

	public static String exeCommand(ChannelExec channelExec, String command) {
		try {
			InputStream in = channelExec.getInputStream();
			channelExec.setCommand(command);
			channelExec.setErrStream(System.err);
			channelExec.connect();
			return IOUtils.toString(in, "UTF-8");
		} catch (IOException | JSchException e) {
			e.printStackTrace();
		}
		return null;
	}

}
