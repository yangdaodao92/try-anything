package com.jsch;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import expect4j.Closure;
import expect4j.Expect4j;
import expect4j.ExpectState;
import expect4j.matches.EofMatch;
import expect4j.matches.Match;
import expect4j.matches.RegExpMatch;
import expect4j.matches.TimeoutMatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class ShellUtil {

	private static Logger log = LoggerFactory.getLogger(Shell.class);

	private Session session;
	private ChannelShell channel;
	private static Expect4j expect = null;
	private static final long defaultTimeOut = 1000;
	private StringBuffer buffer = new StringBuffer();

	private static final int COMMAND_EXECUTION_SUCCESS_OPCODE = -2;
	private static final String BACKSLASH_R = "\r";
	public static final String BACKSLASH_N = "\n";
	public static final String COLON_CHAR = ":";
	public static String ENTER_CHARACTER = BACKSLASH_R;
	public static final int SSH_PORT = 22;

	//正则匹配，用于处理服务器返回的结果
	private static String[] linuxPromptRegEx = new String[]{"~]#", "~#", "#", ":~#", "/$", ">"};
	private static String[] errorMsg = new String[]{"could not acquire the config lock "};

	private String host;
	private int port;
	private String user;
	private String password;

	public ShellUtil(String host, int port, String user, String password) {
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
		expect = getExpect();
	}

	/**
	 * 关闭SSH远程连接
	 */
	public void disconnect() {
		if (channel != null) {
			channel.disconnect();
		}
		if (session != null) {
			session.disconnect();
		}
	}

	/**
	 * 获取服务器返回的信息
	 *
	 * @return 服务端的执行结果
	 */
	public String getResponse() {
		return buffer.toString();
	}

	/**
	 * 获得Expect4j对象，该对象可用于向SSH发送命令请求
	 */
	private Expect4j getExpect() {
		try {
			log.debug(String.format("Start logging to %s@%s:%s", user, host, port));
			JSch jsch = new JSch();
			session = jsch.getSession(user, host, port);
			session.setPassword(password);
			Hashtable<String, String> config = new Hashtable<String, String>();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			localUserInfo ui = new localUserInfo();
			session.setUserInfo(ui);
			session.connect();
			channel = (ChannelShell) session.openChannel("shell");
			Expect4j expect = new Expect4j(channel.getInputStream(), channel.getOutputStream());
			channel.connect();
			log.debug(String.format("Logging to %s@%s:%s successfully!", user, host, port));
			return expect;
		} catch (Exception ex) {
			log.error("Connect to " + host + ":" + port + "failed,please check your username and password!");
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 执行配置命令
	 *
	 * @param commands 要执行的命令，为字符数组
	 * @return 执行是否成功
	 */
	public boolean executeCommands(List<String> commands) {
		//如果expect返回为0，说明登入没有成功
		if (expect == null) {
			return false;
		}

		log.debug("----------Running commands are listed as follows:----------");
		for (String command : commands) {
			log.debug(command);
		}
		log.debug("----------End----------");

		Closure closure = expectState -> {
			buffer.append(expectState.getBuffer());
			// buffer is string
			// buffer for appending
			// output of executed
			// command
			expectState.exp_continue();
		};
		List<Match> lstPattern = new ArrayList<>();
		String[] regEx = linuxPromptRegEx;
		if (regEx != null && regEx.length > 0) {
			synchronized (regEx) {
				for (String regexElement : regEx) {
					// list of regx like, :>, />
					// etc. it is possible
					// command prompts of your
					// remote machine
					try {
						RegExpMatch mat = new RegExpMatch(regexElement, closure);
						lstPattern.add(mat);
					} catch (Exception e) {
						return false;
					}
				}
				lstPattern.add(new EofMatch(new Closure() {
					// should cause
					// entire page to be
					// collected
					public void run(ExpectState state) {
					}
				}));
				lstPattern.add(new TimeoutMatch(defaultTimeOut, new Closure() {
					public void run(ExpectState state) {
					}
				}));
			}
		}
		try {
			boolean isSuccess = true;
			for (String strCmd : commands) {
				isSuccess = isSuccess(lstPattern, strCmd);
			}
			//防止最后一个命令执行不了
			isSuccess = !checkResult(expect.expect(lstPattern));

			//找不到错误信息标示成功
			String response = buffer.toString().toLowerCase();
			for (String msg : errorMsg) {
				if (response.contains(msg)) {
					return false;
				}
			}
			return isSuccess;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	//检查执行是否成功
	private boolean isSuccess(List<Match> objPattern, String strCommandPattern) {
		try {
			boolean isFailed = checkResult(expect.expect(objPattern));
			if (!isFailed) {
				expect.send(strCommandPattern);
				expect.send(BACKSLASH_R);
				return true;
			}
			return false;
		} catch (Exception ex) {
			return false;
		}
	}

	//检查执行返回的状态
	private boolean checkResult(int intRetVal) {
		return intRetVal == COMMAND_EXECUTION_SUCCESS_OPCODE;
	}

	//登入SSH时的控制信息
	//设置不提示输入密码、不显示登入信息等
	public static class localUserInfo implements UserInfo {
		String passwd;

		public String getPassword() {
			return passwd;
		}

		public boolean promptYesNo(String str) {
			return true;
		}

		public String getPassphrase() {
			return null;
		}

		public boolean promptPassphrase(String message) {
			return true;
		}

		public boolean promptPassword(String message) {
			return true;
		}

		public void showMessage(String message) {

		}
	}

}
