package swp_compiler_ss13.crosstest.m3;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import swp_compiler_ss13.crosstest.AbstractCrosstest;
import swp_compiler_ss13.crosstest.Compiler;
import swp_compiler_ss13.fuc.backend.LLVMBackend;
import swp_compiler_ss13.javabite.backend.BackendJb;

import java.util.Collection;

@RunWith(Parameterized.class)
public class NewtonProgTest extends AbstractCrosstest {

	private Class backendToUse;

	public NewtonProgTest(String testname, Class lexerToUse, Class parserToUse, Class analyserToUse, Class irgenToUse,
						  Class backendToUse) {
		this.compiler = new Compiler(lexerToUse, parserToUse, analyserToUse, irgenToUse, backendToUse);
		this.backendToUse = backendToUse;
		assumeAllModulesPreset();
	}

	@Parameterized.Parameters(name= "{index}: {0}")
	public static Collection<Object[]> data() {
		return moduleCombinations();
	}

	@Override
	protected String getProgName() {
		return "newtonProg";
	}

	@Override
	protected String getExpectedOutput() {
		/* allow different floating point precision */
		if (backendToUse == BackendJb.class) {
			return "i hate floating point numbers1.4142156862745097\n";
		} else if (backendToUse == LLVMBackend.class) {
			return "i hate floating point numbers1.414216e+00\n";
		} else {
			return super.getExpectedOutput();
		}
	}
}
