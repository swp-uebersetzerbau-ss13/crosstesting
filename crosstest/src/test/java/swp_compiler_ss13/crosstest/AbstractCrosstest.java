package swp_compiler_ss13.crosstest;

import junit.extensions.PA;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import swp_compiler_ss13.common.backend.BackendException;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.report.ReportType;
import swp_compiler_ss13.common.test.ExampleProgs;
import swp_compiler_ss13.fuc.backend.LLVMBackend;
import swp_compiler_ss13.fuc.ir.IntermediateCodeGeneratorImpl;
import swp_compiler_ss13.fuc.lexer.LexerImpl;
import swp_compiler_ss13.fuc.parser.ParserImpl;
import swp_compiler_ss13.fuc.semantic_analyser.SemanticAnalyser;
import swp_compiler_ss13.javabite.backend.BackendJb;
import swp_compiler_ss13.javabite.codegen.IntermediateCodeGeneratorJb;
import swp_compiler_ss13.javabite.lexer.LexerJb;
import swp_compiler_ss13.javabite.parser.ParserJb;
import swp_compiler_ss13.javabite.semantic.SemanticAnalyserJb;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * <p>
 * The cross tests test for the interchangeability of the fuc and the javabite
 * modules. All 32 possible combinations of the lexer, parser, semantic analyser
 * and backend are tests.
 * </p>
 * <p>
 * For the tests to work, the javabite moduls have to be placed into the
 * fuc/code/dist <code>fuc/code/dist/</code> directory as jar-files. The
 * javabite jars can be obtained be running <code>ant buildCompiler</code> in
 * the javabite repository.
 * </p>
 * <p>
 * The cross test are compilation tests, i.e. the test if the example program
 * compiles through all stages of the compiler, producing some kind of target
 * language code in the end. These tests only test if the compiler runs through
 * without producing errors, not for the correctness of the resulting target
 * language code.
 * </p>
 * <p>
 * All example progs can be found in {@link ExampleProgs}.
 * </p>
 * 
 * @author Jens V. Fischer
 */
public abstract class AbstractCrosstest {

	private static Logger logger = Logger.getLogger(AbstractCrosstest.class);
	protected Compiler compiler;
	private String progName;


	@Test
	public void test() throws InterruptedException, IOException, IntermediateCodeGeneratorException, BackendException {

		this.progName = getProgName();

		InputStream compilationResult = compiler.compile(getProg());
		ReportLogImpl log = compiler.getErrlog();

		String msg = "Expected ReportLog entries: " + getExpectedReportTypes()
				+ ". Actual: " + log.getEntries().toString();

		/* test for expected report log entries (errors and warnings) if program does not compile */
		if (log.hasErrors()){
			assertArrayEquals(msg, getExpectedReportTypes(), log.getErrors().toArray());
			return;
		}

		/* test for expected report log entries (i.e. warnings), if program compiles */
		assertArrayEquals(msg, getExpectedReportTypes(), log.getErrors().toArray());

		/* assert that something was compiled*/
		assertTrue(compilationResult != null);
	}

	protected abstract String getProgName();

	private String getProg(){
		return (String) ( (Object[]) PA.invokeMethod(ExampleProgs.class, this.progName + "()"))[0];
	}
	private String getOutput(){
		return (String) ( (Object[]) PA.invokeMethod(ExampleProgs.class, this.progName + "()"))[1];
	}
	private Integer getExitCode(){
		return (Integer) ( (Object[]) PA.invokeMethod(ExampleProgs.class, this.progName + "()"))[2];
	}
	private ReportType[] getExpectedReportTypes(){
		return (ReportType[]) ( (Object[]) PA.invokeMethod(ExampleProgs.class, this.progName + "()"))[3];
	}


	protected static Collection<Object[]> moduleCombinations() {
		Class[] lexerClasses = new Class[]{LexerJb.class, LexerImpl.class};
		Class[] parserClasses = new Class[]{ParserJb.class, ParserImpl.class};
		Class[] analyserClasses = new Class[]{SemanticAnalyserJb.class, SemanticAnalyser.class};
		Class[] irgenClasses = new Class[]{IntermediateCodeGeneratorJb.class, IntermediateCodeGeneratorImpl.class};
		Class[] backendClasses = new Class[]{BackendJb.class, LLVMBackend.class};
		ArrayList classes = new ArrayList();
		for (Class lexer : lexerClasses) {
			for (Class parser : parserClasses) {
				for (Class analyzer : analyserClasses) {
					for (Class irgen : irgenClasses) {
						for (Class backend : backendClasses) {
							String testname = lexer.getSimpleName() + "->" + parser.getSimpleName() + "->" + analyzer.getSimpleName() + "->" + irgen.getSimpleName() + "->" + backend.getSimpleName();
							classes.add(new Object[]{testname, lexer, parser, analyzer, irgen, backend});
						}

					}
				}
			}
		}

		return classes;
	}

	protected void assumeAllModulesPreset() {
		Assume.assumeTrue("no lexer found, aborting", compiler.lexer != null);
		Assume.assumeTrue("no parser found, aborting", compiler.parser != null);
		Assume.assumeTrue("no semantic analyser found, aborting", compiler.analyser != null);
		Assume.assumeTrue("no irgen found, aborting", compiler.irgen != null);
		Assume.assumeTrue("no lexer found, aborting", compiler.backend != null);
	}

}