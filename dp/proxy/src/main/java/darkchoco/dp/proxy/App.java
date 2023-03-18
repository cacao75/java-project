package darkchoco.dp.proxy;

public class App {

    public static void main( String[] args ) {

        // Package 명까지 다 지정해주어야 한다
        Printable p = new PrinterProxy("Crystal", "darkchoco.dp.proxy.Printer");
        System.out.println("Name is now " + p.getPrinterName());

        p.setPrinterName("Chon");
        System.out.println("Name is now " + p.getPrinterName());

        p.print("Hello, World!");
    }
}
