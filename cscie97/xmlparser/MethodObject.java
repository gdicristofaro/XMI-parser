package cscie97.xmlparser;

public class MethodObject extends Descriptor {
	public final String signature;

	public MethodObject(String name, String description, String signature) {
		super(name, description);
		this.signature = signature;
	}	
	
	@Override
	public String toString() {
		return "[MethodObject] name: " + name + " description: " + description + " signature: " + signature; 
	}
}