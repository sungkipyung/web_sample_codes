package com.simple.web.devpia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DevpiaServiceTest {
	DevpiaService service;

	@Before
	public void setUp() {
		service = new DevpiaService();
	}

	@After
	public void tearDown() {
		service.destroy();
	}

	@Test
	public void testSendMessage() throws Exception {
		try {
			String receiver = "sungkipyung";
			String messsage = "hello\r\nworld4";

			Header[] cookie = service.loginDevpia("sungkipyung", "tjfla0702");
			String result = service.sendMessageWithNewLine(messsage, cookie, "sungkipyung", receiver);
			System.out.println(result);
		} finally {
			service.destroy();
		}
		// txtMessage
	}

	private String readFile(String fileName) throws FileNotFoundException,
			IOException {
		File file = new File(fileName);
		BufferedReader in = new BufferedReader(new FileReader(file));
		StringBuilder builder = new StringBuilder();
		while (true) {
			String line = in.readLine();
			if (line == null)
				break;
			builder.append(line).append("\n");
		}
		String result = builder.toString();
		IOUtils.closeQuietly(in);
		return result;
	}
}
