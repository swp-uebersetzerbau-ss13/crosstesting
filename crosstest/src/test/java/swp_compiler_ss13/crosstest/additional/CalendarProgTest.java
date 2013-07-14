package swp_compiler_ss13.crosstest.additional;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import swp_compiler_ss13.common.test.ExampleProgs;
import swp_compiler_ss13.crosstest.AbstractCrosstest;
import swp_compiler_ss13.crosstest.Compiler;
import swp_compiler_ss13.common.test.Program;

import java.util.Collection;

//@Ignore("does not terminate in test[2: LexerJb->ParserJb->SemanticAnalyserJb->IntermediateCodeGeneratorImpl->BackendJb]")
@RunWith(Parameterized.class)
public class CalendarProgTest extends AbstractCrosstest {

	public CalendarProgTest(String testname, Class lexerToUse, Class parserToUse, Class analyserToUse, Class irgenToUse,
							Class backToUse) {
		this.compiler = new Compiler(lexerToUse, parserToUse, analyserToUse, irgenToUse, backToUse);
		this.program = ExampleProgs.calendarProg();
		assumeAllModulesPreset();
	}

	@Parameterized.Parameters(name= "{index}: {0}")
	public static Collection<Object[]> data() {
		return moduleCombinations();
	}

}
