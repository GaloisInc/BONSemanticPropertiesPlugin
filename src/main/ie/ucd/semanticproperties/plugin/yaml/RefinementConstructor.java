package ie.ucd.semanticproperties.plugin.yaml;


import ie.ucd.semanticproperties.plugin.structs.Transitions;

import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tag;

public class RefinementConstructor extends Constructor {
    public RefinementConstructor() {
        this.yamlConstructors.put(new Tag("!transitions"),new ConstructTransition());	        
    }
    private class ConstructTransition extends AbstractConstruct {
        @SuppressWarnings("unchecked")
        public Object construct(Node node) {
        	String val = (String) constructScalar((ScalarNode) node);  
            Transitions t= Transitions.getTransitionType(val);
            return t;
        }
    } 
}