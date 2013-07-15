package swp_compiler_ss13.crosstest;

import org.apache.log4j.Logger;
import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.backend.Backend;
import swp_compiler_ss13.common.backend.BackendException;
import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.ir.IntermediateCodeGenerator;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.parser.Parser;
import swp_compiler_ss13.common.semanticAnalysis.SemanticAnalyser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * Compiler
 */
public class Compiler {

	private static Logger logger = Logger.getLogger(Compiler.class);


	protected Lexer lexer;
	protected Parser parser;
	protected SemanticAnalyser analyser;
	protected IntermediateCodeGenerator irgen;
	protected Backend backend;
	protected ReportLogImpl errlog;
	protected ReportLogImpl errlogAfterParser;
	protected ReportLogImpl errlogAfterAnalyzer;

	/**
	 * Initializes a compiler with the respective modules from either javabite or fuc.
	 * @param lexerToUse The lexer to use
	 * @param parserToUse the parser to use
	 * @param analyserToUse the semantic analyzer to use
	 * @param irgenToUse the intermediate code generator to use
	 * @param backendToUse the backend to use
	 */
	public Compiler(Class lexerToUse, Class parserToUse, Class analyserToUse, Class irgenToUse, Class backendToUse) {
		lexer = (Lexer) getModule(Lexer.class, lexerToUse);
		parser = (Parser) getModule(Parser.class, parserToUse);
		analyser = (swp_compiler_ss13.common.semanticAnalysis.SemanticAnalyser) getModule(swp_compiler_ss13.common.semanticAnalysis.SemanticAnalyser.class, analyserToUse);
		irgen = (IntermediateCodeGenerator) getModule(IntermediateCodeGenerator.class, irgenToUse);
		backend = (Backend) getModule(Backend.class, backendToUse);
		errlog = new ReportLogImpl();
	}


	/**
	 * Compiles a programme.
	 * @param prog the programme to compile
	 * @return the compilation results (target language code)
	 * @throws UnsupportedEncodingException if no utf-8 encoding
	 * @throws CloneNotSupportedException if the cloning of the ReportLog fails
	 * @throws IntermediateCodeGeneratorException if an error occurs in the Intermediate Code Generator
	 * @throws BackendException if an error occurs in the Backend
	 */
	public Map<String, InputStream> compile(String prog) throws UnsupportedEncodingException, CloneNotSupportedException, IntermediateCodeGeneratorException, BackendException {
		errlog = new ReportLogImpl();

		lexer.setSourceStream(new ByteArrayInputStream(prog.getBytes("UTF-8")));

		parser.setLexer(lexer);
		parser.setReportLog(errlog);
		AST ast = parser.getParsedAST();
		errlogAfterParser = errlog.clone();
		if (errlog.hasErrors())
			return null;

		analyser.setReportLog(errlog);
		AST ast2 = analyser.analyse(ast);
		errlogAfterAnalyzer = errlog.clone();
		if (errlog.hasErrors())
			return null;

		List<Quadruple> tac = irgen.generateIntermediateCode(ast2);

		Map<String, InputStream> targets = backend.generateTargetCode("prog", tac);

		return targets;
	}

	/**
	 * Get the ReportLog
	 * @return the ReportLog
	 */
	public ReportLogImpl getReportLog() {
		return errlog;
	}

	/**
	 * Get a clone of the ReportLog representing the state of the ReportLog after the parser.
	 * @return the ReportLog after the parser
	 */
	public ReportLogImpl getReportLogAfterParser() {
		return errlogAfterParser;
	}

	/**
	 * Get a clone of the ReportLog representing the state of the ReportLog after the semantic analyzer.
	 * @return the ReportLog after the semantic analyzer
	 */
	public ReportLogImpl getReportLogAfterAnalyzer() {
		return errlogAfterAnalyzer;
	}

	private Object getModule(Class moduleClass, Class implClass){
		ServiceLoader serviceLoader = ServiceLoader.load(moduleClass);
		Iterator iterator = serviceLoader.iterator();

		while (iterator.hasNext()) {
			Object module = iterator.next();
			if (module.getClass().equals(implClass)) {
				return module;
			}
		}
		return null;
	}

}
