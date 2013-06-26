package swp_compiler_ss13.crosstest;

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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

public class Compiler {


	protected Lexer lexer;
	protected Parser parser;
	protected SemanticAnalyser analyser;
	protected IntermediateCodeGenerator irgen;
	protected Backend backend;
	protected ReportLogImpl errlog;

	public Compiler(Class lexerToUse, Class parserToUse, Class analyserToUse, Class irgenToUse, Class backToUse) {
		lexer = (Lexer) getModule(Lexer.class, lexerToUse);
		parser = (Parser) getModule(Parser.class, parserToUse);
		analyser = (swp_compiler_ss13.common.semanticAnalysis.SemanticAnalyser) getModule(swp_compiler_ss13.common.semanticAnalysis.SemanticAnalyser.class, analyserToUse);
		irgen = (IntermediateCodeGenerator) getModule(IntermediateCodeGenerator.class, irgenToUse);
		backend = (Backend) getModule(Backend.class, backToUse);
		errlog = new ReportLogImpl();
	}



	protected ReportLogImpl compileForError(String prog) throws BackendException,
			IntermediateCodeGeneratorException, IOException, InterruptedException {
		lexer.setSourceStream(new ByteArrayInputStream(prog.getBytes("UTF-8")));
		parser.setLexer(lexer);
		parser.setReportLog(errlog);
		AST ast = parser.getParsedAST();
		if (errlog.hasErrors()){
			return errlog;
		}
		analyser.setReportLog(errlog);
		analyser.analyse(ast);
		return errlog;
	}

	protected InputStream compile(String prog) throws BackendException,
			IntermediateCodeGeneratorException, IOException, InterruptedException {
		lexer.setSourceStream(new ByteArrayInputStream(prog.getBytes("UTF-8")));
		parser.setLexer(lexer);
		parser.setReportLog(errlog);
		AST ast = parser.getParsedAST();
		analyser.setReportLog(errlog);
		AST ast2 = analyser.analyse(ast);
		List<Quadruple> tac = irgen.generateIntermediateCode(ast2);
		Map<String, InputStream> targets = backend.generateTargetCode("prog", tac);
		return targets.get(targets.keySet().iterator().next());
	}

	private Object getModule(Class moduleClass, Class implClass){
		ServiceLoader serviceLoader = ServiceLoader.load(moduleClass);
		Iterator iterator = serviceLoader.iterator();

		while (iterator.hasNext()) {
			Object module = iterator.next();
			if (module.getClass().equals(implClass)) {
//				logger.info("ToDo");
				return module;
			}
		}
		return null;
	}

	void resetErrlog(){
		errlog = new ReportLogImpl();
	}

}
