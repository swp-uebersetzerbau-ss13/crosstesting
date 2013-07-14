package swp_compiler_ss13.crosstest.m1;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import swp_compiler_ss13.common.test.ExampleProgs;
import swp_compiler_ss13.crosstest.AbstractCrosstest;
import swp_compiler_ss13.crosstest.Compiler;
import swp_compiler_ss13.common.test.Program;

import java.util.Collection;

@RunWith(Parameterized.class)
public class MultipleMinusENotationProgTest extends AbstractCrosstest {

	public MultipleMinusENotationProgTest(String testname, Class lexerToUse, Class parserToUse, Class analyserToUse, Class irgenToUse,
										  Class backToUse) {
		this.compiler = new Compiler(lexerToUse, parserToUse, analyserToUse, irgenToUse, backToUse);
		this.program = ExampleProgs.multipleMinusENotation();
		assumeAllModulesPreset();
	}

	@Parameterized.Parameters(name= "{index}: {0}")
	public static Collection<Object[]> data() {
		return moduleCombinations();
	}

}
