package nz.co.iswe.generator.annotation;

public enum MatchMode {
	//Em uma consulta do tipo like, indica como quer fazer a pesquisa.
	EXACT,
	START,
	END,
	ANYWHERE;
	
	public String toString(){
		switch(this){
			case START: return "START";
			case EXACT: return "EXACT";
			case END:	return "END";
			default:	return "ANYWHERE";
		}
	}
}
