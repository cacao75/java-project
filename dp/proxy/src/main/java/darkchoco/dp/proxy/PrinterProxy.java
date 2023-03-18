package darkchoco.dp.proxy;

import lombok.Getter;

@Getter
public class PrinterProxy implements Printable {

    private String printerName;
    private Printable printer;  // Printer 타입이 아니라 Printable 타입이다
    private final String printerClassName;

    public PrinterProxy(String printerName, String printerClassName) {
        this.printerName = printerName;
        this.printerClassName = printerClassName;
    }

    @Override
    public void setPrinterName(String name) {
        if (printer != null)
            printer.setPrinterName(name);

        this.printerName = name;
    }

    @Override
    public void print(String str) {
        realize();
        printer.print(str);
    }

    private void realize() {
        if (printer == null) {
            try {
                // https://stackoverflow.com/a/46393897
                printer = (Printable)Class.forName(printerClassName).getDeclaredConstructor().newInstance();
                printer.setPrinterName(printerName);
            } catch (ClassNotFoundException e) {
                System.err.println("Class " + printerClassName + " doesn't exist");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
