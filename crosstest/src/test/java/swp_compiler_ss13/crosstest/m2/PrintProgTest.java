package swp_compiler_ss13.crosstest.m2;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import swp_compiler_ss13.crosstest.AbstractCrosstest;
import swp_compiler_ss13.crosstest.Compiler;
import swp_compiler_ss13.fuc.backend.LLVMBackend;
import swp_compiler_ss13.javabite.backend.BackendJb;

import java.util.Collection;

@RunWith(Parameterized.class)
public class PrintProgTest extends AbstractCrosstest {
	Class backendToUse;
	
	public PrintProgTest(String testname, Class lexerToUse, Class parserToUse, Class analyserToUse, Class irgenToUse,
						 Class backToUse) {
		this.backendToUse = backToUse;
		this.compiler = new Compiler(lexerToUse, parserToUse, analyserToUse, irgenToUse, backToUse);
		assumeAllModulesPreset();
	}

	@Parameterized.Parameters(name= "{index}: {0}")
	public static Collection<Object[]> data() {
		return moduleCombinations();
	}

	@Override
	protected String getProgName() {
		return "printProg";
	}

	@Override
	protected String getExpectedOutput() {
		// the outputs are equally based on the languages input
		// so we accepting the discrepancy
		if (backendToUse == BackendJb.class) {
			return "true\n18121313223\n-2.323E-99\njagÄrEttString\"\n";
		} else if (backendToUse == LLVMBackend.class) {
			return "true\n18121313223\n-2.323000e-99\njagÄrEttString\"\n";
		} else {
			return super.getExpectedOutput();
		}
	}
}
