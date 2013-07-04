package swp_compiler_ss13.crosstest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;

public class JavabyteExecutor {
	private final static boolean NO_VERIFY_NEEDED = false;
	private final static String NO_VERIFY = "-noverify";

	Process p;

	public JavabyteExecutor(File classFile) {
		if (classFile == null)
			throw new NullPointerException();

		if (!classFile.exists())
			throw new JavabyteExecutorRuntimeException(
					"JavabyteExecutor can not be started. File does not exists.");

		if (classFile.isDirectory())
			throw new JavabyteExecutorRuntimeException(
					"JavabyteExecutor can not be started. Need a file not a directory.");

		if (!classFile.getName().endsWith(".class"))
			throw new JavabyteExecutorRuntimeException(
					"JavabyteExecutor can not be started. File has not expected extension.");

		String classPath = classFile.getParent();
		String className = classFile.getName();
		className = className.substring(0, className.lastIndexOf('.'));

		String javaExecutablePath = System.getProperty("java.home")
				+ File.separator + "bin" + File.separator + "java";
		ProcessBuilder processBuilder;
		if (NO_VERIFY_NEEDED) {
			processBuilder = new ProcessBuilder(javaExecutablePath, "-cp",
					classPath, NO_VERIFY, className);
		} else {
			processBuilder = new ProcessBuilder(javaExecutablePath, "-cp",
					classPath, className);
		}
		try {
			p = processBuilder.redirectErrorStream(true).start();
			p.waitFor();
		} catch (IOException | InterruptedException e) {
			throw new JavabyteExecutorRuntimeException(e.getMessage(), e);
		}
	}

	public Integer getReturnValue() {
		return p.exitValue();
	}

	public String getProcessOutput() {
		StringWriter writer = new StringWriter();
		try {
			IOUtils.copy(p.getInputStream(), writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return writer.toString();
	}

	public InputStream getInputstream() {
		return p.getInputStream();
	}
}
