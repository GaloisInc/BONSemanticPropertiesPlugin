package ie.ucd.semantic_properties_plugin.example;

import ie.ucd.semantic_properties_plugin.file_checker.CustomConstructor;
import ie.ucd.semantic_properties_plugin.file_checker.CustomRepresenter;
import ie.ucd.semantic_properties_plugin.file_checker.CustomResolver;
import ie.ucd.semantic_properties_plugin.file_checker.RefinementProperty;

import java.io.File;

import org.yaml.snakeyaml.Dumper;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Loader;
import org.yaml.snakeyaml.Yaml;

public class PropertyUse {
	public static void main (String[] args){
		
		File conYaml = new File("resources/examples/concurrency_refinement.yaml");
		
		RefinementProperty r= new RefinementProperty(conYaml);
		
//		Yaml yaml = new Yaml(new Loader(new CustomConstructor()), new Dumper(
//				new CustomRepresenter(), new DumperOptions()),
//				new CustomResolver());
////	
//		Object data = yaml.loadAll(conYaml);
//		Object da= yaml.load(conYaml);
//		
	}

}