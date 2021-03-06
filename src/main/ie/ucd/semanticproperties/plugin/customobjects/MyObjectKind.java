package ie.ucd.semanticproperties.plugin.customobjects;

public enum MyObjectKind {
	unknown(""),
	MyInt("([-+]?[0-9]+)"),
	MyFloat( "([-+]?\\d+\\.?\\d+)"),
	Nat("(\\d+\\.?[0]*)"),
	String("((?:'.*')|(?:\".*\"))"),
	Throwable("([a-zA-Z$](?:[a-zA-Z_$0-9]*)(?:\\.[a-zA-Z_$](?:[a-zA-Z_$0-9]*))*)"),
	Date("(\\d{1,2}(?:\\/|-)\\d{1,2}(?:\\/|-)\\d{4})"),
	Email("([\\w-\\.]+@(:?[\\w-]+\\.)+[\\w-]{2,4})"),
	Class("([a-zA-Z$](?:[a-zA-Z_$0-9]*)(?:\\.[a-zA-Z_$](?:[a-zA-Z_$0-9]*))*)"),
	Description("(.+?\\.)"),
	Version("([0-9](?:.[0-9])*)"),
	Expression("(\\(.+?\\))"),
	URL("((?:mailto\\:|(?:news|(?:ht|f)tp(?:s?))\\://){1}\\S+)");
	MyObjectKind(String s){
		reg=s;
		
	}
	public String getReg(){
		return reg;
	}
	String reg;
}