package darkchoco.dp.proxy;

public class App {

    public static void main( String[] args ) {

        Printable p = new PrinterProxy("Crystal");
        System.out.println("Name is now " + p.getPrinterName());

        p.setPrinterName("Chon");
        System.out.println("Name is now " + p.getPrinterName());

        p.print("Hello, World!");
    }
}
