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

    // printer object 유무 판단이 다수의 Thread 실행시 오류가 날 수 있기 때문에 synchronized 로 '보호'한다
    @Override
    public synchronized void setPrinterName(String name) {
        if (printer != null)
            printer.setPrinterName(name);

        this.printerName = name;
    }

    @Override
    public void print(String str) {
        realize();
        printer.print(str);
    }

    // 여기서도 printer object 유무 판단이 있기 때문에 synchronized를 추가한다
    private synchronized void realize() {
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
