package swp_compiler_ss13.crosstest;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.report.ReportLog;
import swp_compiler_ss13.common.report.ReportType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReportLogImpl implements ReportLog, Cloneable {
	List<ReportType> errorList = new ArrayList<>();
	List<ReportType> warningList = new ArrayList<>();
	

	@Override
	public void reportWarning(ReportType type, List<Token> tokens,
			String message) {
		warningList.add(type);
	}

	@Override
	public void reportError(ReportType type, List<Token> tokens, String message) {
		errorList.add(type);
	}

	public boolean hasErrors(){
		return ! errorList.isEmpty();
	}

	public boolean hasWarnings(){
		return ! warningList.isEmpty();
	}

	public boolean hasEntries(){
		return ! errorList.isEmpty();
	}

	protected ReportLog getReportLog() {
		return this;
	}

	protected boolean afterPreprocessing(String targetClassName) {
		return errorList.isEmpty();
	}

	protected boolean afterAstGeneration(AST ast) {
		return errorList.isEmpty();
	}

	protected boolean afterSemanticalAnalysis(AST ast) {
		return errorList.isEmpty();
	}

	protected boolean afterTacGeneration(List<Quadruple> quadruples) {
		return errorList.isEmpty();
	}

	protected boolean afterTargetCodeGeneration(File mainClassFile) {
		return errorList.isEmpty();
	}

	public List<ReportType> getErrors() {
		return errorList;
	}

	public List<ReportType> getWarnings() {
		return warningList;
	}

	public List<ReportType> getEntries(){
		List<ReportType> reportTypes = new ArrayList<>(warningList);
		reportTypes.addAll(errorList);
		return reportTypes;
	}

	@Override
	protected ReportLogImpl clone() throws CloneNotSupportedException {
		List<ReportType> errorList = new ArrayList<>();
		List<ReportType> warningList = new ArrayList<>();
		for ( ReportType reportType : this.errorList)
			errorList.add(reportType);
		for (ReportType reportType : this.warningList)
			warningList.add(reportType);
		ReportLogImpl reportLog = new ReportLogImpl();
		reportLog.errorList = errorList;
		reportLog.warningList = warningList;

		return reportLog;
	}
}
