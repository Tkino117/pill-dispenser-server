import controller.Controller;

public class Main {
    public static void main(String[] args) {
        int port = 8080;
        Controller controller = new Controller(port);
//        while (true) {
//            try {
//                Thread.sleep(3000);
//                controller.sendMessage("hello from main");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }
}