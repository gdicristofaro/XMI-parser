package cscie97.xmlparser;


public class AssociationObject extends Descriptor {
	public final String type;

	public AssociationObject(String name, String description, String type) {
		super(name, description);
		this.type = type;
	}

	@Override
	public String toString() {
		return "[AssociationObject] name: " + name + " description: " + description + " type: " + type; 
	}
}