package ie.ucd.semanticproperties.plugin.yaml;


import ie.ucd.semanticproperties.plugin.structs.Transitions;

import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

public class RefinementRepresenter extends Representer {
	public RefinementRepresenter() {
		this.representers.put(Transitions.class, new RepresentTransition());
	}

	private class RepresentTransition implements Represent {
		public Node representData(Object data) {
			
			Transitions tran = Transitions.getTransitionType((String)data);
			return representScalar(new Tag("!transisions"), tran.getKind());
		}
	}
}