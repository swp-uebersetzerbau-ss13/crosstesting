package swp_compiler_ss13.crosstest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;

/**
 * Class for executing javabite code.
 */
public class JavabyteExecutor {
	private final static boolean NO_VERIFY_NEEDED = false;
	private final static String NO_VERIFY = "-noverify";

	Process p;

	/**
	 * Initialize a new JavabyteExecutor objekt.
	 * @param classFile the classfile or directory of classfiles with javabytecode
	 */
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
			processBuilder = new ProcessBuilder(javaExecutablePath, "-Dfile.encoding=UTF-8", "-cp",
					classPath, NO_VERIFY, className);
		} else {
			processBuilder = new ProcessBuilder(javaExecutablePath, "-Dfile.encoding=UTF-8", "-cp",
					classPath, className);
		}
		try {
			p = processBuilder.redirectErrorStream(true).start();
			p.waitFor();
		} catch (IOException | InterruptedException e) {
			throw new JavabyteExecutorRuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * Get the return value (exit code) of the execution.
	 * @return the return value (exit code) of the execution
	 */
	public Integer getReturnValue() {
		return p.exitValue();
	}

	/**
	 * Get the output (print statemnt) of the execution.
	 * @return the output (print statemnt) of the execution
	 */
	public String getProcessOutput() {
		StringWriter writer = new StringWriter();
		try {
			IOUtils.copy(p.getInputStream(), writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return writer.toString();
	}

}
