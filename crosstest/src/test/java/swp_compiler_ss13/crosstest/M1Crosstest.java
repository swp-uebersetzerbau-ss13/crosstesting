package swp_compiler_ss13.crosstest;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import swp_compiler_ss13.common.backend.BackendException;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.test.ExampleProgs;

import java.io.IOException;
import java.util.Collection;

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
 * All example progs can be found in {@link swp_compiler_ss13.common.test.ExampleProgs}.
 * </p>
 *
 * @author Jens V. Fischer
 */
@RunWith(Parameterized.class)
public class M1Crosstest extends AbstractCrosstest {

	private static Logger logger = Logger.getLogger(M1Crosstest.class);

	public M1Crosstest(String testname, Class lexerToUse, Class parserToUse, Class analyserToUse, Class irgenToUse, Class backToUse) {
		compiler = new Compiler(lexerToUse, parserToUse, analyserToUse, irgenToUse, backToUse);
		assumeAllModulesPreset();
	}

	@Parameterized.Parameters(name= "{index}: {0}")
	public static Collection<Object[]> data() {
		return moduleCombinations();
	}

	@Before
	public void setUp() throws Exception {
		compiler.resetErrlog();
	}


	/* M1 progs */

	@Test
	public void testSimpleAddProg() throws IOException, InterruptedException, BackendException, IntermediateCodeGeneratorException {
		testProgCompilation(ExampleProgs.simpleAddProg());
	}

	@Test
	public void testAddProg() throws IOException, InterruptedException, BackendException, IntermediateCodeGeneratorException {
		testProgCompilation(ExampleProgs.addProg());
	}

	@Test
	public void testSimpleMulProg() throws IOException, InterruptedException, BackendException, IntermediateCodeGeneratorException {
		testProgCompilation(ExampleProgs.simpleMulProg());
	}

	@Test
	public void testParenthesesProg() throws IOException, InterruptedException, BackendException, IntermediateCodeGeneratorException {
		testProgCompilation(ExampleProgs.parenthesesProg());
	}

	/* M1 progs producing errors */

	@Test
	public void testDoubleDeclaration() throws Exception {
		testProgCompilation(ExampleProgs.doubleDeclaration());
	}

	@Test
	public void testInvalidIds() throws Exception {
		testProgCompilation(ExampleProgs.invalidIds());
	}

	@Test
	public void testMultipleMinusENotation() throws Exception {
		testProgCompilation(ExampleProgs.multipleMinusENotation());
	}

	@Test
	public void testMultiplePlusesInExp() throws Exception {
		testProgCompilation(ExampleProgs.multiplePlusesInExp());
	}

	@Test
	public void testUndefReturn() throws Exception {
		testProgCompilation(ExampleProgs.undefReturnProg());
	}
}