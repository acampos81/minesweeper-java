public class MessageDisplay implements MessageListener, Displayable {

    private String message = "";

    @Override
    public void display() {
        System.out.println("---------------------------------");
        System.out.println(message);
        System.out.println("---------------------------------");
        message = "";
    }

    @Override
    public void HandleMessage(String message) {
        this.message = message;
    }
}
