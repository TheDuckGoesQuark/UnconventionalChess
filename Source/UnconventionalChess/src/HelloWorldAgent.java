import jade.core.Agent;

public class HelloWorldAgent extends Agent {
    protected void setup() {
        System.out.println("Hello world! I'm an agent!");
        System.out.println("My local name is " + getAID().getLocalName());
        System.out.println("My GUID is " + getAID().getName());
        System.out.println("My addresses are " + String.join(",", getAID().getAddressesArray()));

        Object[] args = getArguments();
        System.out.println("My arguments are:");
        if (args != null) {
            for (Object arg : args) {
                System.out.println("" + arg);
            }
        }
    }
}
