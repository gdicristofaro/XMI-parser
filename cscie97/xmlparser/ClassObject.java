package cscie97.xmlparser;

import java.util.Collection;

public class ClassObject extends Descriptor {
	
	public final Collection<MethodObject> methods;
	public final Collection<PropertyObject> properties;
	public final Collection<AssociationObject> associations;
	
	public ClassObject(String name, String description,
			Collection<MethodObject> methods,
			Collection<PropertyObject> properties,
			Collection<AssociationObject> associations) {
		super(name, description);
		this.methods = methods;
		this.properties = properties;
		this.associations = associations;
	}
}