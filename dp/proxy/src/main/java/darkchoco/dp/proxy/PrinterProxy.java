package darkchoco.dp.proxy;

import lombok.Getter;

@Getter
public class PrinterProxy implements Printable {

    private String printerName;
    private Printer printer;

    public PrinterProxy(String printerName) {
        this.printerName = printerName;
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
        if (printer == null)
            printer = new Printer(printerName);
    }
}
