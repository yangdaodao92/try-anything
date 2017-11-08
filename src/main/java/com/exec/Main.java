package com.exec;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		System.out.println(ExecUtil.executeCommands("ipconfig"));
	}

}
