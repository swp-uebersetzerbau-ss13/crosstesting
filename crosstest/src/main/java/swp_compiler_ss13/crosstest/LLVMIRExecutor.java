package swp_compiler_ss13.crosstest;


import org.apache.log4j.Logger;
import swp_compiler_ss13.common.backend.Backend;
import swp_compiler_ss13.common.backend.BackendException;
import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.fuc.backend.LLVMBackend;
import swp_compiler_ss13.fuc.backend.QuadrupleImpl;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LLVMIRExecutor {

	/**
	 * The logger used to output information.
	 *
	 */
	private static Logger logger = Logger.getLogger(LLVMIRExecutor.class);

	/**
	 * Tries to start <code>lli</code> as a process.
	 * @return the process identifier
	 * @throws java.io.IOException if <code>lli</code> is not found
	 */
	public static Process tryToStartLLI() throws IOException {
		ProcessBuilder pb = new ProcessBuilder("lli", "-jit-enable-eh", "-");
		pb.redirectErrorStream(true);
		Process p = null;
		try {
			p = pb.start();
		} catch (IOException e) {
			String errorMsg = "If you have LLVM installed you might need to check your PATH:\n" +
					"Intellij IDEA: Run -> Edit Configurations -> Environment variables\n" +
					"Eclipse: Run Configurations -> Environment\n" +
					"Shell: Check $PATH";
			logger.error("No lli (interpreter and dynamic compiler, part of LLVM) found.");
			logger.info(errorMsg);
			logger.error(e.getStackTrace());
			throw e;
		}
		return p;
	}

	/**
	 * Exectues LLVM IR code via LLVM's <code>lli</code> tool and shows the
	 * result of that execution (exit code and output from the programm).
	 *
	 * @param irCode
	 *            an <code>InputStream</code> of LLVM IR Code
	 * @return the LLVM IR Code as String, the output and the exit code of the execution of the LLVM IR code
	 * @exception java.io.IOException
	 *                if an error occurs reading the InputStream or starting <code>lli</code>
	 * @exception InterruptedException
	 *                if an error occurs in the LLVM Backend
	 * @throws swp_compiler_ss13.common.backend.BackendException
	 *             if an error occurs
	 */
	public static ExecutionResult runIR(InputStream irCode) throws InterruptedException, BackendException, IOException {

		BufferedReader irCodeReader = new BufferedReader(new InputStreamReader(irCode));
		StringBuilder irCodeStringBuilder = new StringBuilder();

		Process p = tryToStartLLI();

		/* write LLVM IR code to stdin of the lli process */
		PrintWriter processInputStream = new PrintWriter(p.getOutputStream());
		String line = null;
		while ((line = irCodeReader.readLine()) != null) {
			irCodeStringBuilder.append(line + "\n");
			processInputStream.println(line);
		}
		processInputStream.close();

		/* read stdout from lli process */
		BufferedReader outPutReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		StringBuilder executionOutput = new StringBuilder();
		line = null;
		while ((line = outPutReader.readLine()) != null) {
			executionOutput.append(line + "\n");
		}

		int executionExitCode = p.waitFor();

		return new ExecutionResult(executionOutput.toString(), executionExitCode, irCodeStringBuilder.toString());
	}



	/**
	 * The result of executing LLVM IR Code. The result consists of the output
	 * and the exit code of the execution and the IR code that was executed.
	 */
	public static class ExecutionResult {
		public String output;
		public Integer exitCode;
		public String irCode;

		public ExecutionResult(String output, int exitCode, String irCode) {
			this.output = output;
			this.exitCode = exitCode;
			this.irCode = irCode;
		}
	}
}
