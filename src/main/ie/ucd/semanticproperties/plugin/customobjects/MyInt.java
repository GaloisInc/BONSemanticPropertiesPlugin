package ie.ucd.semanticproperties.plugin.customobjects;

public class MyInt extends MyNumberObject {
	public MyInt(String newId, long newValue) {
		super(newId,newValue);
	}
	public MyInt() {
		super();
	}
	@Override
	public MyObjectKind getKind() {
		return MyObjectKind.MyInt;
	}
//	@Override
//	public String getReg() {
//		String reg= "([-+]?[1-9][0-9]+)";
//		return reg;
//	}

}
