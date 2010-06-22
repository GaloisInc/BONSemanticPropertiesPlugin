package ie.ucd.semantic_properties_plugin.file_checker;

import java.util.regex.Pattern;

import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.resolver.Resolver;

public class CustomResolver extends Resolver {

 
    protected void addImplicitResolvers() {
    	String r="java\\.(.)*\\.(.)*";
    	Pattern namePattern = Pattern.compile(r);
    	addImplicitResolver(new Tag("java.lang"), namePattern, "java.lang.Throwable;");
        addImplicitResolver(Tag.BOOL, BOOL, "yYnNtTfFoO");
        // addImplicitResolver(Tags.FLOAT, FLOAT, "-+0123456789.");
        addImplicitResolver(Tag.INT, INT, "-+0123456789");
        addImplicitResolver(Tag.MERGE, MERGE, "<");
        addImplicitResolver(Tag.NULL, NULL, "~nN\0");
        addImplicitResolver(Tag.NULL, EMPTY, null);
        // addImplicitResolver(Tags.TIMESTAMP, TIMESTAMP, "0123456789");
        addImplicitResolver(Tag.VALUE, VALUE, "=");
    }
}