package cscie97.xmlparser;

public class PropertyObject extends Descriptor {
	public final String type;

	public PropertyObject(String name, String description, String type) {
		super(name, description);
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "[PropertyObject] name: " + name + " description: " + description + " type: " + type; 
	}
}