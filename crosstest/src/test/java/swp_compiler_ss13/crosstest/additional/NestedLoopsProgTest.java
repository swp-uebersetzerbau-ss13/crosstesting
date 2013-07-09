package swp_compiler_ss13.crosstest.additional;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import swp_compiler_ss13.crosstest.AbstractCrosstest;
import swp_compiler_ss13.crosstest.Compiler;

import java.util.Collection;

@RunWith(Parameterized.class)
//@Ignore("doesn't terminate in LexerJb->ParserImpl->SemanticAnalyserJb->IntermediateCodeGeneratorImpl->BackendJb")
public class NestedLoopsProgTest extends AbstractCrosstest {

	public NestedLoopsProgTest(String testname, Class lexerToUse, Class parserToUse, Class analyserToUse, Class irgenToUse,
							   Class backToUse) {
		this.compiler = new Compiler(lexerToUse, parserToUse, analyserToUse, irgenToUse, backToUse);
		this.testname = testname;
		assumeAllModulesPreset();
	}

	@Parameterized.Parameters(name= "{index}: {0}")
	public static Collection<Object[]> data() {
		return moduleCombinations();
	}

	@Override
	protected String getProgName() {
		return "nestedLoopsProg";
	}
}
